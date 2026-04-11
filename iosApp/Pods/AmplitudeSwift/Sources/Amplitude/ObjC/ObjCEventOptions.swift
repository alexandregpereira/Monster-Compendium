import Foundation

public typealias ObjCEventCallback = (ObjCBaseEvent, Int, String) -> Void

@objc(AMPEventOptions)
public class ObjCEventOptions: NSObject {
    internal let _options = EventOptions()

    internal var options: EventOptions {
        _options
    }

    @objc
    public var userId: String? {
        get {
            options.userId
        }
        set(value) {
            options.userId = value
        }
    }

    @objc
    public var deviceId: String? {
        get {
            options.deviceId
        }
        set(value) {
            options.deviceId = value
        }
    }

    @objc
    public var timestamp: Int64 {
        get {
            options.timestamp ?? -1
        }
        set(value) {
            options.timestamp = value
        }
    }

    @objc
    public var eventId: Int64 {
        get {
            options.eventId ?? -1
        }
        set(value) {
            options.eventId = value
        }
    }

    @objc
    public var sessionId: Int64 {
        get {
            options.sessionId ?? -1
        }
        set(value) {
            options.sessionId = value
        }
    }

    @objc
    public var insertId: String? {
        get {
            options.insertId
        }
        set(value) {
            options.insertId = value
        }
    }

    @objc
    public var locationLat: Double {
        get {
            options.locationLat ?? Double.nan
        }
        set(value) {
            options.locationLat = value.isNaN ? nil : value
        }
    }

    @objc
    public var locationLng: Double {
        get {
            options.locationLng ?? Double.nan
        }
        set(value) {
            options.locationLng = value.isNaN ? nil : value
        }
    }

    @objc
    public var appVersion: String? {
        get {
            options.appVersion
        }
        set(value) {
            options.appVersion = value
        }
    }

    @objc
    public var versionName: String? {
        get {
            options.versionName
        }
        set(value) {
            options.versionName = value
        }
    }

    @objc
    public var platform: String? {
        get {
            options.platform
        }
        set(value) {
            options.platform = value
        }
    }

    @objc
    public var osName: String? {
        get {
            options.osName
        }
        set(value) {
            options.osName = value
        }
    }

    @objc
    public var osVersion: String? {
        get {
            options.osVersion
        }
        set(value) {
            options.osVersion = value
        }
    }

    @objc
    public var deviceBrand: String? {
        get {
            options.deviceBrand
        }
        set(value) {
            options.deviceBrand = value
        }
    }

    @objc
    public var deviceManufacturer: String? {
        get {
            options.deviceManufacturer
        }
        set(value) {
            options.deviceManufacturer = value
        }
    }

    @objc
    public var deviceModel: String? {
        get {
            options.deviceModel
        }
        set(value) {
            options.deviceModel = value
        }
    }

    @objc
    public var carrier: String? {
        get {
            options.carrier
        }
        set(value) {
            options.carrier = value
        }
    }

    @objc
    public var country: String? {
        get {
            options.country
        }
        set(value) {
            options.country = value
        }
    }

    @objc
    public var region: String? {
        get {
            options.region
        }
        set(value) {
            options.region = value
        }
    }

    @objc
    public var city: String? {
        get {
            options.city
        }
        set(value) {
            options.city = value
        }
    }

    @objc
    public var dma: String? {
        get {
            options.dma
        }
        set(value) {
            options.dma = value
        }
    }

    @objc
    public var idfa: String? {
        get {
            options.idfa
        }
        set(value) {
            options.idfa = value
        }
    }

    @objc
    public var idfv: String? {
        get {
            options.idfv
        }
        set(value) {
            options.idfv = value
        }
    }

    @objc
    public var adid: String? {
        get {
            options.adid
        }
        set(value) {
            options.adid = value
        }
    }

    @objc
    public var language: String? {
        get {
            options.language
        }
        set(value) {
            options.language = value
        }
    }

    @objc
    public var library: String? {
        get {
            options.library
        }
        set(value) {
            options.library = value
        }
    }

    @objc
    public var ip: String? {
        get {
            options.ip
        }
        set(value) {
            options.ip = value
        }
    }

    @objc
    public var plan: ObjCPlan? {
        get {
            guard let plan = options.plan else { return nil }
            return ObjCPlan(plan)
        }
        set(value) {
            options.plan = value?.plan
        }
    }

    @objc
    public var ingestionMetadata: ObjCIngestionMetadata? {
        get {
            guard let ingestionMetadata = options.ingestionMetadata else { return nil }
            return ObjCIngestionMetadata(ingestionMetadata)
        }
        set(value) {
            options.ingestionMetadata = value?.ingestionMetadata
        }
    }

    @objc
    public var revenue: Double {
        get {
            options.revenue ?? Double.nan
        }
        set(value) {
            options.revenue = value.isNaN ? nil : value
        }
    }

    @objc
    public var price: Double {
        get {
            options.price ?? Double.nan
        }
        set(value) {
            options.price = value.isNaN ? nil : value
        }
    }

    @objc
    public var quantity: Int {
        get {
            options.quantity ?? -1
        }
        set(value) {
            options.quantity = value
        }
    }

    @objc
    public var productId: String? {
        get {
            options.productId
        }
        set(value) {
            options.productId = value
        }
    }

    @objc
    public var revenueType: String? {
        get {
            options.revenueType
        }
        set(value) {
            options.revenueType = value
        }
    }

    @objc
    public var currency: String? {
        get {
            options.currency
        }
        set(value) {
            options.currency = value
        }
    }

    @objc
    public var extra: ObjCProperties {
        ObjCProperties(getter: { key in
            guard let extra = self.options.extra else { return nil }
            return extra[key]
        }, setter: { (key, value) in
            if self.options.extra == nil {
                self.options.extra = [:]
            }
            self.options.extra![key] = value
        }, remover: { key in
            self.options.extra?.removeValue(forKey: key)
        })
    }

    @objc
    public var callback: ObjCEventCallback? {
        get {
            guard let callback = options.callback else { return nil }
            return { (event, code, message) in callback(event.event, code, message)  }
        }
        set(value) {
            if let value = value {
                options.callback = { (event, code, message) in
                    value(ObjCBaseEvent(event: event), code, message)
                }
            } else {
                options.callback = nil
            }
        }
    }

    @objc
    public var partnerId: String? {
        get {
            options.partnerId
        }
        set(value) {
            options.partnerId = value
        }
    }

    @objc(mergeEventOptions:)
    public func mergeEventOptions(other: ObjCEventOptions) {
        options.mergeEventOptions(eventOptions: other.options)
    }
}
