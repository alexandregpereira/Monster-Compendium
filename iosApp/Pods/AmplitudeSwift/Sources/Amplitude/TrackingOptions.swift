//
//  TrackingOptions.swift
//
//
//  Created by Marvin Liu on 10/28/22.
//

import Foundation

public class TrackingOptions {

    public init() {}

    private static let COPPA_CONTROL_PROPERTIES = [
        Constants.AMP_TRACKING_OPTION_IDFA,
        Constants.AMP_TRACKING_OPTION_IDFV,
        Constants.AMP_TRACKING_OPTION_CITY,
        Constants.AMP_TRACKING_OPTION_IP_ADDRESS,
    ]

    var disabledFields: Set<String> = []

    public func shouldTrackVersionName() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_VERSION_NAME)
    }

    @discardableResult
    public func disableTrackVersionName() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_VERSION_NAME)
        return self
    }

    public func shouldTrackOsName() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_OS_NAME)
    }

    @discardableResult
    public func disableTrackOsName() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_OS_NAME)
        return self
    }

    public func shouldTrackOsVersion() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_OS_VERSION)
    }

    @discardableResult
    public func disableTrackOsVersion() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_OS_VERSION)
        return self
    }

    public func shouldTrackDeviceManufacturer() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_DEVICE_MANUFACTURER)
    }

    @discardableResult
    public func disableTrackDeviceManufacturer() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_DEVICE_MANUFACTURER)
        return self
    }

    public func shouldTrackDeviceModel() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_DEVICE_MODEL)
    }

    @discardableResult
    public func disableTrackDeviceModel() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_DEVICE_MODEL)
        return self
    }

    public func shouldTrackCarrier() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_CARRIER)
    }

    @discardableResult
    public func disableTrackCarrier() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_CARRIER)
        return self
    }

    public func shouldTrackIpAddress() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_IP_ADDRESS)
    }

    @discardableResult
    public func disableTrackIpAddress() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_IP_ADDRESS)
        return self
    }

    public func shouldTrackCountry() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_COUNTRY)
    }

    @discardableResult
    public func disableTrackCountry() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_COUNTRY)
        return self
    }

    public func shouldTrackCity() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_CITY)
    }

    @discardableResult
    public func disableTrackCity() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_CITY)
        return self
    }

    public func shouldTrackDMA() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_DMA)
    }

    @discardableResult
    public func disableTrackDMA() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_DMA)
        return self
    }

    public func shouldTrackIDFV() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_IDFV)
    }

    @discardableResult
    public func disableTrackIDFV() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_IDFV)
        return self
    }

    public func shouldTrackLanguage() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_LANGUAGE)
    }

    @discardableResult
    public func disableTrackLanguage() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_LANGUAGE)
        return self
    }

    public func shouldTrackRegion() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_REGION)
    }

    @discardableResult
    public func disableTrackRegion() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_REGION)
        return self
    }

    public func shouldTrackPlatform() -> Bool {
        return shouldTrackField(field: Constants.AMP_TRACKING_OPTION_PLATFORM)
    }

    @discardableResult
    public func disableTrackPlatform() -> TrackingOptions {
        disabledFields.insert(Constants.AMP_TRACKING_OPTION_PLATFORM)
        return self
    }

    static func forCoppaControl() -> TrackingOptions {
        let trackingOptions = TrackingOptions()
        for property in COPPA_CONTROL_PROPERTIES {
            trackingOptions.disableTrackingField(field: property)
        }

        return trackingOptions
    }

    @discardableResult
    func mergeIn(other: TrackingOptions) -> TrackingOptions {
        for key in other.disabledFields {
            disableTrackingField(field: key)
        }
        return self
    }

    private func shouldTrackField(field: String) -> Bool {
        return !disabledFields.contains(field)
    }

    private func disableTrackingField(field: String) {
        disabledFields.insert(field)
    }
}
