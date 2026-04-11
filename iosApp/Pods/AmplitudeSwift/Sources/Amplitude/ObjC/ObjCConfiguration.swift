import Foundation

@objc(AMPConfiguration)
public class ObjCConfiguration: NSObject {
    internal let configuration: Configuration

    @objc(initWithApiKey:)
    public static func initWithApiKey(apiKey: String) -> ObjCConfiguration {
        ObjCConfiguration(apiKey: apiKey)
    }

    @objc(initWithApiKey:instanceName:)
    public static func initWithApiKey(apiKey: String, instanceName: String) -> ObjCConfiguration {
        ObjCConfiguration(apiKey: apiKey, instanceName: instanceName)
    }

    @objc(initWithApiKey:)
    public convenience init(apiKey: String) {
        self.init(configuration: Configuration(apiKey: apiKey))
    }

    @objc(initWithApiKey:instanceName:)
    public convenience init(apiKey: String, instanceName: String) {
        self.init(configuration: Configuration(apiKey: apiKey, instanceName: instanceName))
    }

    @objc(initWithApiKey:instanceName:enableAutocaptureRemoteConfig:)
    public convenience init(apiKey: String, instanceName: String, enableAutoCaptureRemoteConfig: Bool) {
        self.init(configuration: Configuration(apiKey: apiKey,
                                               instanceName: instanceName,
                                               enableAutoCaptureRemoteConfig: enableAutoCaptureRemoteConfig))
    }

    internal init(configuration: Configuration) {
        self.configuration = configuration
    }

    @objc
    public var apiKey: String {
        configuration.apiKey
    }

    @objc
    public var flushQueueSize: Int {
        get {
            configuration.flushQueueSize
        }
        set(value) {
            configuration.flushQueueSize = value
        }
    }

    @objc
    public var flushIntervalMillis: Int {
        get {
            configuration.flushIntervalMillis
        }
        set(value) {
            configuration.flushIntervalMillis = value
        }
    }

    @objc
    public var instanceName: String {
        configuration.instanceName
    }

    @objc
    public var optOut: Bool {
        get {
            configuration.optOut
        }
        set(value) {
            configuration.optOut = value
        }
    }

    @objc
    public var logLevel: ObjCLogLevel {
        get {
            return ObjCLogLevel(configuration.logLevel)
        }
        set(value) {
            let logLevel = value.logLevel
            configuration.logLevel = logLevel
            configuration.loggerProvider.logLevel = logLevel.rawValue
        }
    }

    @objc
    public var loggerProvider: ObjCLoggerProvider? {
        get {
            { (logLevel, message) in
                switch logLevel {
                case LogLevelEnum.error.rawValue:
                    self.configuration.loggerProvider.error(message: message)
                case LogLevelEnum.warn.rawValue:
                    self.configuration.loggerProvider.warn(message: message)
                case LogLevelEnum.log.rawValue:
                    self.configuration.loggerProvider.log(message: message)
                case LogLevelEnum.debug.rawValue:
                    self.configuration.loggerProvider.debug(message: message)
                default:
                    break
                }
            }
        }
        set(value) {
            if let value = value {
                configuration.loggerProvider = ObjCLoggerProviderWrapper(logLevel: configuration.logLevel, logProvider: value)
            }
        }
    }

    @objc
    public var minIdLength: Int {
        get {
            configuration.minIdLength ?? -1
        }
        set(value) {
            configuration.minIdLength = value
        }
    }

    @objc
    public var callback: ObjCEventCallback? {
        get {
            guard let callback = configuration.callback else { return nil }
            return { (event, code, message) in callback(event.event, code, message)  }
        }
        set(value) {
            if let value = value {
                configuration.callback = { (event, code, message) in
                    value(ObjCBaseEvent(event: event), code, message)
                }
            } else {
                configuration.callback = nil
            }
        }
    }

    @objc
    public var partnerId: String? {
        get {
            configuration.partnerId
        }
        set(value) {
            configuration.partnerId = value
        }
    }

