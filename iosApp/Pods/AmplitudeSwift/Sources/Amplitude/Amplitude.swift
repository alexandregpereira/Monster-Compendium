#if AMPLITUDE_DISABLE_UIKIT
@_spi(Internal) @_exported import AmplitudeCoreNoUIKit
#else
@_spi(Internal) @_exported import AmplitudeCore
#endif

import Foundation

public class Amplitude {

    public private(set) var configuration: Configuration
    private var inForeground = false

    public var sessionId: Int64 {
        sessions.sessionId
    }

    private let identityLock = NSLock()
    private var _identity = Identity()
    public var identity: Identity {
        get {
            identityLock.withLock {
                return _identity
            }
        }
        set {
            applyIdentityUpdate(newValue)
        }
    }

    private func applyIdentityUpdate(_ identity: Identity, sendIdentifyIfNeeded: Bool = true) {
        var deviceIdChanged = false
        var userIdChanged = false
        var userPropertiesChanged = false
        identityLock.withLock {
            let oldValue = _identity
            _identity = identity

            if identity.deviceId != oldValue.deviceId {
                deviceIdChanged = true
                try? storage.write(key: .DEVICE_ID, value: identity.deviceId)
            }

            if identity.userId != oldValue.userId {
                userIdChanged = true
                try? storage.write(key: .USER_ID, value: identity.userId)
            }

            // Convert to NSDictionary to allow comparison
            let oldUserProperties = oldValue.userProperties as NSDictionary
            let newUserProperties = identity.userProperties as NSDictionary
            if oldUserProperties != newUserProperties {
                userPropertiesChanged = true
            }
        }

        // Inform plugins after we've relinquished the lock
        if userIdChanged {
            timeline.apply { plugin in
                if let amplitudePlugin = plugin as? Plugin {
                    amplitudePlugin.onUserIdChanged(identity.userId)
                }
            }
        }

        if deviceIdChanged {
            timeline.apply { plugin in
                if let amplitudePlugin = plugin as? Plugin {
                    amplitudePlugin.onDeviceIdChanged(identity.deviceId)
                }
            }
        }

        if userIdChanged || deviceIdChanged || userPropertiesChanged {
            timeline.apply { plugin in
                plugin.onIdentityChanged(identity)
            }
        }

        if sendIdentifyIfNeeded, userPropertiesChanged {
            identify(userProperties: identity.userProperties)
        }
    }

    var contextPlugin: ContextPlugin
    let timeline = Timeline()
    weak var interfaceSignalProvider: InterfaceSignalProvider? {
        didSet {
#if (os(iOS) || os(tvOS) || os(visionOS) || targetEnvironment(macCatalyst)) && !AMPLITUDE_DISABLE_UIKIT
            UIKitElementInteractions.interfaceChangeProviderDidChange(for: self, from: oldValue, to: interfaceSignalProvider)
#endif
        }
    }

    public let amplitudeContext: AmplitudeContext

    lazy var storage: any Storage = {
        return self.configuration.storageProvider
    }()

    lazy var identifyStorage: any Storage = {
        return self.configuration.identifyStorageProvider
    }()

    private var sessionsLock = NSLock()
    private var _sessions: Sessions?
    var sessions: Sessions {
        get {
            sessionsLock.synchronizedLazy(&_sessions) {
                Sessions(amplitude: self)
            }
        }
        set {
            sessionsLock.withLock {
                _sessions = newValue
            }
        }
    }

    public lazy var logger: (any Logger)? = {
        return self.configuration.loggerProvider
    }()

    let trackingQueue = DispatchQueue(label: "com.amplitude.analytics")

    private(set) lazy var autocaptureManager: AutocaptureManager = {
        AutocaptureManager(
            context: amplitudeContext,
            trackingQueue: trackingQueue,
            autocapture: configuration.autocapture,
            rageClickEnabled: configuration.interactionsOptions.rageClick.enabled,
            deadClickEnabled: configuration.interactionsOptions.deadClick.enabled,
            enableRemoteConfig: configuration.enableAutoCaptureRemoteConfig)
    }()

