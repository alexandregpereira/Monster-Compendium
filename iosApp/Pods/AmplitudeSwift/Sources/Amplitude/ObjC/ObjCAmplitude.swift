import Foundation

@objc(Amplitude)
public class ObjCAmplitude: NSObject {
    private let amplitude: Amplitude
    private var plugins: [ObjCPluginWrapper] = []

    @objc(initWithConfiguration:)
    public static func initWithConfiguration(
        configuration: ObjCConfiguration
    ) -> ObjCAmplitude {
        ObjCAmplitude(configuration: configuration)
    }

    @objc(initWithConfiguration:)
    public init(
        configuration: ObjCConfiguration
    ) {
        amplitude = Amplitude(configuration: configuration.configuration)
    }

    @objc
    public var configuration: ObjCConfiguration {
        ObjCConfiguration(configuration: amplitude.configuration)
    }

    @objc
    public var storage: ObjCStorage {
        ObjCStorage(amplitude: amplitude)
    }

    @objc(track:)
    @discardableResult
    public func track(event: ObjCBaseEvent) -> ObjCAmplitude {
        amplitude.track(event: event.event)
        return self
    }

    @objc(track:callback:)
    @discardableResult
    public func track(event: ObjCBaseEvent, callback: ObjCEventCallback?) -> ObjCAmplitude {
        amplitude.track(event: event.event, callback: callback == nil ? nil : { (event, code, message) in
            callback!(ObjCBaseEvent(event: event), code, message)
        })
        return self
    }

    @objc(track:options:)
    @discardableResult
    public func track(event: ObjCBaseEvent, options: ObjCEventOptions?) -> ObjCAmplitude {
        amplitude.track(event: event.event, options: options?.options)
        return self
    }

    @objc(track:options:callback:)
    @discardableResult
    public func track(event: ObjCBaseEvent, options: ObjCEventOptions?, callback: ObjCEventCallback?) -> ObjCAmplitude {
        amplitude.track(event: event.event, options: options?.options, callback: callback == nil ? nil : { (event, code, message) in
            callback!(ObjCBaseEvent(event: event), code, message)
        })
        return self
    }

    @objc(track:eventProperties:)
    @discardableResult
    public func track(eventType: String, eventProperties: [String: Any]?) -> ObjCAmplitude {
        amplitude.track(eventType: eventType, eventProperties: eventProperties)
        return self
    }

    @objc(track:eventProperties:options:)
    @discardableResult
    public func track(eventType: String, eventProperties: [String: Any]?, options: ObjCEventOptions?) -> ObjCAmplitude {
        amplitude.track(eventType: eventType, eventProperties: eventProperties, options: options?.options)
        return self
    }

    @objc(identify:)
    @discardableResult
    public func identify(identify: ObjCIdentify) -> ObjCAmplitude {
        amplitude.identify(identify: identify.identify)
        return self
    }

    @objc(identify:options:)
    @discardableResult
    public func identify(identify: ObjCIdentify, options: ObjCEventOptions?) -> ObjCAmplitude {
        amplitude.identify(identify: identify.identify, options: options?.options)
        return self
    }

    @objc(groupIdentify:groupName:identify:)
    @discardableResult
    public func groupIdentify(groupType: String, groupName: String, identify: ObjCIdentify) -> ObjCAmplitude {
        amplitude.groupIdentify(groupType: groupType, groupName: groupName, identify: identify.identify)
        return self
    }

    @objc(groupIdentify:groupName:identify:options:)
    @discardableResult
    public func groupIdentify(groupType: String, groupName: String, identify: ObjCIdentify, options: ObjCEventOptions?) -> ObjCAmplitude {
        amplitude.groupIdentify(groupType: groupType, groupName: groupName, identify: identify.identify, options: options?.options)
        return self
    }

    @objc(setGroup:groupName:)
    @discardableResult
    public func setGroup(groupType: String, groupName: String) -> ObjCAmplitude {
        amplitude.setGroup(groupType: groupType, groupName: groupName)
        return self
    }

