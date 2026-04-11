//
//  PersistentStorageResponseHandler.swift
//
//
//  Created by Marvin Liu on 11/30/22.
//

import Foundation

#if AMPLITUDE_DISABLE_UIKIT
@_spi(Internal) import AmplitudeCoreNoUIKit
#else
@_spi(Internal) import AmplitudeCore
#endif

class PersistentStorageResponseHandler: ResponseHandler {
    var configuration: Configuration
    var storage: PersistentStorage
    var eventPipeline: EventPipeline
    var eventBlock: URL
    var eventsString: String
    let diagnosticsClient: CoreDiagnostics

    init(
        configuration: Configuration,
        storage: PersistentStorage,
        eventPipeline: EventPipeline,
        eventBlock: URL,
        eventsString: String,
        diagnosticsClient: CoreDiagnostics
    ) {
        self.configuration = configuration
        self.storage = storage
        self.eventPipeline = eventPipeline
        self.eventBlock = eventBlock
        self.eventsString = eventsString
        self.diagnosticsClient = diagnosticsClient
    }

    func handleSuccessResponse(code: Int) -> Bool {
        guard let events = BaseEvent.fromArrayString(jsonString: eventsString) else {
            storage.remove(eventBlock: eventBlock)
            removeEventCallbackByEventsString(eventsString: eventsString)
            return true
        }
        triggerEventsCallback(events: events, code: code, message: "Successfully send event")
        storage.remove(eventBlock: eventBlock)
        return true
    }

    func handleBadRequestResponse(data: [String: Any]) -> Bool {
        guard let events = BaseEvent.fromArrayString(jsonString: eventsString) else {
            storage.remove(eventBlock: eventBlock)
            removeEventCallbackByEventsString(eventsString: eventsString)
            return true
        }

        let error = data["error"] as? String ?? ""

        let isInvalidApiKey = error == "Invalid API key: \(configuration.apiKey)"
        if isInvalidApiKey {
            triggerEventsCallback(
                events: events,
                code: HttpClient.HttpStatus.BAD_REQUEST.rawValue,
                message: error
            )
            storage.remove(eventBlock: eventBlock)
            return true
        }

        var dropIndexes = Set<Int>()
        if let eventsWithInvalidFields = data["events_with_invalid_fields"] as? [String: [Int]] {
            dropIndexes.formUnion(Self.collectIndices(data: eventsWithInvalidFields))
        }
        if let eventsWithMissingFields = data["events_with_missing_fields"] as? [String: [Int]] {
            dropIndexes.formUnion(Self.collectIndices(data: eventsWithMissingFields))
        }
        if let silencedEvents = data["silenced_events"] as? [Int] {
            dropIndexes.formUnion(silencedEvents)
        }
        var silencedDevices = Set<String>()
        if let silencedDevicesArray = data["silenced_devices"] as? [String] {
            silencedDevices.formUnion(silencedDevicesArray)
        }

        var eventsToDrop = [BaseEvent]()
        var eventsToRetry = [BaseEvent]()
        for (index, event) in events.enumerated() {
            if dropIndexes.contains(index) || (event.deviceId != nil && silencedDevices.contains(event.deviceId!)) {
                eventsToDrop.append(event)
            } else {
                eventsToRetry.append(event)
            }
        }

        triggerEventsCallback(events: eventsToDrop, code: HttpClient.HttpStatus.BAD_REQUEST.rawValue, message: error)

        eventsToRetry.forEach { event in
            eventPipeline.put(event: event)
        }

        storage.remove(eventBlock: eventBlock)
        return !eventsToDrop.isEmpty
    }

    func handlePayloadTooLargeResponse(data: [String: Any]) -> Bool {
        guard let events = BaseEvent.fromArrayString(jsonString: eventsString) else {
            storage.remove(eventBlock: eventBlock)
            removeEventCallbackByEventsString(eventsString: eventsString)
            return true
        }
        if events.count == 1 {
            let error = data["error"] as? String ?? ""
            triggerEventsCallback(
                events: events,
                code: HttpClient.HttpStatus.PAYLOAD_TOO_LARGE.rawValue,
                message: error
            )
            storage.remove(eventBlock: eventBlock)
            return true
        }
        storage.splitBlock(eventBlock: eventBlock, events: events)
        return true
    }

