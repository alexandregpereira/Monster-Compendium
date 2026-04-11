//
//  EventBridge.swift
//  EventBridge
//
//  Created by Brian Giori on 12/21/21.
//

import Foundation

@objc public class AnalyticsEvent: NSObject {
    @objc public let eventType: String
    @objc public let eventProperties: NSDictionary?
    @objc public let userProperties: NSDictionary?
    
    @objc public init(eventType: String, eventProperties: NSDictionary?, userProperties: NSDictionary?) {
        self.eventType = eventType
        self.eventProperties = eventProperties
        self.userProperties = userProperties
    }
    
    @objc public override func isEqual(_ object: Any?) -> Bool {
        guard let other = object as? AnalyticsEvent else {
            return false
        }
        return self.eventType == other.eventType &&
            dictionaryEquals(self.eventProperties, other.eventProperties) &&
            dictionaryEquals(self.userProperties, other.userProperties)
    }
}

@objc public protocol EventBridge {
    @objc func setEventReceiver(_ eventReceiver: @escaping (AnalyticsEvent) -> ())
    @objc func logEvent(event: AnalyticsEvent)
}

@objc internal class EventBridgeImpl: NSObject, EventBridge {
    
    private let eventReceiverLock = DispatchSemaphore(value: 1)
    private var eventReceiver: ((AnalyticsEvent) -> ())? = nil
    private var eventQueue: [AnalyticsEvent] = []
    
    @objc func setEventReceiver(_ eventReceiver: @escaping (AnalyticsEvent) -> ()) {
        eventReceiverLock.wait()
        self.eventReceiver = eventReceiver
        let events = eventQueue
        eventQueue = []
        eventReceiverLock.signal()
        for event in events {
            eventReceiver(event)
        }
    }
    
    @objc func logEvent(event: AnalyticsEvent) {
        eventReceiverLock.wait()
        defer { eventReceiverLock.signal() }
        guard let eventReceiver = self.eventReceiver else {
            if (eventQueue.count < 512) {
                eventQueue.append(event)
            }
            return
        }
        eventReceiver(event)
    }
}

internal func dictionaryEquals(_ d1: NSDictionary?, _ d2: NSDictionary?) -> Bool {
    if let d1 = d1, let d2 = d2 as? [AnyHashable:Any] {
        guard d1.isEqual(to: d2) else {
            return false
        }
    } else {
        guard d1 == nil, d2 == nil else {
            return false
        }
    }
    return true
}
