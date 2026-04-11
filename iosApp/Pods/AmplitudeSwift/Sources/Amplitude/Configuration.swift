//
//  Configuration.swift
//
//
//  Created by Marvin Liu on 10/27/22.
//

import Foundation

#if AMPLITUDE_DISABLE_UIKIT
@_spi(Internal) import AmplitudeCoreNoUIKit
#else
@_spi(Internal) import AmplitudeCore
#endif

public class Configuration {

    public struct Defaults {
        public static let instanceName = Constants.Configuration.DEFAULT_INSTANCE
        public static let flushQueueSize = Constants.Configuration.FLUSH_QUEUE_SIZE
        public static let flushIntervalMillis = Constants.Configuration.FLUSH_INTERVAL_MILLIS
        public static let flushMaxRetries = Constants.Configuration.FLUSH_MAX_RETRIES
        public static let logLevel = LogLevelEnum.warn
        public static let minTimeBetweenSessionsMillis = Constants.Configuration.MIN_TIME_BETWEEN_SESSIONS_MILLIS
        public static let identifyBatchIntervalMillis = Constants.Configuration.IDENTIFY_BATCH_INTERVAL_MILLIS
        public static let serverZone = ServerZone.US
        public static let optOut = false
        public static let useBatch = false
        public static let enableCoppaControl = false
        public static let flushEventsOnClose = true
        public static let maxQueuedEventCount = -1
        public static let autocaptureOptions: AutocaptureOptions = .sessions
        public static let migrateLegacyData = true
        public static let enableAutoCaptureRemoteConfig = true
        public static let trackingOptions = TrackingOptions()
        public static let networkTrackingOptions = NetworkTrackingOptions.default
        public static let interactionsOptions = InteractionsOptions()
        public static let enableDiagnostics = true
        public static let enableRequestBodyCompression = false
    }

    public internal(set) var apiKey: String
    public var flushQueueSize: Int
    public var flushIntervalMillis: Int
    public internal(set) var instanceName: String
    public var optOut: Bool {
        didSet {
            optOutChanged?(optOut)
        }
    }
    public let storageProvider: any Storage
    public let identifyStorageProvider: any Storage
    public var logLevel: LogLevelEnum
    public var loggerProvider: any Logger
    public var minIdLength: Int?
    public var partnerId: String?
    public var callback: EventCallback?
    public var flushMaxRetries: Int
    public var useBatch: Bool
    public var serverZone: ServerZone
    public var serverUrl: String?
    public var plan: Plan?
    public var ingestionMetadata: IngestionMetadata?
    public var trackingOptions: TrackingOptions
    public var enableCoppaControl: Bool
    public var flushEventsOnClose: Bool
    public var minTimeBetweenSessionsMillis: Int
    public var identifyBatchIntervalMillis: Int
    public internal(set) var migrateLegacyData: Bool
    @available(*, deprecated, renamed: "autocapture", message: "Please use `autocapture` instead.")
    public lazy var defaultTracking: DefaultTrackingOptions = {
        DefaultTrackingOptions(delegate: self)
    }() {
        didSet {
            defaultTracking.delegate = self
            autocapture = defaultTracking.autocaptureOptions
        }
    }
    public internal(set) var autocapture: AutocaptureOptions
    public internal(set) var networkTrackingOptions: NetworkTrackingOptions
    public var offline: Bool?
    internal let diagonostics: Diagnostics
    public var maxQueuedEventCount = -1
    var optOutChanged: ((Bool) -> Void)?
    public let enableAutoCaptureRemoteConfig: Bool
    public var interactionsOptions: InteractionsOptions
    public var enableDiagnostics: Bool

    /// Controls request body compression **only** when a custom `serverUrl` is configured.
    /// When using the SDK's default endpoints, request bodies are always compressed
    /// regardless of this setting.
    public var enableRequestBodyCompression: Bool

    let remoteConfigClient: RemoteConfigClient
    let diagnosticsClient: CoreDiagnostics