    public init(
        configuration: Configuration
    ) {
        trackingQueue.suspend()
        self.configuration = configuration

#if AMPLITUDE_DISABLE_UIKIT
        let serverZone: AmplitudeCoreNoUIKit.ServerZone
#else
        let serverZone: AmplitudeCore.ServerZone
#endif
        switch configuration.serverZone {
        case .US:
            serverZone = .US
        case .EU:
            serverZone = .EU
        @unknown default:
            serverZone = .US
        }

        amplitudeContext = AmplitudeContext(apiKey: configuration.apiKey,
                                            instanceName: configuration.getNormalizeInstanceName(),
                                            serverZone: serverZone,
                                            logger: configuration.loggerProvider,
                                            remoteConfigClient: configuration.remoteConfigClient,
                                            diagnosticsClient: configuration.diagnosticsClient)

        let contextPlugin = ContextPlugin()
        self.contextPlugin = contextPlugin

        migrateApiKeyStorages()
        migrateDefaultInstanceStorages()
        if configuration.migrateLegacyData && getStorageVersion() < .API_KEY_AND_INSTANCE_NAME && isSandboxEnabled() {
            RemnantDataMigration(self).execute()
        }
        migrateInstanceOnlyStorages()

        _identity = Identity(userId: configuration.storageProvider.read(key: .USER_ID),
                             deviceId: configuration.storageProvider.read(key: .DEVICE_ID),
                             userProperties: [:])

        // Trigger lazy initialization before plugins are set up (plugins may query it during setup)
        _ = autocaptureManager

        if configuration.offline != NetworkConnectivityCheckerPlugin.Disabled,
           VendorSystem.current.networkConnectivityCheckingEnabled {
            _ = add(plugin: NetworkConnectivityCheckerPlugin())
        }
        // required plugin for specific platform, only has lifecyclePlugin now
        if let requiredPlugin = VendorSystem.current.requiredPlugin {
            _ = add(plugin: requiredPlugin)
        }
        _ = add(plugin: contextPlugin)
        _ = add(plugin: AnalyticsConnectorPlugin())
        _ = add(plugin: AnalyticsConnectorIdentityPlugin())
        _ = add(plugin: AmplitudeDestinationPlugin())
        _ = add(plugin: NetworkTrackingPlugin())

        // Monitor changes to optOut to send to Timeline
        configuration.optOutChanged = { [weak self] optOut in
            self?.timeline.apply {
                $0.onOptOutChanged(optOut)
            }
        }

        trackingQueue.async { [self] in
            self.trimQueuedEvents()
        }
        trackingQueue.resume()

        amplitudeContext.diagnosticsClient.setTag(name: "sdk.\(Constants.SDK_LIBRARY).version", value: Constants.SDK_VERSION)
        autocaptureManager.updateDiagnostics()
    }

    convenience init(apiKey: String, configuration: Configuration) {
        configuration.apiKey = apiKey
        self.init(configuration: configuration)
    }

    @discardableResult
    public func track(event: BaseEvent, options: EventOptions? = nil, callback: EventCallback? = nil) -> Amplitude {
        if options != nil {
            event.mergeEventOptions(eventOptions: options!)
        }
        if callback != nil {
            event.callback = callback
        }
        process(event: event)
        return self
    }

    @discardableResult
    public func track(eventType: String, eventProperties: [String: Any]? = nil, options: EventOptions? = nil) -> Amplitude {
        let event = BaseEvent(eventType: eventType)
        event.eventProperties = eventProperties
        if let eventOptions = options {
            event.mergeEventOptions(eventOptions: eventOptions)
        }
        process(event: event)
        return self
    }