    @objc
    public var flushMaxRetries: Int {
        get {
            configuration.flushMaxRetries
        }
        set(value) {
            configuration.flushMaxRetries = value
        }
    }

    @objc
    public var useBatch: Bool {
        get {
            configuration.useBatch
        }
        set(value) {
            configuration.useBatch = value
        }
    }

    @objc
    public var serverZone: ServerZone {
        get {
            configuration.serverZone
        }
        set(value) {
            configuration.serverZone = value
        }
    }

    @objc
    public var serverUrl: String? {
        get {
            configuration.serverUrl
        }
        set(value) {
            configuration.serverUrl = value
        }
    }

    @objc
    public var plan: ObjCPlan? {
        get {
            guard let plan = configuration.plan else { return nil }
            return ObjCPlan(plan)
        }
        set(value) {
            configuration.plan = value?.plan
        }
    }

    @objc
    public var ingestionMetadata: ObjCIngestionMetadata? {
        get {
            guard let ingestionMetadata = configuration.ingestionMetadata else { return nil }
            return ObjCIngestionMetadata(ingestionMetadata)
        }
        set(value) {
            configuration.ingestionMetadata = value?.ingestionMetadata
        }
    }

    @objc
    public var enableCoppaControl: Bool {
        get {
            configuration.enableCoppaControl
        }
        set(value) {
            configuration.enableCoppaControl = value
        }
    }

    @objc
    public var trackingOptions: ObjCTrackingOptions {
        get {
            ObjCTrackingOptions(configuration.trackingOptions)
        }
        set(value) {
            configuration.trackingOptions = value.options
        }
    }

    @objc
    public var flushEventsOnClose: Bool {
        get {
            configuration.flushEventsOnClose
        }
        set(value) {
            configuration.flushEventsOnClose = value
        }
    }

    @objc
    public var minTimeBetweenSessionsMillis: Int {
        get {
            configuration.minTimeBetweenSessionsMillis
        }
        set(value) {
            configuration.minTimeBetweenSessionsMillis = value
        }
    }

    @objc
    @available(*, deprecated, renamed: "autocapture", message: "Please use `autocapture` instead.")
    /// The SDK no longer tracks changes to the defaultTracking options after initialization.
    public var defaultTracking: ObjCDefaultTrackingOptions {
        get {
            ObjCDefaultTrackingOptions(configuration.defaultTracking)
        }
        set(value) {
            configuration.defaultTracking = value.options
        }
    }

    @objc
    public var autocapture: ObjCAutocaptureOptions {
        get {
            ObjCAutocaptureOptions(options: configuration.autocapture)
        }
        set(value) {
            configuration.autocapture = value.options
        }
    }

    @objc
    public var identifyBatchIntervalMillis: Int {
        get {
            configuration.identifyBatchIntervalMillis
        }
        set(value) {
            configuration.identifyBatchIntervalMillis = value
        }
    }

    @objc
    public var migrateLegacyData: Bool {
        get {
            configuration.migrateLegacyData
        }
        set(value) {
            configuration.migrateLegacyData = value
        }
    }

    @objc
    public var offline: NSNumber? {
        get {
            return configuration.offline as NSNumber?
        }
        set(value) {
            configuration.offline = value?.boolValue
        }
    }

    @objc
    public var networkTrackingOptions: ObjCNetworkTrackingOptions {
        get {
            return ObjCNetworkTrackingOptions(configuration.networkTrackingOptions)
        }
        set(value) {
            configuration.networkTrackingOptions = value.options
        }
    }

    @objc
    public var interactionsOptions: ObjCInteractionsOptions {
        get {
            return ObjCInteractionsOptions(configuration.interactionsOptions)
        }
        set(value) {
            configuration.interactionsOptions = value.options
        }
    }

    @objc public var enableAutoCaptureRemoteConfig: Bool {
        return configuration.enableAutoCaptureRemoteConfig
    }

    @objc public var enableRequestBodyCompression: Bool {
        get {
            return configuration.enableRequestBodyCompression
        }
        set {
            configuration.enableRequestBodyCompression = newValue
        }
    }
}
