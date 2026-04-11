//
//  BaseEvent.swift
//
//
//  Created by Marvin Liu on 11/3/22.
//

#if AMPLITUDE_DISABLE_UIKIT
import AmplitudeCoreNoUIKit
#else
import AmplitudeCore
#endif
import Foundation

open class BaseEvent: EventOptions, AnalyticsEvent, Codable {
    public var eventType: String
    public var eventProperties: [String: Any]?
    public var userProperties: [String: Any]?
    public var groups: [String: Any]?
    public var groupProperties: [String: Any]?

    enum CodingKeys: String, CodingKey {
        case eventType = "event_type"
        case eventProperties = "event_properties"
        case userProperties = "user_properties"
        case groups
        case groupProperties = "group_properties"
        case userId = "user_id"
        case deviceId = "device_id"
        case timestamp = "time"
        case eventId = "event_id"
        case sessionId = "session_id"
        case insertId = "insert_id"
        case locationLat = "location_lat"
        case locationLng = "location_lng"
        case appVersion = "app_version"
        case versionName = "version_name"
        case platform
        case osName = "os_name"
        case osVersion = "os_version"
        case deviceBrand = "device_brand"
        case deviceManufacturer = "device_manufacturer"
        case deviceModel = "device_model"
        case carrier
        case country
        case region
        case city
        case dma
        case idfa
        case idfv
        case adid
        case language
        case library
        case ip
        case plan
        case ingestionMetadata = "ingestion_metadata"
        case revenue
        case price
        case quantity
        case productId = "product_id"
        case revenueType = "revenue_type"
        case currency = "currency"
        case partnerId = "partner_id"
    }

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
        eventType: String,
        eventProperties: [String: Any]? = nil,
        userProperties: [String: Any]? = nil,
        groups: [String: Any]? = nil,
        groupProperties: [String: Any]? = nil
    ) {
        self.eventType = eventType
        self.eventProperties = eventProperties
        self.userProperties = userProperties
        self.groups = groups
        self.groupProperties = groupProperties
        super.init(
            userId: userId,
            deviceId: deviceId,
            timestamp: timestamp,
            eventId: eventId,
            sessionId: sessionId,
            insertId: insertId,
            locationLat: locationLat,
            locationLng: locationLng,
            appVersion: appVersion,
            versionName: versionName,
            platform: platform,
            osName: osName,
            osVersion: osVersion,
            deviceBrand: deviceBrand,
            deviceManufacturer: deviceManufacturer,
            deviceModel: deviceModel,
            carrier: carrier,
            country: country,
            region: region,
            city: city,
            dma: dma,
            idfa: idfa,
            idfv: idfv,
            adid: adid,
            language: language,
            library: library,
            ip: ip,
            plan: plan,
            ingestionMetadata: ingestionMetadata,
            revenue: revenue,
            price: price,
            quantity: quantity,
            productId: productId,
            revenueType: revenueType,
            currency: currency,
            extra: extra,
            callback: callback,
            partnerId: partnerId
        )
    }

    func isValid() -> Bool {
        return userId != nil || deviceId != nil
    }

    required public init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: CodingKeys.self)
        eventType = try values.decode(String.self, forKey: .eventType)
        eventProperties = try values.decodeIfPresent([String: Any].self, forKey: .eventProperties)
        userProperties = try values.decodeIfPresent([String: Any].self, forKey: .userProperties)
        groups = try values.decodeIfPresent([String: Any].self, forKey: .groups)
        groupProperties = try values.decodeIfPresent([String: Any].self, forKey: .groupProperties)
        super.init()
        userId = try values.decodeIfPresent(String.self, forKey: .userId)
        deviceId = try values.decodeIfPresent(String.self, forKey: .deviceId)
        timestamp = try values.decodeIfPresent(Int64.self, forKey: .timestamp)
        eventId = try values.decodeIfPresent(Int64.self, forKey: .eventId)
        sessionId = try values.decodeIfPresent(Int64.self, forKey: .sessionId)
        insertId = try values.decodeIfPresent(String.self, forKey: .insertId)
        locationLat = try values.decodeIfPresent(Double.self, forKey: .locationLat)
        locationLng = try values.decodeIfPresent(Double.self, forKey: .locationLng)
        appVersion = try values.decodeIfPresent(String.self, forKey: .appVersion)
        versionName = try values.decodeIfPresent(String.self, forKey: .versionName)
        platform = try values.decodeIfPresent(String.self, forKey: .platform)
        osName = try values.decodeIfPresent(String.self, forKey: .osName)
        osVersion = try values.decodeIfPresent(String.self, forKey: .osVersion)
        deviceBrand = try values.decodeIfPresent(String.self, forKey: .deviceBrand)
        deviceManufacturer = try values.decodeIfPresent(String.self, forKey: .deviceManufacturer)
        deviceModel = try values.decodeIfPresent(String.self, forKey: .deviceModel)
        carrier = try values.decodeIfPresent(String.self, forKey: .carrier)
        country = try values.decodeIfPresent(String.self, forKey: .country)
        region = try values.decodeIfPresent(String.self, forKey: .region)
        city = try values.decodeIfPresent(String.self, forKey: .city)
        dma = try values.decodeIfPresent(String.self, forKey: .dma)
        idfa = try values.decodeIfPresent(String.self, forKey: .idfa)
        idfv = try values.decodeIfPresent(String.self, forKey: .idfv)
        adid = try values.decodeIfPresent(String.self, forKey: .adid)
        language = try values.decodeIfPresent(String.self, forKey: .language)
        library = try values.decodeIfPresent(String.self, forKey: .library)
        ip = try values.decodeIfPresent(String.self, forKey: .ip)
        plan = try values.decodeIfPresent(Plan.self, forKey: .plan)
        ingestionMetadata = try values.decodeIfPresent(IngestionMetadata.self, forKey: .ingestionMetadata)
        revenue = try values.decodeIfPresent(Double.self, forKey: .revenue)
        price = try values.decodeIfPresent(Double.self, forKey: .price)
        quantity = try values.decodeIfPresent(Int.self, forKey: .quantity)
        productId = try values.decodeIfPresent(String.self, forKey: .productId)
        revenueType = try values.decodeIfPresent(String.self, forKey: .revenueType)
        currency = try values.decodeIfPresent(String.self, forKey: .currency)
        partnerId = try values.decodeIfPresent(String.self, forKey: .partnerId)
    }

    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(eventType, forKey: .eventType)
        try container.encodeAny(eventProperties, forKey: .eventProperties)
        try container.encodeAny(userProperties, forKey: .userProperties)
        try container.encodeAny(groups, forKey: .groups)
        try container.encodeAny(groupProperties, forKey: .groupProperties)
        try container.encode(userId, forKey: .userId)
        try container.encode(deviceId, forKey: .deviceId)
        try container.encode(timestamp, forKey: .timestamp)
        try container.encode(eventId, forKey: .eventId)
        try container.encode(sessionId, forKey: .sessionId)
        try container.encode(insertId, forKey: .insertId)
        try container.encode(locationLat, forKey: .locationLat)
        try container.encode(locationLng, forKey: .locationLng)
        try container.encode(appVersion, forKey: .appVersion)
        try container.encode(versionName, forKey: .versionName)
        try container.encode(platform, forKey: .platform)
        try container.encode(osName, forKey: .osName)
        try container.encode(osVersion, forKey: .osVersion)
        try container.encode(deviceBrand, forKey: .deviceBrand)
        try container.encode(deviceManufacturer, forKey: .deviceManufacturer)
        try container.encode(deviceModel, forKey: .deviceModel)
        try container.encode(carrier, forKey: .carrier)
        try container.encode(country, forKey: .country)
        try container.encode(region, forKey: .region)
        try container.encode(city, forKey: .city)
        try container.encode(dma, forKey: .dma)
        try container.encode(idfa, forKey: .idfa)
        try container.encode(idfv, forKey: .idfv)
        try container.encode(adid, forKey: .adid)
        try container.encode(language, forKey: .language)
        try container.encode(library, forKey: .library)
        try container.encode(ip, forKey: .ip)
        try container.encodeIfPresent(plan, forKey: .plan)
        try container.encodeIfPresent(ingestionMetadata, forKey: .ingestionMetadata)
        try container.encode(revenue, forKey: .revenue)
        try container.encode(price, forKey: .price)
        try container.encode(quantity, forKey: .quantity)
        try container.encode(productId, forKey: .productId)
        try container.encode(revenueType, forKey: .revenueType)
        try container.encode(currency, forKey: .currency)
        try container.encode(partnerId, forKey: .partnerId)
    }
}

extension BaseEvent {
    func toString() -> String {
        var returnString = ""
        do {
            let encoder = JSONEncoder()
            encoder.outputFormatting = .sortedKeys
            let json = try encoder.encode(self)
            if let printed = String(data: json, encoding: .utf8) {
                returnString = printed
            }
        } catch {
            returnString = error.localizedDescription
        }
        return returnString
    }
}

extension BaseEvent {
    static func fromArrayString<T: BaseEvent>(jsonString: String) -> [T]? {
        let jsonData = jsonString.data(using: .utf8)!
        let decoder = JSONDecoder()
        return try? decoder.decode([T].self, from: jsonData)
    }

    static func fromString<T: BaseEvent>(jsonString: String) -> T? {
        let jsonData = jsonString.data(using: .utf8)!
        let decoder = JSONDecoder()
        return try? decoder.decode(T.self, from: jsonData)
    }
}