    @discardableResult
    @available(*, deprecated, message: "use 'track' instead")
    public func logEvent(event: BaseEvent) -> Amplitude {
        return track(event: event)
    }

    @discardableResult
    public func identify(userProperties: [String: Any]?, options: EventOptions? = nil) -> Amplitude {
        return identify(identify: convertPropertiesToIdentify(userProperties: userProperties), options: options)
    }

    @discardableResult
    public func identify(identify: Identify, options: EventOptions? = nil) -> Amplitude {
        let event = IdentifyEvent()
        event.userProperties = identify.properties as [String: Any]
        if let eventOptions = options {
            event.mergeEventOptions(eventOptions: eventOptions)

            var identity = self.identity
            var identityChanged = false
            if let userId = eventOptions.userId {
                identityChanged = true
                identity.userId = userId
            }
            if let deviceId = eventOptions.deviceId {
                identityChanged = true
                identity.deviceId = deviceId
            }
            if identityChanged {
                self.identity = identity
            }
        }
        process(event: event)
        return self
    }

    private func convertPropertiesToIdentify(userProperties: [String: Any]?) -> Identify {
        let identify = Identify()
        userProperties?.forEach { key, value in
            _ = identify.set(property: key, value: value)
        }
        return identify
    }

    @discardableResult
    public func groupIdentify(
        groupType: String,
        groupName: String,
        groupProperties: [String: Any]?,
        options: EventOptions? = nil
    ) -> Amplitude {
        return groupIdentify(
            groupType: groupType,
            groupName: groupName,
            identify: convertPropertiesToIdentify(userProperties: groupProperties),
            options: options
        )
    }

    @discardableResult
    public func groupIdentify(
        groupType: String,
        groupName: String,
        identify: Identify,
        options: EventOptions? = nil
    ) -> Amplitude {
        let event = GroupIdentifyEvent()
        var groups = [String: Any]()
        groups[groupType] = groupName
        event.groups = groups
        event.groupProperties = identify.properties
        if let eventOptions = options {
            event.mergeEventOptions(eventOptions: eventOptions)
        }
        process(event: event)
        return self
    }

    @discardableResult
    public func setGroup(
        groupType: String,
        groupName: String,
        options: EventOptions? = nil
    ) -> Amplitude {
        let identify = Identify().set(property: groupType, value: groupName)
        let event = IdentifyEvent()
        event.groups = [groupType: groupName]
        event.userProperties = identify.properties
        track(event: event, options: options)
        return self
    }

    @discardableResult
    public func setGroup(
        groupType: String,
        groupName: [String],
        options: EventOptions? = nil
    ) -> Amplitude {
        let identify = Identify().set(property: groupType, value: groupName)
        let event = IdentifyEvent()
        event.groups = [groupType: groupName]
        event.userProperties = identify.properties
        track(event: event, options: options)
        return self
    }

    @discardableResult
    @available(*, deprecated, message: "use 'revenue' instead")
    public func logRevenue() -> Amplitude {
        return self
    }

    @discardableResult
    public func revenue(
        revenue: Revenue,
        options: EventOptions? = nil
    ) -> Amplitude {
        guard revenue.isValid() else {
            logger?.warn(message: "Invalid revenue object, missing required fields")
            return self
        }

        let event = revenue.toRevenueEvent()
        if let eventOptions = options {
            event.mergeEventOptions(eventOptions: eventOptions)
        }
        _ = self.revenue(event: event)
        return self
    }

    @discardableResult
    public func revenue(event: RevenueEvent) -> Amplitude {
        process(event: event)
        return self
    }

    @discardableResult
    public func add(plugin: UniversalPlugin) -> Self {
        if let plugin = plugin as? Plugin {
            plugin.setup(amplitude: self)
        } else {
            plugin.setup(analyticsClient: self, amplitudeContext: amplitudeContext)
        }
        if let interfaceSignalProvider = plugin as? InterfaceSignalProvider {
            self.interfaceSignalProvider = interfaceSignalProvider
        }
        timeline.add(plugin: plugin)
        return self
    }