    @available(*, deprecated, message: "Please use the `autocapture` parameter instead.")
    public convenience init(
        apiKey: String,
        flushQueueSize: Int = Defaults.flushQueueSize,
        flushIntervalMillis: Int = Defaults.flushIntervalMillis,
        instanceName: String = Defaults.instanceName,
        optOut: Bool = Defaults.optOut,
        storageProvider: (any Storage)? = nil,
        identifyStorageProvider: (any Storage)? = nil,
        logLevel: LogLevelEnum = Defaults.logLevel,
        loggerProvider: any Logger = ConsoleLogger(),
        minIdLength: Int? = nil,
        partnerId: String? = nil,
        callback: EventCallback? = nil,
        flushMaxRetries: Int = Defaults.flushMaxRetries,
        useBatch: Bool = Defaults.useBatch,
        serverZone: ServerZone = Defaults.serverZone,
        serverUrl: String? = nil,
        plan: Plan? = nil,
        ingestionMetadata: IngestionMetadata? = nil,
        trackingOptions: TrackingOptions = Defaults.trackingOptions,
        enableCoppaControl: Bool = Defaults.enableCoppaControl,
        flushEventsOnClose: Bool = Defaults.flushEventsOnClose,
        minTimeBetweenSessionsMillis: Int = Defaults.minTimeBetweenSessionsMillis,
        // `trackingSessionEvents` has been replaced by `defaultTracking.sessions`
        defaultTracking: DefaultTrackingOptions,
        identifyBatchIntervalMillis: Int = Defaults.identifyBatchIntervalMillis,
        migrateLegacyData: Bool = Defaults.migrateLegacyData,
        offline: Bool? = false,
        networkTrackingOptions: NetworkTrackingOptions = Defaults.networkTrackingOptions
    ) {
        self.init(apiKey: apiKey,
            flushQueueSize: flushQueueSize,
            flushIntervalMillis: flushIntervalMillis,
            instanceName: instanceName,
            optOut: optOut,
            storageProvider: storageProvider,
            identifyStorageProvider: identifyStorageProvider,
            logLevel: logLevel,
            loggerProvider: loggerProvider,
            minIdLength: minIdLength,
            partnerId: partnerId,
            callback: callback,
            flushMaxRetries: flushMaxRetries,
            useBatch: useBatch,
            serverZone: serverZone,
            serverUrl: serverUrl,
            plan: plan,
            ingestionMetadata: ingestionMetadata,
            trackingOptions: trackingOptions,
            enableCoppaControl: enableCoppaControl,
            flushEventsOnClose: flushEventsOnClose,
            minTimeBetweenSessionsMillis: minTimeBetweenSessionsMillis,
            autocapture: defaultTracking.autocaptureOptions,
            identifyBatchIntervalMillis: identifyBatchIntervalMillis,
            migrateLegacyData: migrateLegacyData,
            offline: offline,
            networkTrackingOptions: networkTrackingOptions)
        self.defaultTracking = defaultTracking
    }

