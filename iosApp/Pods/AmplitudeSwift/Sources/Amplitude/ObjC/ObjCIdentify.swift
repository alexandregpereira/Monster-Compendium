import Foundation

@objc(AMPIdentify)
public class ObjCIdentify: NSObject {
    internal let identify = Identify()

    @objc(set:value:)
    @discardableResult
    public func set(property: String, value: Any?) -> ObjCIdentify {
        identify.set(property: property, value: value)
        return self
    }

    @objc(setOnce:value:)
    @discardableResult
    public func setOnce(property: String, value: Any?) -> ObjCIdentify {
        identify.setOnce(property: property, value: value)
        return self
    }

    @objc(prepend:value:)
    @discardableResult
    public func prepend(property: String, value: Any?) -> ObjCIdentify {
        identify.prepend(property: property, value: value)
        return self
    }

    @objc(append:value:)
    @discardableResult
    public func append(property: String, value: Any?) -> ObjCIdentify {
        identify.append(property: property, value: value)
        return self
    }

    @objc(postInsert:value:)
    @discardableResult
    public func postInsert(property: String, value: Any?) -> ObjCIdentify {
        identify.postInsert(property: property, value: value)
        return self
    }

    @objc(preInsert:value:)
    @discardableResult
    public func preInsert(property: String, value: Any?) -> ObjCIdentify {
        identify.preInsert(property: property, value: value)
        return self
    }

    @objc(remove:value:)
    @discardableResult
    public func remove(property: String, value: Any?) -> ObjCIdentify {
        identify.remove(property: property, value: value)
        return self
    }

    @objc(add:valueInt:)
    @discardableResult
    public func add(property: String, value: Int) -> ObjCIdentify {
        identify.add(property: property, value: value)
        return self
    }

    @objc(add:valueInt64:)
    @discardableResult
    public func add(property: String, value: Int64) -> ObjCIdentify {
        identify.add(property: property, value: value)
        return self
    }

    @objc(add:valueDouble:)
    @discardableResult
    public func add(property: String, value: Double) -> ObjCIdentify {
        identify.add(property: property, value: value)
        return self
    }

    @objc(add:valueFloat:)
    @discardableResult
    public func add(property: String, value: Float) -> ObjCIdentify {
        identify.add(property: property, value: value)
        return self
    }

    @objc(unset:)
    @discardableResult
    public func unset(property: String) -> ObjCIdentify {
        identify.unset(property: property)
        return self
    }

    @objc
    @discardableResult
    public func clearAll() -> ObjCIdentify {
        identify.clearAll()
        return self
    }
}