    @discardableResult
    public func remove(plugin: UniversalPlugin) -> Amplitude {
        if self.interfaceSignalProvider === plugin {
            self.interfaceSignalProvider = nil
        }
        timeline.remove(plugin: plugin)
        return self
    }

    @discardableResult
    public func flush() -> Amplitude {
        trackingQueue.async {
            self.timeline.apply { plugin in
                if let _plugin = plugin as? EventPlugin {
                    _plugin.flush()
                }
            }
        }
        return self
    }

    @discardableResult
    public func setUserId(userId: String?) -> Amplitude {
        identity.userId = userId
        return self
    }

    @discardableResult
    public func setDeviceId(deviceId: String?) -> Amplitude {
        identity.deviceId = deviceId
        return self
    }

    public func getUserId() -> String? {
        return identity.userId
    }

    public func getDeviceId() -> String? {
        return identity.deviceId
    }

    public func getSessionId() -> Int64 {
        return sessions.sessionId
    }

    @discardableResult
    public func setSessionId(timestamp: Int64) -> Amplitude {
        trackingQueue.async { [self, identity] in
            let sessionEvents: [BaseEvent]
            if timestamp >= 0 {
                sessionEvents = self.sessions.startNewSession(timestamp: timestamp)
            } else {
                sessionEvents = self.sessions.endCurrentSession()
            }

            if !configuration.optOut {
                self.sessions.assignEventId(events: sessionEvents).forEach { e in
                    e.userId = e.userId ?? identity.userId
                    e.deviceId = e.deviceId ?? identity.deviceId
                    self.timeline.processEvent(event: e)
                }
            }
        }
        return self
    }

    @discardableResult
    public func setSessionId(date: Date) -> Amplitude {
        let timestamp = Int64(date.timeIntervalSince1970 * 1000)
        setSessionId(timestamp: timestamp)
        return self
    }

    @discardableResult
    public func reset() -> Amplitude {
        setUserId(userId: nil)
        identity.userProperties.removeAll()
        contextPlugin.initializeDeviceId(forceReset: true)
        timeline.apply { $0.onReset() }
        return self
    }

    public func apply(closure: (Plugin) -> Void) {
        timeline.apply { plugin in
            if let plugin = plugin as? Plugin {
                closure(plugin)
            }
        }
    }

    private func process(event: BaseEvent) {
        if configuration.optOut {
            logger?.log(message: "Skip event based on opt out configuration")
            return
        }

        if event.eventType == Constants.IDENTIFY_EVENT, let userProperties = event.userProperties {
            var updatedIdentity = identity
            updatedIdentity.apply(identify: userProperties as [String: Any])
            applyIdentityUpdate(updatedIdentity, sendIdentifyIfNeeded: false)
        }

        trackingQueue.async { [self, identity, inForeground] in
            let events = self.sessions.processEvent(event: event, inForeground: inForeground)
            events.forEach { e in
                e.userId = e.userId ?? identity.userId
                e.deviceId = e.deviceId ?? identity.deviceId
                self.timeline.processEvent(event: e)
            }
        }
    }

    func onEnterForeground(timestamp: Int64) {
        inForeground = true
        let dummySessionStartEvent = BaseEvent(
            timestamp: timestamp,
            eventType: Constants.AMP_SESSION_START_EVENT
        )
        trackingQueue.async { [self, identity] in
            // set inForeground to false to represent state before event was fired
            let events = self.sessions.processEvent(event: dummySessionStartEvent, inForeground: false)
            if !configuration.optOut {
                events.forEach { e in
                    e.userId = e.userId ?? identity.userId
                    e.deviceId = e.deviceId ?? identity.deviceId
                    self.timeline.processEvent(event: e)
                }
            }
        }
    }

