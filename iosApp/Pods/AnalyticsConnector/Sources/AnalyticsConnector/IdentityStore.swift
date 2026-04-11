//
//  IdentityStore.swift
//  AnalyticsConnector
//
//  Created by Brian Giori on 12/21/21.
//

import Foundation

internal let ID_OP_SET = "$set"
internal let ID_OP_UNSET = "$unset"
internal let ID_OP_CLEAR_ALL = "$clearAll"

@objc public class Identity: NSObject {
    @objc public let userId: String?
    @objc public let deviceId: String?
    @objc public let userProperties: NSDictionary
    @objc public init(userId: String? = nil, deviceId: String? = nil, userProperties: NSDictionary? = nil) {
        self.userId = userId
        self.deviceId = deviceId
        self.userProperties = userProperties ?? NSDictionary()
    }
    @objc public override func isEqual(_ object: Any?) -> Bool {
        guard let other = object as? Identity else {
            return false
        }
        return self.userId == other.userId &&
            self.deviceId == other.deviceId &&
            dictionaryEquals(self.userProperties, other.userProperties)
    }
}

@objc public protocol IdentityStore {
    @objc func getIdentity() -> Identity
    @objc func setIdentity(_ identity: Identity)
    @objc func editIdentity() -> IdentityStoreEditor
    @objc func addIdentityListener(key: String, _ listener: @escaping (Identity) -> ())
    @objc func removeIdentityListener(key: String)
}

@objc public protocol IdentityStoreEditor {
    @objc func setUserId(_ userId: String?) -> IdentityStoreEditor
    @objc func setDeviceId(_ deviceId: String?) -> IdentityStoreEditor
    @objc func setUserProperties(_ userProperties: NSDictionary) -> IdentityStoreEditor
    @objc func updateUserProperties(_ userPropertyActions: NSDictionary) -> IdentityStoreEditor
    @objc func commit()
}

@objc internal class IdentityStoreImpl: NSObject, IdentityStore {
    private let identityLock = DispatchSemaphore(value: 1)
    private var identity = Identity()
    private let listenersLock = DispatchSemaphore(value: 1)
    private var listeners: [String: (Identity) -> ()] = [:]
    
    @objc func getIdentity() -> Identity {
        identityLock.wait()
        defer { identityLock.signal() }
        return identity
    }
    
    @objc func setIdentity(_ identity: Identity) {
        identityLock.wait()
        let identityChanged = self.identity != identity
        self.identity = identity
        identityLock.signal()
        if identityChanged {
            listenersLock.wait()
            let safeListeners = listeners.values
            listenersLock.signal()
            for listener in safeListeners {
                listener(identity)
            }
        }
    }
    
    @objc func editIdentity() -> IdentityStoreEditor {
        return IdentityStoreEditorImpl(identityStore: self)
    }
    
    @objc func addIdentityListener(key: String, _ listener: @escaping (Identity) -> ()) {
        listenersLock.wait()
        defer { listenersLock.signal() }
        listeners[key] = listener
    }
    
    @objc func removeIdentityListener(key: String) {
        listenersLock.wait()
        defer { listenersLock.signal() }
        listeners.removeValue(forKey: key)
    }
}

@objc internal class IdentityStoreEditorImpl: NSObject, IdentityStoreEditor {
    
    private let identityStore: IdentityStore
    
    private var userId: String?
    private var deviceId: String?
    private var userProperties: NSMutableDictionary
    
    internal init(identityStore: IdentityStore) {
        let identity = identityStore.getIdentity()
        self.userId = identity.userId
        self.deviceId = identity.deviceId
        self.userProperties = identity.userProperties.mutableCopy() as? NSMutableDictionary ?? NSMutableDictionary()
        self.identityStore = identityStore
    }
    
    @objc func setUserId(_ userId: String?) -> IdentityStoreEditor {
        self.userId = userId
        return self
    }
    
    @objc func setDeviceId(_ deviceId: String?) -> IdentityStoreEditor {
        self.deviceId = deviceId
        return self
    }
    
    @objc func setUserProperties(_ userProperties: NSDictionary) -> IdentityStoreEditor {
        if let userProperties = userProperties.mutableCopy() as? NSMutableDictionary {
            self.userProperties = userProperties
        }
        return self
    }
    
    @objc func updateUserProperties(_ userPropertyActions: NSDictionary) -> IdentityStoreEditor {
        userPropertyActions.forEach { (action: Any, properties: Any) in
            guard let action = action as? String else {
                return
            }
            guard let properties = properties as? [AnyHashable: Any] else {
                return
            }
            switch (action) {
            case ID_OP_SET:
                self.userProperties.addEntries(from: properties)
            case ID_OP_UNSET:
                self.userProperties.removeObjects(forKeys: Array(properties.keys))
            case ID_OP_CLEAR_ALL:
                self.userProperties.removeAllObjects()
            default: break
            }
        }
        return self
    }
    
    @objc func commit() {
        let identity = Identity(userId: userId, deviceId: deviceId, userProperties: userProperties)
        self.identityStore.setIdentity(identity)
    }
}
