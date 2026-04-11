import Foundation

public class IdentifyInterceptor {
    private struct Identity: Equatable {
        let userId: String?
        let deviceId: String?

        init(_ event: BaseEvent) {
            userId = event.userId
            deviceId = event.deviceId
        }

        init(_ newUserId: String?, _ newDeviceId: String?) {
            userId = newUserId
            deviceId = newDeviceId
        }
    }

    private let configuration: Configuration
    private let pipeline: EventPipeline
    private let logger: (any Logger)?
    private var identifyTransferTimer: QueueTimer?
    private let identifyBatchIntervalMillis: Int
    private var lastIdentity: Identity?
    private let queue: DispatchQueue

    private lazy var storage: any Storage = {
        return self.configuration.identifyStorageProvider
    }()

    init(
        configuration: Configuration,
        pipeline: EventPipeline,
        queue: DispatchQueue,
        identifyBatchIntervalMillis: Int = Constants.Configuration.IDENTIFY_BATCH_INTERVAL_MILLIS
    ) {
        self.configuration = configuration
        self.pipeline = pipeline
        self.logger = configuration.loggerProvider
        if identifyBatchIntervalMillis < Constants.MIN_IDENTIFY_BATCH_INTERVAL_MILLIS {
            self.logger?.warn(message: "Minimum `identifyBatchIntervalMillis` is \(Constants.MIN_IDENTIFY_BATCH_INTERVAL_MILLIS).")
        }
        self.identifyBatchIntervalMillis = max(identifyBatchIntervalMillis, Constants.MIN_IDENTIFY_BATCH_INTERVAL_MILLIS)
        self.lastIdentity = Identity(nil, nil)
        self.queue = queue
    }

    public func intercept(event: BaseEvent) -> BaseEvent? {
        do {
            return try interceptIdentifyEvent(event)
        } catch {
            logger?.error(message: "Error when intercept event: \(error.localizedDescription)")
        }

        return event
    }

    private func isIdentityUpdated(_ event: BaseEvent) -> Bool {
        let eventIdentity = Identity(event)

        guard let currentIdenity = lastIdentity else {
            lastIdentity = eventIdentity
            return true
        }

        if eventIdentity != currentIdenity {
            lastIdentity = eventIdentity
            return true
        }

        return false
    }

    private func interceptIdentifyEvent(_ event: BaseEvent) throws -> BaseEvent? {
        if isIdentityUpdated(event) {
            transferInterceptedIdentifyEvent()
        }

        switch event.eventType  {
        case Constants.IDENTIFY_EVENT:
            if isInterceptEvent(event) {
                try writeEventToStorage(event)
                return nil
            } else if hasOperation(properties: event.userProperties, operation: Identify.Operation.CLEAR_ALL) {
                removeEventsFromStorage()
            } else {
                transferInterceptedIdentifyEvent()
            }
        case Constants.GROUP_IDENTIFY_EVENT:
            break
        default:
            if hasOperation(properties: event.userProperties, operation: Identify.Operation.CLEAR_ALL) {
                removeEventsFromStorage()
            } else {
                transferInterceptedIdentifyEvent()
            }
        }

        return event
    }

    func getCombinedInterceptedIdentify() -> IdentifyEvent? {
        var combinedInterceptedIdentify: IdentifyEvent?
        let eventFiles: [URL]? = storage.read(key: StorageKey.EVENTS)

        if let eventFiles {
            for eventFile in eventFiles {
                guard let eventsString = storage.getEventsString(eventBlock: eventFile) else {
                    continue
                }
                if eventsString.isEmpty {
                    continue
                }

                if let events: [IdentifyEvent] = BaseEvent.fromArrayString(jsonString: eventsString) {
                    for event in events {
                        if let dest = combinedInterceptedIdentify {
                            combinedInterceptedIdentify = mergeEventUserProperties(destination: dest, source: event)
                        } else {
                            combinedInterceptedIdentify = event
                        }
                    }
                }
            }

            for eventFile in eventFiles {
                storage.remove(eventBlock: eventFile)
            }
        }

        return combinedInterceptedIdentify
    }

    func getUserPropertySetValues(_ event: BaseEvent) -> [String: Any?]? {
        if let setProperties = event.userProperties?[Identify.Operation.SET.rawValue] as? [String: Any?]? {
            return setProperties
        }

        return nil
    }

    func mergeEventUserProperties(destination: IdentifyEvent, source: IdentifyEvent) -> IdentifyEvent {
        var sourceUserProperties: [String: Any?]?
        var destinationUserProperties: [String: Any?]?

        // note destination/source contain only $set properties
        sourceUserProperties = getUserPropertySetValues(source)
        destinationUserProperties = getUserPropertySetValues(destination)

        destination.userProperties = destination.userProperties ?? [:]
        destination.userProperties![Identify.Operation.SET.rawValue] = mergeUserProperties(
            destination: destinationUserProperties,
            source: sourceUserProperties
        )

        return destination
    }

    func transferInterceptedIdentifyEvent() {
        if let interceptedEvent = getCombinedInterceptedIdentify() {
            pipeline.put(event: interceptedEvent)
        }
    }

    private func writeEventToStorage(_ event: BaseEvent) throws {
        try storage.write(key: StorageKey.EVENTS, value: event)
        scheduleTransferInterceptedIdentifyEvent()
    }

    private func removeEventsFromStorage() {
        guard let eventFiles: [URL] = storage.read(key: StorageKey.EVENTS) else { return }
        for eventFile in eventFiles {
            storage.remove(eventBlock: eventFile)
        }
    }

    private func scheduleTransferInterceptedIdentifyEvent() {
        guard identifyTransferTimer == nil else {
            return
        }

        identifyTransferTimer = QueueTimer(interval: getIdentifyBatchInterval(), once: true, queue: queue) { [weak self] in
            let transferInterceptedIdentifyEvent = self?.transferInterceptedIdentifyEvent
            self?.identifyTransferTimer = nil
            transferInterceptedIdentifyEvent?()
        }
    }

    func mergeUserProperties(destination: [String: Any?]?, source: [String: Any?]?) -> [String: Any?] {
        var result = destination ?? [:]
        result = result.merging(source ?? [:]) { old, new in (new == nil || new is NSNull) ? old : new }

        return result
    }

    /**
     * Returns true if the given event should be intercepted
     */
    func isInterceptEvent(_ event: BaseEvent) -> Bool {
        return event.eventType == Constants.IDENTIFY_EVENT
            && isEmptyValues(event.groups)
            && hasOnlyOperation(properties: event.userProperties, operation: Identify.Operation.SET)
    }

    private func isEmptyValues(_ values: [String: Any?]?) -> Bool {
        return values == nil || values?.isEmpty == true
    }

    private func hasOnlyOperation(properties: [String: Any?]?, operation: Identify.Operation) -> Bool {
        return hasOperation(properties: properties, operation: operation) && properties?.count == 1
    }

    private func hasOperation(properties: [String: Any?]?, operation: Identify.Operation) -> Bool {
        return !isEmptyValues(properties) && properties![operation.rawValue] != nil
    }

    public func getIdentifyBatchInterval() -> TimeInterval {
        return TimeInterval.milliseconds(self.identifyBatchIntervalMillis)
    }
}
