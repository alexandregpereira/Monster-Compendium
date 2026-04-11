import Foundation

@objc(AMPPlugin)
public class ObjCPlugin: NSObject {
    internal let type: PluginType
    internal let setup: ((ObjCAmplitude) -> Void)?
    internal let execute: (ObjCBaseEvent) -> ObjCBaseEvent?
    internal let flush: (() -> Void)?

    @objc(initWithType:setup:execute:)
    public static func initWithType(
        type: PluginType,
        setup: @escaping (ObjCAmplitude) -> Void,
        execute: @escaping (ObjCBaseEvent) -> ObjCBaseEvent?
    ) -> ObjCPlugin {
        ObjCPlugin(type: type, setup: setup, execute: execute)
    }

    @objc(initWithType:execute:)
    public static func initWithType(
        type: PluginType,
        execute: @escaping (ObjCBaseEvent) -> ObjCBaseEvent?
    ) -> ObjCPlugin {
        ObjCPlugin(type: type, execute: execute)
    }

    @objc(initWithType:setup:execute:flush:)
    public static func initWithType(
        type: PluginType,
        setup: @escaping (ObjCAmplitude) -> Void,
        execute: @escaping (ObjCBaseEvent) -> ObjCBaseEvent?,
        flush: @escaping () -> Void
    ) -> ObjCPlugin {
        ObjCPlugin(type: type, setup: setup, execute: execute, flush: flush)
    }

    @objc(initWithType:execute:flush:)
    public static func initWithType(
        type: PluginType,
        execute: @escaping (ObjCBaseEvent) -> ObjCBaseEvent?,
        flush: @escaping () -> Void
    ) -> ObjCPlugin {
        ObjCPlugin(type: type, execute: execute, flush: flush)
    }

    @objc(initWithType:setup:execute:)
    public init(
        type: PluginType,
        setup: @escaping (ObjCAmplitude) -> Void,
        execute: @escaping (ObjCBaseEvent) -> ObjCBaseEvent?
    ) {
        self.type = type
        self.setup = setup
        self.execute = execute
        self.flush = nil
    }

    @objc(initWithType:execute:)
    public init(type: PluginType, execute: @escaping (ObjCBaseEvent) -> ObjCBaseEvent?) {
        self.type = type
        self.setup = nil
        self.execute = execute
        self.flush = nil
    }

    @objc(initWithType:setup:execute:flush:)
    public init(
        type: PluginType,
        setup: @escaping (ObjCAmplitude) -> Void,
        execute: @escaping (ObjCBaseEvent) -> ObjCBaseEvent?,
        flush: @escaping () -> Void
    ) {
        self.type = type
        self.setup = setup
        self.execute = execute
        self.flush = flush
    }

    @objc(initWithType:execute:flush:)
    public init(
        type: PluginType,
        execute: @escaping (ObjCBaseEvent) -> ObjCBaseEvent?,
        flush: @escaping () -> Void)
    {
        self.type = type
        self.setup = nil
        self.execute = execute
        self.flush = flush
    }
}

class ObjCPluginWrapper: Plugin, EventPlugin {
    weak var amplitude: ObjCAmplitude?
    let type: PluginType
    let wrapped: ObjCPlugin

    init(amplitude: ObjCAmplitude, wrapped: ObjCPlugin) {
        self.amplitude = amplitude
        self.type = wrapped.type
        self.wrapped = wrapped
    }

    func setup(amplitude: Amplitude) {
        guard let amplitude = self.amplitude else { return }
        wrapped.setup?(amplitude)
    }

    func execute(event: BaseEvent) -> BaseEvent? {
        wrapped.execute(ObjCBaseEvent(event: event))?.event
    }

    func track(event: BaseEvent) -> BaseEvent? {
        execute(event: event)
    }

    func identify(event: IdentifyEvent) -> IdentifyEvent? {
        execute(event: event) as? IdentifyEvent
    }

    func groupIdentify(event: GroupIdentifyEvent) -> GroupIdentifyEvent? {
        execute(event: event) as? GroupIdentifyEvent
    }

    func revenue(event: RevenueEvent) -> RevenueEvent? {
        execute(event: event) as? RevenueEvent
    }

    func flush() {
        wrapped.flush?()
    }
}