    public init(
        apiKey: String,
        flushQueueSize: Int = Defaults.flushQueueSize,
        flushIntervalMillis: Int = Defaults.flushIntervalMillis,
        instanceName: String = Defaults.instanceName,
        optOut: Bool = Defaults.optOut,
        storageProvider: (any Storage)? = nil,
        identifyStorageProvider: (any Storage)? = nil,
        logLevel: LogLevelEnum = Defaults.logLevel,
        loggerProvider: any Logger = ConsoleLogger(),
        minIdLength: Int? = nil,
        partnerId: String? = nil,
        callback: EventCallback? = nil,
        flushMaxRetries: Int = Defaults.flushMaxRetries,
        useBatch: Bool = Defaults.useBatch,
        serverZone: ServerZone = Defaults.serverZone,
        serverUrl: String? = nil,
        plan: Plan? = nil,
        ingestionMetadata: IngestionMetadata? = nil,
        trackingOptions: TrackingOptions = Defaults.trackingOptions,
        enableCoppaControl: Bool = Defaults.enableCoppaControl,
        flushEventsOnClose: Bool = Defaults.flushEventsOnClose,
        minTimeBetweenSessionsMillis: Int = Defaults.minTimeBetweenSessionsMillis,
        // `trackingSessionEvents` has been replaced by `defaultTracking.sessions`
        autocapture: AutocaptureOptions = Defaults.autocaptureOptions,
        identifyBatchIntervalMillis: Int = Defaults.identifyBatchIntervalMillis,
        maxQueuedEventCount: Int = Defaults.maxQueuedEventCount,
        migrateLegacyData: Bool = Defaults.migrateLegacyData,
        offline: Bool? = false,
        networkTrackingOptions: NetworkTrackingOptions = Defaults.networkTrackingOptions,
        enableAutoCaptureRemoteConfig: Bool = Defaults.enableAutoCaptureRemoteConfig,
        interactionsOptions: InteractionsOptions = Defaults.interactionsOptions,
        enableDiagnostics: Bool = Defaults.enableDiagnostics,
        enableRequestBodyCompression: Bool = Defaults.enableRequestBodyCompression,
    ) {
        let normalizedInstanceName = Configuration.getNormalizeInstanceName(instanceName)

        self.apiKey = apiKey
        self.flushQueueSize = flushQueueSize
        self.flushIntervalMillis = flushIntervalMillis
        self.instanceName = normalizedInstanceName
        self.optOut = optOut
        self.diagonostics = Diagnostics()
        self.logLevel = logLevel
        self.loggerProvider = loggerProvider
        self.serverZone = serverZone
        self.enableDiagnostics = enableDiagnostics
        self.remoteConfigClient = RemoteConfigClient(apiKey: self.apiKey,
                                                     serverZone: self.serverZone,
                                                     instanceName: self.instanceName,
                                                     logger: self.loggerProvider)
        self.diagnosticsClient = DiagnosticsClient(apiKey: self.apiKey,
                                                   serverZone: self.serverZone,
                                                   instanceName: self.instanceName,
                                                   logger: self.loggerProvider,
                                                   enabled: self.enableDiagnostics,
                                                   remoteConfigClient: self.remoteConfigClient)
        self.storageProvider = storageProvider
        ?? PersistentStorage(storagePrefix: PersistentStorage.getEventStoragePrefix(apiKey, normalizedInstanceName), logger: self.loggerProvider, diagonostics: self.diagonostics, diagnosticsClient: self.diagnosticsClient)
        self.identifyStorageProvider = identifyStorageProvider
        ?? PersistentStorage(storagePrefix: PersistentStorage.getIdentifyStoragePrefix(apiKey, normalizedInstanceName), logger: self.loggerProvider, diagonostics: self.diagonostics, diagnosticsClient: self.diagnosticsClient)
        self.minIdLength = minIdLength
        self.partnerId = partnerId
        self.callback = callback
        self.flushMaxRetries = flushMaxRetries
        self.useBatch = useBatch
        self.serverUrl = serverUrl
        self.plan = plan
        self.ingestionMetadata = ingestionMetadata
        self.trackingOptions = trackingOptions
        self.enableCoppaControl = enableCoppaControl
        self.flushEventsOnClose = flushEventsOnClose
        self.minTimeBetweenSessionsMillis = minTimeBetweenSessionsMillis
        self.autocapture = autocapture
        self.identifyBatchIntervalMillis = identifyBatchIntervalMillis
        self.maxQueuedEventCount = maxQueuedEventCount
        self.migrateLegacyData = migrateLegacyData
        // Logging is OFF by default
        self.loggerProvider.logLevel = logLevel.rawValue
        self.offline = offline
        self.networkTrackingOptions = networkTrackingOptions
        self.enableAutoCaptureRemoteConfig = enableAutoCaptureRemoteConfig
        self.interactionsOptions = interactionsOptions
        self.enableRequestBodyCompression = enableRequestBodyCompression
    }

    func isValid() -> Bool {
        return !apiKey.isEmpty && flushQueueSize > 0 && flushIntervalMillis > 0
            && minTimeBetweenSessionsMillis > 0
            && (minIdLength == nil || minIdLength! > 0)
    }

    private class func getNormalizeInstanceName(_ instanceName: String) -> String {
        return instanceName == "" ? Constants.Configuration.DEFAULT_INSTANCE : instanceName
    }

    internal func getNormalizeInstanceName() -> String {
        return Configuration.getNormalizeInstanceName(self.instanceName)
    }
}

extension Configuration: DefaultTrackingOptionsDelegate {
    @available(*, deprecated)
    func didChangeOptions(options: DefaultTrackingOptions) {
        autocapture = options.autocaptureOptions
    }
}
