//
//  Identity.swift
//  Amplitude-Swift
//
//  Created by Chris Leonavicius on 3/28/25.
//

#if AMPLITUDE_DISABLE_UIKIT
import AmplitudeCoreNoUIKit
#else
import AmplitudeCore
#endif

public struct Identity: AnalyticsIdentity {
    public var userId: String?
    public var deviceId: String?
    public var userProperties: [String: Any] = [:]
}

extension Identity {

    mutating func apply(identify: [String: Any]) {
        var updatedProperties = userProperties

        for op in Identify.Operation.orderedCases {
            guard let properties = identify[op.rawValue] else {
                continue
            }

            let opProperties = properties as? [String: Any] ?? [:]

            switch op {
            case .SET:
                updatedProperties.merge(opProperties) { (_, new) in new }
            case .CLEAR_ALL:
                updatedProperties = [:]
            case .UNSET:
                for (key, _) in opProperties {
                    updatedProperties[key] = nil
                }
            case .SET_ONCE, .ADD, .APPEND, .PREPEND, .PRE_INSERT, .POST_INSERT, .REMOVE:
                // Unsupported
                break
            }
        }

        // Transfer properties that are not explicit operations as SETs
        let allOpStrings = Set(Identify.Operation.orderedCases.map(\.rawValue))
        for (key, value) in identify where !allOpStrings.contains(key) {
            updatedProperties[key] = value
        }

        userProperties = updatedProperties
    }
}
