//
//  Types.swift
//
//
//  Created by Marvin Liu on 10/27/22.
//

#if AMPLITUDE_DISABLE_UIKIT
import AmplitudeCoreNoUIKit
#else
import AmplitudeCore
#endif
import Foundation

public struct Plan: Codable {
    public var branch: String?
    public var source: String?
    public var version: String?
    public var versionId: String?

    public init(branch: String? = nil, source: String? = nil, version: String? = nil, versionId: String? = nil) {
        self.branch = branch
        self.source = source
        self.version = version
        self.versionId = versionId
    }
}

public struct IngestionMetadata: Codable {
    public var sourceName: String?
    public var sourceVersion: String?

    enum CodingKeys: String, CodingKey {
        case sourceName = "source_name"
        case sourceVersion = "source_version"
    }

    public init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: CodingKeys.self)
        sourceName = try values.decodeIfPresent(String.self, forKey: .sourceName)
        sourceVersion = try values.decodeIfPresent(String.self, forKey: .sourceVersion)
    }

    public init(sourceName: String? = nil, sourceVersion: String? = nil) {
        self.sourceName = sourceName
        self.sourceVersion = sourceVersion
    }
}

public typealias EventCallback = (BaseEvent, Int, String) -> Void

// Swift 5.7 supports any existential type.
// The type of EventBlock has to be determined pre-runtime.
// It cannot be dynamically associated with this protocol.
// https://github.com/apple/swift/issues/62219#issuecomment-1326531801
public protocol Storage {
    func write(key: StorageKey, value: Any?) throws
    func read<T>(key: StorageKey) -> T?
    func getEventsString(eventBlock: URL) -> String?
    func remove(eventBlock: URL)
    func splitBlock(eventBlock: URL, events: [BaseEvent])
    func rollover()
    func reset()
    func getResponseHandler(
        configuration: Configuration,
        eventPipeline: EventPipeline,
        eventBlock: URL,
        eventsString: String
    ) -> ResponseHandler
}

public enum StorageKey: String, CaseIterable {
    case LAST_EVENT_ID = "last_event_id"
    case PREVIOUS_SESSION_ID = "previous_session_id"
    case LAST_EVENT_TIME = "last_event_time"
    case OPT_OUT = "opt_out"
    case EVENTS = "events"
    case USER_ID = "user_id"
    case DEVICE_ID = "device_id"
    case APP_BUILD = "app_build"
    case APP_VERSION = "app_version"
    // The version of PersistentStorage, used for data migrations
    // Value should be a PersistentStorageVersion value
    // Note the first version is 2, which corresponds to apiKey-instanceName based storage
    case STORAGE_VERSION = "storage_version"
}

public enum PersistentStorageVersion: Int, Comparable {
    public static func < (lhs: PersistentStorageVersion, rhs: PersistentStorageVersion) -> Bool {
        return lhs.rawValue < rhs.rawValue
    }

    case NO_VERSION = -1
    // Note that versioning was added after these storage changes (0, 1)
    case API_KEY = 0
    case INSTANCE_NAME = 1
    // This is the first version (2) we set a value in storageProvider.read(.StorageVersion)
    case API_KEY_AND_INSTANCE_NAME = 2
}

public protocol Logger: CoreLogger {
    associatedtype LogLevel: RawRepresentable
    var logLevel: Int { get set }
}

@objc(AMPPluginType)
public enum PluginType: Int, CaseIterable {
    case before
    case enrichment
    case destination
    case utility
    case observe
}

public protocol Plugin: UniversalPlugin {
    var type: PluginType { get }
    func setup(amplitude: Amplitude)
    func execute(event: BaseEvent) -> BaseEvent?
    func onUserIdChanged(_ userId: String?)
    func onDeviceIdChanged(_ deviceId: String?)
    func onSessionIdChanged(_ sessionId: Int64)
    func onOptOutChanged(_ optOut: Bool)
}

public protocol EventPlugin: Plugin {
    func track(event: BaseEvent) -> BaseEvent?
    func identify(event: IdentifyEvent) -> IdentifyEvent?
    func groupIdentify(event: GroupIdentifyEvent) -> GroupIdentifyEvent?
    func revenue(event: RevenueEvent) -> RevenueEvent?
    func flush()
}

public extension Plugin {

    var name: String? {
        return nil
    }

    // default behavior
    func execute(event: BaseEvent) -> BaseEvent? {
        var event = event
        execute(&event)
        return event
    }

    func execute<Event: AnalyticsEvent>(_ event: inout Event) {
        // no-op
    }

    func setup(amplitude: Amplitude) {
        setup(analyticsClient: amplitude,
              amplitudeContext: amplitude.amplitudeContext)
    }

    func setup(analyticsClient: any AnalyticsClient, amplitudeContext: AmplitudeContext) {
        // no-op
    }

    func onUserIdChanged(_ userId: String?) {}
    func onDeviceIdChanged(_ deviceId: String?) {}
    func onSessionIdChanged(_ sessionId: Int64) {}
    func onOptOutChanged(_ optOut: Bool) {}

    func teardown(){
        // Clean up any resources from setup if necessary
    }
}

public protocol ResponseHandler {
    func handle(result: Result<Int, Error>)
    func handleSuccessResponse(code: Int)
    func handleBadRequestResponse(data: [String: Any])
    func handlePayloadTooLargeResponse(data: [String: Any])
    func handleTooManyRequestsResponse(data: [String: Any])
    func handleTimeoutResponse(data: [String: Any])
    func handleFailedResponse(data: [String: Any])

    // Added on v1.11.2.
    // A replacement for handle(result: Result<Int, Error>) -> Void
    // Return true if some attempts to recover are implemented
    func handle(result: Result<Int, Error>) -> Bool
}

extension ResponseHandler {
    static func collectIndices(data: [String: [Int]]) -> Set<Int> {
        var indices = Set<Int>()
        for (_, elements) in data {
            for el in elements {
                indices.insert(el)
            }
        }
        return indices
    }
}

// Provide compatibility for new `handle` function added on v1.11.2.
extension ResponseHandler {
    public func handle(result: Result<Int, any Error>) -> Bool {
        let _: Void = handle(result: result)
        return false
    }
}
