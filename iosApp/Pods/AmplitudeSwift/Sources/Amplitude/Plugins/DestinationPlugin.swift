//
//  DestinationPlugin.swift
//
//
//  Created by Hao Yu on 11/15/22.
//

open class DestinationPlugin: BasePlugin, EventPlugin {
    public let type: PluginType = .destination
    private let timeline = Timeline()

    open func track(event: BaseEvent) -> BaseEvent? {
        return event
    }

    open func identify(event: IdentifyEvent) -> IdentifyEvent? {
        return event
    }

    open func groupIdentify(event: GroupIdentifyEvent) -> GroupIdentifyEvent? {
        return event
    }

    open func revenue(event: RevenueEvent) -> RevenueEvent? {
        return event
    }

    open func flush() {
    }

    open override func execute(event: BaseEvent) -> BaseEvent? {
        // Skip this destination if it is disabled via settings
        if !enabled {
            return nil
        }
        let beforeResult = timeline.applyPlugin(pluginType: .before, event: event)
        let enrichmentResult = timeline.applyPlugin(pluginType: .enrichment, event: beforeResult)
        var destinationResult: BaseEvent?
        switch enrichmentResult {
        case let e as IdentifyEvent:
            destinationResult = identify(event: e)
        case let e as GroupIdentifyEvent:
            destinationResult = track(event: e)
        case let e as RevenueEvent:
            destinationResult = revenue(event: e)
        default:
            destinationResult = track(event: event)
        }
        return destinationResult
    }
}

extension DestinationPlugin {
    var enabled: Bool {
        return true
    }

    var logger: (any Logger)? {
        return self.amplitude?.logger
    }

    @discardableResult
    func add(plugin: Plugin) -> Plugin {
        plugin.setup(amplitude: amplitude!)
        timeline.add(plugin: plugin)
        return plugin
    }

    func remove(plugin: Plugin) {
        timeline.remove(plugin: plugin)
    }

    public func apply(closure: (Plugin) -> Void) {
        timeline.apply { plugin in
            if let plugin = plugin as? Plugin {
                closure(plugin)
            }
        }
    }
}
