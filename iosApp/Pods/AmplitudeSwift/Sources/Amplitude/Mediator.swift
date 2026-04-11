//
//  Mediator.swift
//
//
//  Created by Hao Yu on 11/9/22.
//

#if AMPLITUDE_DISABLE_UIKIT
import AmplitudeCoreNoUIKit
#else
import AmplitudeCore
#endif
import Foundation

internal class Mediator {
    // create an array with certain type.
    internal var plugins = [UniversalPlugin]()
    private var pluginsByName: [String: UniversalPlugin] = [:]
    private let lock = NSLock()

    internal func add(plugin: UniversalPlugin) {
        lock.lock()
        defer { lock.unlock() }

        if let name = plugin.name {
            pluginsByName[name] = plugin
        }

        plugins.append(plugin)
    }

    internal func remove(plugin: UniversalPlugin) {
        lock.lock()
        defer { lock.unlock() }

        if let name = plugin.name {
            pluginsByName[name] = nil
        }

        plugins.removeAll { (storedPlugin) -> Bool in
            if storedPlugin === plugin {
                storedPlugin.teardown()
                return true
            }
            return false
        }
    }

    internal func execute(event: BaseEvent?) -> BaseEvent? {
        let plugins = lock.withLock { self.plugins }

        var result: BaseEvent? = event
        plugins.forEach { plugin in
            if var r = result {
                if let p = plugin as? DestinationPlugin {
                    _ = p.execute(event: r)
                } else if let p = plugin as? EventPlugin {
                    if let rr = result {
                        if let identifyEvent = rr as? IdentifyEvent {
                            result = p.identify(event: identifyEvent)
                        } else if let groupIdentifyEvent = rr as? GroupIdentifyEvent {
                            result = p.groupIdentify(event: groupIdentifyEvent)
                        } else if let revenueEvent = rr as? RevenueEvent {
                            result = p.revenue(event: revenueEvent)
                        } else {
                            result = p.track(event: rr)
                        }
                    }
                } else if let plugin = plugin as? Plugin {
                    result = plugin.execute(event: r)
                } else {
                    plugin.execute(&r)
                }
            }
        }
        return result
    }

    internal func plugin(name: String) -> UniversalPlugin? {
        return lock.withLock { pluginsByName[name] }
    }

    internal func applyClosure(_ closure: (UniversalPlugin) -> Void) {
        let plugins = lock.withLock { self.plugins }
        plugins.forEach { plugin in
            closure(plugin)
        }
    }
}
