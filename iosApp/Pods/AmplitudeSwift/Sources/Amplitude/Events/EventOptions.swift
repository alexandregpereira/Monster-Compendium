//
//  EventOptions.swift
//
//
//  Created by Marvin Liu on 11/3/22.
//

import Foundation

open class EventOptions {
    public var userId: String?
    public var deviceId: String?
    public var timestamp: Int64?
    public var eventId: Int64?
    public var sessionId: Int64?
    public var insertId: String?
    public var locationLat: Double?
    public var locationLng: Double?
    public var appVersion: String?
    public var versionName: String?
    public var platform: String?
    public var osName: String?
    public var osVersion: String?
    public var deviceBrand: String?
    public var deviceManufacturer: String?
    public var deviceModel: String?
    public var carrier: String?
    public var country: String?
    public var region: String?
    public var city: String?
    public var dma: String?
    public var idfa: String?
    public var idfv: String?
    public var adid: String?
    public var language: String?
    public var library: String?
    public var ip: String?
    public var plan: Plan?
    public var ingestionMetadata: IngestionMetadata?
    public var revenue: Double?
    public var price: Double?
    public var quantity: Int?
    public var productId: String?
    public var revenueType: String?
    public var currency: String?
    public var extra: [String: Any]?
    public var callback: EventCallback?
    public var partnerId: String?
    internal var attempts: Int

    public init(
        userId: String? = nil,
        deviceId: String? = nil,
        timestamp: Int64? = nil,
        eventId: Int64? = nil,
        sessionId: Int64? = nil,
        insertId: String? = nil,
        locationLat: Double? = nil,
        locationLng: Double? = nil,
        appVersion: String? = nil,
        versionName: String? = nil,
        platform: String? = nil,
        osName: String? = nil,
        osVersion: String? = nil,
        deviceBrand: String? = nil,
        deviceManufacturer: String? = nil,
        deviceModel: String? = nil,
        carrier: String? = nil,
        country: String? = nil,
        region: String? = nil,
        city: String? = nil,
        dma: String? = nil,
        idfa: String? = nil,
        idfv: String? = nil,
        adid: String? = nil,
        language: String? = nil,
        library: String? = nil,
        ip: String? = nil,
        plan: Plan? = nil,
        ingestionMetadata: IngestionMetadata? = nil,
        revenue: Double? = nil,
        price: Double? = nil,
        quantity: Int? = nil,
        productId: String? = nil,
        revenueType: String? = nil,
        currency: String? = nil,
        extra: [String: Any]? = nil,
        callback: EventCallback? = nil,
        partnerId: String? = nil,
        attempts: Int = 0
    ) {
        self.userId = userId
        self.deviceId = deviceId
        self.timestamp = timestamp
        self.eventId = eventId
        self.sessionId = sessionId
        self.insertId = insertId
        self.locationLat = locationLat
        self.locationLng = locationLng
        self.appVersion = appVersion
        self.versionName = versionName
        self.platform = platform
        self.osName = osName
        self.osVersion = osVersion
        self.deviceBrand = deviceBrand
        self.deviceManufacturer = deviceManufacturer
        self.deviceModel = deviceModel
        self.carrier = carrier
        self.country = country
        self.region = region
        self.city = city
        self.dma = dma
        self.idfa = idfa
        self.idfv = idfv
        self.adid = adid
        self.language = language
        self.library = library
        self.ip = ip
        self.plan = plan
        self.ingestionMetadata = ingestionMetadata
        self.revenue = revenue
        self.price = price
        self.quantity = quantity
        self.productId = productId
        self.revenueType = revenueType
        self.currency = currency
        self.extra = extra
        self.callback = callback
        self.partnerId = partnerId
        self.attempts = attempts
    }

    public func mergeEventOptions(eventOptions: EventOptions) {
        userId = eventOptions.userId ?? userId
        deviceId = eventOptions.deviceId ?? deviceId
        timestamp = eventOptions.timestamp ?? timestamp
        eventId = eventOptions.eventId ?? eventId
        sessionId = eventOptions.sessionId ?? sessionId
        insertId = eventOptions.insertId ?? insertId
        locationLat = eventOptions.locationLat ?? locationLat
        locationLng = eventOptions.locationLng ?? locationLng
        appVersion = eventOptions.appVersion ?? appVersion
        versionName = eventOptions.versionName ?? versionName
        platform = eventOptions.platform ?? platform
        osName = eventOptions.osName ?? osName
        osVersion = eventOptions.osVersion ?? osVersion
        deviceBrand = eventOptions.deviceBrand ?? deviceBrand
        deviceManufacturer = eventOptions.deviceManufacturer ?? deviceManufacturer
        deviceModel = eventOptions.deviceModel ?? deviceModel
        carrier = eventOptions.carrier ?? carrier
        country = eventOptions.country ?? country
        region = eventOptions.region ?? region
        city = eventOptions.city ?? city
        dma = eventOptions.dma ?? dma
        idfa = eventOptions.idfa ?? idfa
        idfv = eventOptions.idfv ?? idfv
        adid = eventOptions.adid ?? adid
        language = eventOptions.language ?? language
        library = eventOptions.library ?? library
        ip = eventOptions.ip ?? ip
        plan = eventOptions.plan ?? plan
        ingestionMetadata = eventOptions.ingestionMetadata ?? ingestionMetadata
        revenue = eventOptions.revenue ?? revenue
        price = eventOptions.price ?? price
        quantity = eventOptions.quantity ?? quantity
        productId = eventOptions.productId ?? productId
        revenueType = eventOptions.revenueType ?? revenueType
        currency = eventOptions.currency ?? currency
        extra = eventOptions.extra ?? extra
        callback = eventOptions.callback ?? callback
        partnerId = eventOptions.partnerId ?? partnerId
    }
}
