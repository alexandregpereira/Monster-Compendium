import Foundation

@objc(AMPRevenue)
public class ObjCRevenue: NSObject {
    internal let instance = Revenue()

    @objc
    public var productId: String? {
        get {
            instance.productId
        }
        set(value) {
            instance.productId = value
        }
    }

    @objc
    public var quantity: Int {
        get {
            instance.quantity
        }
        set(value) {
            instance.quantity = value
        }
    }

    @objc
    public var price: Double {
        get {
            instance.price ?? Double.nan
        }
        set(value) {
            instance.price = value.isNaN ? nil : value
        }
    }

    @objc
    public var revenue: Double {
        get {
            instance.revenue ?? Double.nan
        }
        set(value) {
            instance.revenue = value.isNaN ? nil : value
        }
    }

    @objc
    public var revenueType: String? {
        get {
            instance.revenueType
        }
        set(value) {
            instance.revenueType = value
        }
    }

    @objc
    public var currency: String? {
        get {
            instance.currency
        }
        set(value) {
            instance.currency = value
        }
    }

    @objc
    public var receipt: String? {
        get {
            instance.receipt
        }
        set(value) {
            instance.receipt = value
        }
    }

    @objc
    public var receiptSig: String? {
        get {
            instance.receiptSig
        }
        set(value) {
            instance.receiptSig = value
        }
    }

    @objc
    public var properties: ObjCProperties {
        ObjCProperties(getter: { key in
            guard let properties = self.instance.properties else { return nil }
            return properties[key] ?? nil
        }, setter: { (key, value) in
            if self.instance.properties == nil {
                self.instance.properties = [:]
            }
            self.instance.properties![key] = value
        }, remover: { key in
            self.instance.properties?.removeValue(forKey: key)
        })
    }

    @objc(setReceipt:receiptSignature:)
    @discardableResult
    public func setReceipt(receipt: String, receiptSignature: String) -> ObjCRevenue {
        instance.setReceipt(receipt: receipt, receiptSignature: receiptSignature)
        return self
    }
}
