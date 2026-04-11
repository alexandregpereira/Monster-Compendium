import Foundation

@objc(AMPProperties)
public class ObjCProperties: NSObject {
    internal let getter: (String) -> Any?
    internal let setter: (String, Any) -> Void
    internal let remover: (String) -> Void

    internal init(
        getter: @escaping (String) -> Any?,
        setter: @escaping (String, Any) -> Void,
        remover: @escaping (String) -> Void
    ) {
        self.getter = getter
        self.setter = setter
        self.remover = remover
    }

    @objc(get:)
    public func get(key: String) -> Any? {
        getter(key)
    }

    @objc(set:value:)
    @discardableResult
    public func set(key: String, value: Any) -> ObjCProperties {
        setter(key, value)
        return self
    }

    @objc(remove:)
    @discardableResult
    public func remove(key: String) -> ObjCProperties {
        remover(key)
        return self
    }
}