    func onExitForeground(timestamp: Int64) {
        inForeground = false
        trackingQueue.async { [self] in
            self.sessions.lastEventTime = timestamp
        }
        if configuration.flushEventsOnClose == true {
            flush()
        }
    }

    private func getStorageVersion() -> PersistentStorageVersion {
        let storageVersionInt: Int? = configuration.storageProvider.read(key: .STORAGE_VERSION)
        let storageVersion: PersistentStorageVersion = (storageVersionInt == nil) ? PersistentStorageVersion.NO_VERSION : PersistentStorageVersion(rawValue: storageVersionInt!)!
        return storageVersion
    }

    private func migrateApiKeyStorages() {
        if getStorageVersion() >= PersistentStorageVersion.API_KEY {
            return
        }
        configuration.loggerProvider.debug(message: "Running migrateApiKeyStorages")
        if let persistentStorage = configuration.storageProvider as? PersistentStorage {
            let apiKeyStorage = PersistentStorage(storagePrefix: "\(PersistentStorage.DEFAULT_STORAGE_PREFIX)-\(configuration.apiKey)", logger: self.logger, diagonostics: configuration.diagonostics, diagnosticsClient: self.amplitudeContext.diagnosticsClient)
            StoragePrefixMigration(source: apiKeyStorage, destination: persistentStorage, logger: logger).execute()
        }

        if let persistentIdentifyStorage = configuration.identifyStorageProvider as? PersistentStorage {
            let apiKeyIdentifyStorage = PersistentStorage(storagePrefix: "\(PersistentStorage.DEFAULT_STORAGE_PREFIX)-identify-\(configuration.apiKey)", logger: self.logger, diagonostics: configuration.diagonostics, diagnosticsClient: self.amplitudeContext.diagnosticsClient)
            StoragePrefixMigration(source: apiKeyIdentifyStorage, destination: persistentIdentifyStorage, logger: logger).execute()
        }
    }

    private func migrateDefaultInstanceStorages() {
        if getStorageVersion() >= PersistentStorageVersion.INSTANCE_NAME ||
            configuration.instanceName != Constants.Configuration.DEFAULT_INSTANCE {
            return
        }
        configuration.loggerProvider.debug(message: "Running migrateDefaultInstanceStorages")
        let legacyDefaultInstanceName = "default_instance"
        if let persistentStorage = configuration.storageProvider as? PersistentStorage {
            let legacyStorage = PersistentStorage(storagePrefix: "storage-\(legacyDefaultInstanceName)", logger: self.logger, diagonostics: configuration.diagonostics, diagnosticsClient: self.amplitudeContext.diagnosticsClient)
            StoragePrefixMigration(source: legacyStorage, destination: persistentStorage, logger: logger).execute()
        }

        if let persistentIdentifyStorage = configuration.identifyStorageProvider as? PersistentStorage {
            let legacyIdentifyStorage = PersistentStorage(storagePrefix: "identify-\(legacyDefaultInstanceName)", logger: self.logger, diagonostics: configuration.diagonostics, diagnosticsClient: self.amplitudeContext.diagnosticsClient)
            StoragePrefixMigration(source: legacyIdentifyStorage, destination: persistentIdentifyStorage, logger: logger).execute()
        }
    }

