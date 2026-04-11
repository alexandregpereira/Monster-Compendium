//
//  Revenue.swift
//
//
//  Created by Marvin Liu on 12/8/22.
//

import Foundation

public class Revenue {
    enum Property: String {
        case REVENUE_PRODUCT_ID = "$productId"
        case REVENUE_QUANTITY = "$quantity"
        case REVENUE_PRICE = "$price"
        case REVENUE_TYPE = "$revenueType"
        case REVENUE_CURRENCY = "$currency"
        case REVENUE_RECEIPT = "$receipt"
        case REVENUE_RECEIPT_SIG = "$receiptSig"
        case REVENUE = "$revenue"
    }

    public init() {}

    private var _productId: String?
    public var productId: String? {
        get {
            return _productId
        }
        set(value) {
            if value != nil && !value!.isEmpty {
                _productId = value
            }
        }
    }

    private var _quantity: Int = 1
    public var quantity: Int {
        get {
            return _quantity
        }
        set(value) {
            if value > 0 {
                _quantity = value
            }
        }
    }

    private var _price: Double?
    public var price: Double? {
        get {
            return _price
        }
        set(value) {
            if value != nil {
                _price = value
            }
        }
    }

    private var _revenue: Double?
    public var revenue: Double? {
        get {
            return _revenue
        }
        set(value) {
            if value != nil {
                _revenue = value
            }
        }
    }

    public var revenueType: String?

    public var currency: String?

    public var receipt: String?

    public var receiptSig: String?

    public var properties: [String: Any]?

    @discardableResult
    public func setReceipt(receipt: String, receiptSignature: String) -> Revenue {
        self.receipt = receipt
        self.receiptSig = receiptSignature
        return self
    }

    func isValid() -> Bool {
        return price != nil
    }

    func toRevenueEvent() -> RevenueEvent {
        let event = RevenueEvent()
        var eventProperties = properties ?? [String: Any]()
        if productId != nil {
            eventProperties[Property.REVENUE_PRODUCT_ID.rawValue] = productId
        }
        eventProperties[Property.REVENUE_QUANTITY.rawValue] = quantity
        if price != nil {
            eventProperties[Property.REVENUE_PRICE.rawValue] = price
        }
        if revenueType != nil {
            eventProperties[Property.REVENUE_TYPE.rawValue] = revenueType
        }
        if currency != nil {
            eventProperties[Property.REVENUE_CURRENCY.rawValue] = currency
        }
        if receipt != nil {
            eventProperties[Property.REVENUE_RECEIPT.rawValue] = receipt
        }
        if receiptSig != nil {
            eventProperties[Property.REVENUE_RECEIPT_SIG.rawValue] = receiptSig
        }
        if revenue != nil {
            eventProperties[Property.REVENUE.rawValue] = revenue
        }
        event.eventProperties = eventProperties
        return event
    }
}
