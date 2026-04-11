import Foundation

class RemnantDataMigration {
    private static let DEVICE_ID_KEY = "device_id"
    private static let USER_ID_KEY = "user_id"
    private static let PREVIOUS_SESSION_TIME_KEY = "previous_session_time"
    private static let PREVIOUS_SESSION_ID_KEY = "previous_session_id"

    private let logger: (any Logger)?
    private let storage: Storage
    private let identifyStorage: Storage
    private let legacyStorage: LegacyDatabaseStorage

    init(_ amplitude: Amplitude) {
        logger = amplitude.logger
        storage = amplitude.storage
        identifyStorage = amplitude.identifyStorage
        legacyStorage = LegacyDatabaseStorage.getStorage(amplitude.configuration.instanceName, amplitude.logger)
    }

    func execute() {
        let lastEventTime: Int64? = storage.read(key: StorageKey.LAST_EVENT_TIME)
        let firstRunSinceUpgrade = lastEventTime == nil

        moveDeviceAndUserId()
        moveSessionData()

        var maxIdentifyId: Int64 = -1
        if firstRunSinceUpgrade {
            moveInterceptedIdentifies()
            maxIdentifyId = moveIdentifies()
        }
        var maxEventId = moveEvents()
        if maxEventId < maxIdentifyId {
            maxEventId = maxIdentifyId
        }

        if maxEventId > 0 {
            let currentLastEventId: Int64? = storage.read(key: StorageKey.LAST_EVENT_ID)
            if currentLastEventId == nil || currentLastEventId! <= 0 {
                try? storage.write(key: StorageKey.LAST_EVENT_ID, value: maxEventId)
            }
        }
    }

    private func moveDeviceAndUserId() {
        let currentDeviceId: String? = storage.read(key: StorageKey.DEVICE_ID)
        if currentDeviceId == nil || currentDeviceId! == "" {
            if let deviceId = legacyStorage.getValue(RemnantDataMigration.DEVICE_ID_KEY) {
                try? storage.write(key: StorageKey.DEVICE_ID, value: deviceId)
            }
        }

        let currentUserId: String? = storage.read(key: StorageKey.USER_ID)
        if currentUserId == nil || currentUserId == "" {
            if let userId = legacyStorage.getValue(RemnantDataMigration.USER_ID_KEY) {
                try? storage.write(key: StorageKey.USER_ID, value: userId)
            }
        }
    }

    private func moveSessionData() {
        let currentSessionId: Int64? = storage.read(key: StorageKey.PREVIOUS_SESSION_ID)
        let currentLastEventTime: Int64? = storage.read(key: StorageKey.LAST_EVENT_TIME)

        let previousSessionId = legacyStorage.getLongValue(RemnantDataMigration.PREVIOUS_SESSION_ID_KEY)
        let lastEventTime = legacyStorage.getLongValue(RemnantDataMigration.PREVIOUS_SESSION_TIME_KEY)

        if (currentSessionId == nil || currentSessionId! < 0) && previousSessionId != nil && previousSessionId! >= 0 {
            try? storage.write(key: StorageKey.PREVIOUS_SESSION_ID, value: previousSessionId)
            legacyStorage.removeLongValue(RemnantDataMigration.PREVIOUS_SESSION_ID_KEY)
        }

        if (currentLastEventTime == nil || currentLastEventTime! < 0) && lastEventTime != nil && lastEventTime! >= 0 {
            try? storage.write(key: StorageKey.LAST_EVENT_TIME, value: lastEventTime)
            legacyStorage.removeLongValue(RemnantDataMigration.PREVIOUS_SESSION_TIME_KEY)
        }
    }

    private func moveEvents() -> Int64 {
        var maxEventId: Int64 = -1
        let remnantEvents = legacyStorage.readEvents()
        remnantEvents.forEach { event in
            let eventId = moveEvent(event, storage, legacyStorage.removeEvent)
            if maxEventId < eventId {
                maxEventId = eventId
            }
        }
        return maxEventId
    }

    private func moveIdentifies() -> Int64 {
        var maxEventId: Int64 = -1
        let remnantEvents = legacyStorage.readIdentifies()
        remnantEvents.forEach { event in
            let eventId = moveEvent(event, storage, legacyStorage.removeIdentify)
            if maxEventId < eventId {
                maxEventId = eventId
            }
        }
        return maxEventId
    }

    private func moveInterceptedIdentifies() {
        let remnantEvents = legacyStorage.readInterceptedIdentifies()
        remnantEvents.forEach { event in
            _ = moveEvent(event, identifyStorage, legacyStorage.removeInterceptedIdentify)
        }
    }

    private func moveEvent(_ event: [String: Any], _ destinationStorage: Storage, _ removeFromSource: (_ rowId: Int64) -> Void) -> Int64 {
        do {
            let rowId = event["$rowId"] as? Int64
            let converted = convertLegacyEvent(rowId!, event)
            let jsonData = try JSONSerialization.data(withJSONObject: converted)
            let convertedEvent = BaseEvent.fromString(jsonString: String(data: jsonData, encoding: .utf8)!)
            try destinationStorage.write(key: StorageKey.EVENTS, value: convertedEvent)
            removeFromSource(rowId!)
            return rowId!
        } catch {
            logger?.error(message: "event migration failed: \(error)")
            return -1
        }
    }

    private func convertLegacyEvent(_ eventId: Int64, _ event: [String: Any]) -> [String: Any] {
        var convertedEvent = event

        convertedEvent["event_id"] = eventId

        if let library = event["library"] as? [String: Any] {
            convertedEvent["library"] = "\(library["name"] ?? "unknown")/\(library["version"] ?? "unknown")"
        }

        if let timestamp = event["timestamp"] {
            convertedEvent["time"] = timestamp
        }

        if let uuid = event["uuid"] {
            convertedEvent["insert_id"] = uuid
        }

        if let apiProperties = event["api_properties"] as? [String: Any] {
            if let idfa = apiProperties["ios_idfa"] {
                convertedEvent["idfa"] = idfa
            }

            if let idfv = apiProperties["ios_idfv"] {
                convertedEvent["idfv"] = idfv
            }

            if let productId = apiProperties["productId"] {
                convertedEvent["productId"] = productId
            }

            if let quantity = apiProperties["quantity"] {
                convertedEvent["quantity"] = quantity
            }

            if let price = apiProperties["price"] {
                convertedEvent["price"] = price
            }

            if let location = apiProperties["location"] as? [String: Any] {
                if let lat = location["lat"] {
                    convertedEvent["location_lat"] = lat
                }
                if let lng = location["lng"] {
                    convertedEvent["location_lng"] = lng
                }
            }
        }

        if let productId = event["$productId"] {
            convertedEvent["productId"] = productId
        }

        if let quantity = event["$quantity"] {
            convertedEvent["quantity"] = quantity
        }

        if let price = event["$price"] {
            convertedEvent["price"] = price
        }

        if let revenueType = event["$revenueType"] {
            convertedEvent["revenueType"] = revenueType
        }

        return convertedEvent
    }
}
