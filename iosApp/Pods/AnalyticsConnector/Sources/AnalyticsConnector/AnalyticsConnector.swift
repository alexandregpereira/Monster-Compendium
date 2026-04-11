//
//  AnalyticsConnector.swift
//  AnalyticsConnector
//
//  Created by Brian Giori on 12/20/21.
//

import Foundation

@objc public class AnalyticsConnector : NSObject {
    
    private static let instancesLock: DispatchSemaphore = DispatchSemaphore(value: 1)
    private static var instances: [String:AnalyticsConnector] = [:]
    
    @objc public static func getInstance(_ instanceName: String) -> AnalyticsConnector {
        instancesLock.wait()
        defer { instancesLock.signal() }
        if let instance = instances[instanceName] {
            return instance
        } else {
            instances[instanceName] = AnalyticsConnector(
                eventBridge: EventBridgeImpl(),
                identityStore: IdentityStoreImpl()
            )
            return instances[instanceName]!
        }
    }
    
    @objc public let eventBridge: EventBridge
    @objc public let identityStore: IdentityStore
    
    private init(eventBridge: EventBridge, identityStore: IdentityStore) {
        self.eventBridge = eventBridge
        self.identityStore = identityStore
    }
}
