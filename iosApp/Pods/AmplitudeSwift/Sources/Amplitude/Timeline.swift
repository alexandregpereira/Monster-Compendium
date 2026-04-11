//
//  Timeline.swift
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

public class Timeline {
    internal let plugins: [PluginType: Mediator]
    private let addLock = NSLock()

    init() {
        self.plugins = [
            PluginType.before: Mediator(),
            PluginType.enrichment: Mediator(),
            PluginType.destination: Mediator(),
            PluginType.utility: Mediator(),
            PluginType.observe: Mediator(),
        ]
    }

    func processEvent(event: BaseEvent) {
        let beforeResult = self.applyPlugin(pluginType: PluginType.before, event: event)
        let enrichmentResult = self.applyPlugin(pluginType: PluginType.enrichment, event: beforeResult)
        _ = self.applyPlugin(pluginType: PluginType.destination, event: enrichmentResult)
    }

    internal func applyPlugin(pluginType: PluginType, event: BaseEvent?) -> BaseEvent? {
        var result: BaseEvent? = event
        if let mediator = plugins[pluginType] {
            result = mediator.execute(event: event)
        }
        return result
    }

    internal func add(plugin: UniversalPlugin) {
        addLock.withLock {
            guard plugin.name.flatMap({ self.plugin(name: $0) }) == nil else {
                return
            }
            let pluginType: PluginType
            switch plugin {
            case let plugin as Plugin:
                pluginType = plugin.type
            default:
                pluginType = .enrichment
            }
            if let mediator = plugins[pluginType] {
                mediator.add(plugin: plugin)
            }
        }
    }

    internal func remove(plugin: UniversalPlugin) {
        // remove all plugins with this name in every category
        for mediator in plugins.values {
            mediator.remove(plugin: plugin)
        }
    }

    internal func apply(_ closure: (UniversalPlugin) -> Void) {
        for type in PluginType.allCases {
            plugins[type]?.applyClosure { plugin in
                closure(plugin)
                if let destPlugin = plugin as? DestinationPlugin {
                    destPlugin.apply(closure: closure)
                }
            }
        }
    }

    func plugin(name: String) -> UniversalPlugin? {
        return PluginType.allCases
            .compactMap { self.plugins[$0]?.plugin(name: name) }
            .first
    }
}