    @objc(setGroup:groupName:options:)
    @discardableResult
    public func setGroup(groupType: String, groupName: String, options: ObjCEventOptions?) -> ObjCAmplitude {
        amplitude.setGroup(groupType: groupType, groupName: groupName, options: options?.options)
        return self
    }

    @objc(setGroup:groupNames:)
    @discardableResult
    public func setGroup(groupType: String, groupNames: [String]) -> ObjCAmplitude {
        amplitude.setGroup(groupType: groupType, groupName: groupNames)
        return self
    }

    @objc(setGroup:groupNames:options:)
    @discardableResult
    public func setGroup(groupType: String, groupNames: [String], options: ObjCEventOptions?) -> ObjCAmplitude {
        amplitude.setGroup(groupType: groupType, groupName: groupNames, options: options?.options)
        return self
    }

    @objc(revenue:)
    @discardableResult
    public func revenue(revenue: ObjCRevenue) -> ObjCAmplitude {
        amplitude.revenue(revenue: revenue.instance)
        return self
    }

    @objc(revenue:options:)
    @discardableResult
    public func revenue(revenue: ObjCRevenue, options: ObjCEventOptions? = nil) -> ObjCAmplitude {
        amplitude.revenue(revenue: revenue.instance, options: options?.options)
        return self
    }

    @objc(add:)
    @discardableResult
    public func add(plugin: AnyObject) -> ObjCAmplitude {
        switch plugin {
        case let swiftPlugin as UniversalPlugin:
            amplitude.add(plugin: swiftPlugin)
        case let objcPlugin as ObjCPlugin:
            let wrapper = ObjCPluginWrapper(amplitude: self, wrapped: objcPlugin)
            plugins.append(wrapper)
            amplitude.add(plugin: wrapper)
        default:
            fatalError("Attempted to add a plugin that is not an instance of Plugin or ObjCPlugin")
        }
        return self
    }

    @objc(remove:)
    @discardableResult
    public func remove(plugin: ObjCPlugin) -> ObjCAmplitude {
        guard let pluginIndex = plugins.firstIndex(where: { wrapper in wrapper.wrapped == plugin }) else { return self }
        let wrapper = plugins[pluginIndex]
        plugins.remove(at: pluginIndex)
        amplitude.remove(plugin: wrapper)
        return self
    }

    @objc
    @discardableResult
    public func flush() -> ObjCAmplitude {
        amplitude.flush()
        return self
    }

    @objc(setUserId:)
    @discardableResult
    public func setUserId(userId: String?) -> ObjCAmplitude {
        amplitude.setUserId(userId: userId)
        return self
    }

    @objc(setDeviceId:)
    @discardableResult
    public func setDeviceId(deviceId: String?) -> ObjCAmplitude {
        amplitude.setDeviceId(deviceId: deviceId)
        return self
    }

    @objc
    public func getUserId() -> String? {
        amplitude.getUserId()
    }

    @objc
    public func getDeviceId() -> String? {
        amplitude.getDeviceId()
    }

    @objc
    public func getSessionId() -> Int64 {
        amplitude.getSessionId()
    }

    @objc(setSessionIdWithTimestamp:)
    @discardableResult
    public func setSessionId(timestamp: Int64) -> ObjCAmplitude {
        amplitude.setSessionId(timestamp: timestamp)
        return self
    }

    @objc(setSessionIdWithDate:)
    @discardableResult
    public func setSessionId(date: Date) -> ObjCAmplitude {
        amplitude.setSessionId(date: date)
        return self
    }

    @objc
    @discardableResult
    public func reset() -> ObjCAmplitude {
        amplitude.reset()
        return self
    }

    @objc
    var optOut: Bool {
        get {
            return amplitude.optOut
        }
        set {
            amplitude.optOut = newValue
        }
    }
}

extension ObjCAmplitude: PluginHost {

    public func plugin(name: String) -> (any UniversalPlugin)? {
        return amplitude.plugin(name: name)
    }

    public func plugins<PluginType: UniversalPlugin>(type: PluginType.Type) -> [PluginType] {
        return amplitude.plugins(type: type)
    }
}