    internal func migrateInstanceOnlyStorages() {
        if getStorageVersion() >= .API_KEY_AND_INSTANCE_NAME {
            configuration.loggerProvider.debug(message: "Skipping migrateInstanceOnlyStorages based on STORAGE_VERSION")
            return
        }
        configuration.loggerProvider.debug(message: "Running migrateInstanceOnlyStorages")

        let skipEventMigration = !isSandboxEnabled()
        // Only migrate sandboxed apps to avoid potential data pollution
        if skipEventMigration {
            configuration.loggerProvider.debug(message: "Skipping event migration in non-sandboxed app. Transfering UserDefaults only.")
        }

        let instanceName = configuration.getNormalizeInstanceName()
        if let persistentStorage = configuration.storageProvider as? PersistentStorage {
            let instanceOnlyEventPrefix = "\(PersistentStorage.DEFAULT_STORAGE_PREFIX)-storage-\(instanceName)"
            let instanceNameOnlyStorage = PersistentStorage(storagePrefix: instanceOnlyEventPrefix, logger: self.logger, diagonostics: configuration.diagonostics, diagnosticsClient: self.amplitudeContext.diagnosticsClient)
            StoragePrefixMigration(
                source: instanceNameOnlyStorage,
                destination: persistentStorage,
                logger: logger
            ).execute(skipEventFiles: skipEventMigration)
        }

        if let persistentIdentifyStorage = configuration.identifyStorageProvider as? PersistentStorage {
            let instanceOnlyIdentifyPrefix = "\(PersistentStorage.DEFAULT_STORAGE_PREFIX)-identify-\(instanceName)"
            let instanceNameOnlyIdentifyStorage = PersistentStorage(storagePrefix: instanceOnlyIdentifyPrefix, logger: self.logger, diagonostics: configuration.diagonostics, diagnosticsClient: self.amplitudeContext.diagnosticsClient)
            StoragePrefixMigration(
                source: instanceNameOnlyIdentifyStorage,
                destination: persistentIdentifyStorage,
                logger: logger
            ).execute(skipEventFiles: skipEventMigration)
        }

        do {
            // Store the current storage version
            try configuration.storageProvider.write(
                key: .STORAGE_VERSION,
                value: PersistentStorageVersion.API_KEY_AND_INSTANCE_NAME.rawValue as Int
            )
            configuration.loggerProvider.debug(message: "Updated STORAGE_VERSION to .API_KEY_AND_INSTANCE_NAME")
        } catch {
            configuration.loggerProvider.error(message: "Unable to set STORAGE_VERSION in storageProvider during migration")
        }
    }

    internal func isSandboxEnabled() -> Bool {
        return SandboxHelper().isSandboxEnabled()
    }

    func trimQueuedEvents() {
        logger?.debug(message: "Trimming queued events..")
        guard configuration.maxQueuedEventCount > 0,
              let eventBlocks: [URL] = storage.read(key: .EVENTS),
              !eventBlocks.isEmpty else {
            return
        }

        var eventCount = 0
        // Blocks are returned in sorted order, oldest -> newest. Reverse to count newest blocks first.
        // Only whole blocks are deleted, meaning up to maxQueuedEventCount + flushQueueSize - 1
        // events may be left on device.
        for eventBlock in eventBlocks.reversed() {
            if eventCount < configuration.maxQueuedEventCount {
                if let eventString = storage.getEventsString(eventBlock: eventBlock),
                   let eventArray =  BaseEvent.fromArrayString(jsonString: eventString) {
                    eventCount += eventArray.count
                }
            } else {
                logger?.debug(message: "Trimming \(eventBlock)")
                storage.remove(eventBlock: eventBlock)
            }
        }
        logger?.debug(message: "Completed trimming events, kept \(eventCount) most recent events")
    }
}

extension Amplitude: PluginHost {

    public func plugin(name: String) -> UniversalPlugin? {
        return timeline.plugin(name: name)
    }

    public func plugins<PluginType: UniversalPlugin>(type: PluginType.Type) -> [PluginType] {
        var typedPlugins: [PluginType] = []
        timeline.apply { plugin in
            if let typedPlugin = plugin as? PluginType {
                typedPlugins.append(typedPlugin)
            }
        }
        return typedPlugins
    }
}

extension Amplitude: AnalyticsClient {

    public func track(eventType: String, eventProperties: [String: Any]? = nil) {
        track(eventType: eventType, eventProperties: eventProperties, options: nil)
    }

    public var optOut: Bool {
        get {
            return configuration.optOut
        }
        set {
            configuration.optOut = newValue
        }
    }
}
