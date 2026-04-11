import Foundation

@objc(AMPTrackingOptions)
public class ObjCTrackingOptions: NSObject {
    internal let options: TrackingOptions

    @objc
    convenience public override init() {
        self.init(TrackingOptions())
    }

    internal init(_ options: TrackingOptions) {
        self.options = options
    }

    @objc
    public func shouldTrackVersionName() -> Bool {
        options.shouldTrackVersionName()
    }

    @objc
    @discardableResult
    public func disableTrackVersionName() -> ObjCTrackingOptions {
        options.disableTrackVersionName()
        return self
    }

    @objc
    public func shouldTrackOsName() -> Bool {
        options.shouldTrackOsName()
    }

    @objc
    @discardableResult
    public func disableTrackOsName() -> ObjCTrackingOptions {
        options.disableTrackOsName()
        return self
    }

    @objc
    public func shouldTrackOsVersion() -> Bool {
        options.shouldTrackOsVersion()
    }

    @objc
    @discardableResult
    public func disableTrackOsVersion() -> ObjCTrackingOptions {
        options.disableTrackOsVersion()
        return self
    }

    @objc
    public func shouldTrackDeviceManufacturer() -> Bool {
        options.shouldTrackDeviceManufacturer()
    }

    @objc
    @discardableResult
    public func disableTrackDeviceManufacturer() -> ObjCTrackingOptions {
        options.disableTrackDeviceManufacturer()
        return self
    }

    @objc
    public func shouldTrackDeviceModel() -> Bool {
        options.shouldTrackDeviceModel()
    }

    @objc
    @discardableResult
    public func disableTrackDeviceModel() -> ObjCTrackingOptions {
        options.disableTrackDeviceModel()
        return self
    }

    @objc
    public func shouldTrackCarrier() -> Bool {
        options.shouldTrackCarrier()
    }

    @objc
    @discardableResult
    public func disableTrackCarrier() -> ObjCTrackingOptions {
        options.disableTrackCarrier()
        return self
    }

    @objc
    public func shouldTrackIpAddress() -> Bool {
        options.shouldTrackIpAddress()
    }

    @objc
    @discardableResult
    public func disableTrackIpAddress() -> ObjCTrackingOptions {
        options.disableTrackIpAddress()
        return self
    }

    @objc
    public func shouldTrackCountry() -> Bool {
        options.shouldTrackCountry()
    }

    @objc
    @discardableResult
    public func disableTrackCountry() -> ObjCTrackingOptions {
        options.disableTrackCountry()
        return self
    }

    @objc
    public func shouldTrackCity() -> Bool {
        options.shouldTrackCity()
    }

    @objc
    @discardableResult
    public func disableTrackCity() -> ObjCTrackingOptions {
        options.disableTrackCity()
        return self
    }

    @objc
    public func shouldTrackDMA() -> Bool {
        options.shouldTrackDMA()
    }

    @objc
    @discardableResult
    public func disableTrackDMA() -> ObjCTrackingOptions {
        options.disableTrackDMA()
        return self
    }

    @objc
    public func shouldTrackIDFV() -> Bool {
        options.shouldTrackIDFV()
    }

    @objc
    @discardableResult
    public func disableTrackIDFV() -> ObjCTrackingOptions {
        options.disableTrackIDFV()
        return self
    }

    @objc
    public func shouldTrackLanguage() -> Bool {
        options.shouldTrackLanguage()
    }

    @objc
    @discardableResult
    public func disableTrackLanguage() -> ObjCTrackingOptions {
        options.disableTrackLanguage()
        return self
    }

    @objc
    public func shouldTrackRegion() -> Bool {
        options.shouldTrackRegion()
    }

    @objc
    @discardableResult
    public func disableTrackRegion() -> ObjCTrackingOptions {
        options.disableTrackRegion()
        return self
    }

    @objc
    public func shouldTrackPlatform() -> Bool {
        options.shouldTrackPlatform()
    }

    @objc
    @discardableResult
    public func disableTrackPlatform() -> ObjCTrackingOptions {
        options.disableTrackPlatform()
        return self
    }
}