    func handleTooManyRequestsResponse(data: [String: Any]) -> Bool {
        // wait for next time to pick it up
        return false
    }

    func handleTimeoutResponse(data: [String: Any]) -> Bool {
        // Wait for next time to pick it up
        return false
    }

    func handleFailedResponse(data: [String: Any]) -> Bool {
        // wait for next time to try again
        return false
    }

    func handle(result: Result<Int, Error>) -> Bool {
        switch result {
        case .success(let code):
            // We don't care about the data when success
            return handleSuccessResponse(code: code)
        case .failure(let error):
            switch error {
            case HttpClient.Exception.httpError(let code, let data):
                var json = [String: Any]()
                if data != nil {
                    json = (try? JSONSerialization.jsonObject(with: data!, options: []) as? [String: Any]) ?? json
                }
                switch code {
                case HttpClient.HttpStatus.BAD_REQUEST.rawValue:
                    return handleBadRequestResponse(data: json)
                case HttpClient.HttpStatus.PAYLOAD_TOO_LARGE.rawValue:
                    return handlePayloadTooLargeResponse(data: json)
                case HttpClient.HttpStatus.TIMEOUT.rawValue:
                    return handleTimeoutResponse(data: json)
                case HttpClient.HttpStatus.TOO_MANY_REQUESTS.rawValue:
                    return handleTooManyRequestsResponse(data: json)
                case HttpClient.HttpStatus.FAILED.rawValue:
                    return handleFailedResponse(data: json)
                default:
                    return handleFailedResponse(data: json)
                }
            default:
                return false
            }
        }
    }
}

extension PersistentStorageResponseHandler {
    private func triggerEventsCallback(events: [BaseEvent], code: Int, message: String) {
        events.forEach { event in
            configuration.callback?(event, code, message)
            if let eventInsertId = event.insertId, let eventCallback = storage.getEventCallback(insertId: eventInsertId)
            {
                eventCallback(event, code, message)
                storage.removeEventCallback(insertId: eventInsertId)
            }
        }

        let diagnosticsClient = self.diagnosticsClient
        if code >= 1, code < 300 {
            diagnosticsClient.increment(name: "analytics.events.sent", size: events.count)
        } else {
            diagnosticsClient.increment(name: "analytics.events.dropped", size: events.count)
            let properties: [String: any Sendable] = [
                "events": events.map { $0.eventType },
                "count": events.count,
                "code": code,
                "message": message
            ]
            diagnosticsClient.recordEvent(name: "analytics.events.dropped", properties: properties)
        }
    }

    func removeEventCallbackByEventsString(eventsString: String) {
        guard let regex = try? NSRegularExpression(pattern: #"\"insert_id\":\"(.{36})\","#) else {
            return
        }
        let eventsNSString = NSString(string: eventsString)
        regex.matches(in: eventsString, options: [], range: NSRange(location: 0, length: eventsNSString.length)).forEach
        { match in
            (1..<match.numberOfRanges).forEach {
                if match.range(at: $0).location != NSNotFound {
                    let eventInsertId = eventsNSString.substring(with: match.range(at: $0))
                    storage.removeEventCallback(insertId: eventInsertId)
                }
            }
        }
    }
}

extension PersistentStorageResponseHandler {
    func handle(result: Result<Int, any Error>) {
        let _: Bool = handle(result: result)
    }

    func handleSuccessResponse(code: Int) {
        let _: Bool = handleSuccessResponse(code: code)
    }

    func handleBadRequestResponse(data: [String: Any]) {
        let _: Bool = handleBadRequestResponse(data: data)
    }

    func handlePayloadTooLargeResponse(data: [String: Any]) {
        let _: Bool = handlePayloadTooLargeResponse(data: data)
    }

    func handleTooManyRequestsResponse(data: [String: Any]) {
        let _: Bool = handleTooManyRequestsResponse(data: data)
    }

    func handleTimeoutResponse(data: [String: Any]) {
        let _: Bool = handleTimeoutResponse(data: data)
    }

    func handleFailedResponse(data: [String: Any]) {
        let _: Bool = handleFailedResponse(data: data)
    }
}
