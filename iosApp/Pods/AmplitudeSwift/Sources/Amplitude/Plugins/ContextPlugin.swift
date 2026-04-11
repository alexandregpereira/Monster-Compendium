//
//  ContextPlugin.swift
//
//
//  Created by Marvin Liu on 10/28/22.
//

#if os(iOS)
import CoreTelephony
#endif
import Foundation

class ContextPlugin: BeforePlugin {
    internal static var device = VendorSystem.current

    override func setup(amplitude: Amplitude) {
        super.setup(amplitude: amplitude)
        initializeDeviceId()
    }

    override func execute(event: BaseEvent) -> BaseEvent? {
        let context = staticContext

        // merge context data
        mergeContext(event: event, context: context)

        return event
    }

    internal var staticContext = staticContextData()

    internal static func staticContextData() -> [String: Any] {
        var staticContext = [String: Any]()
        // library
        staticContext["library"] = "\(Constants.SDK_LIBRARY)/\(Constants.SDK_VERSION)"

        // app info
        let info = Bundle.main.infoDictionary
        let localizedInfo = Bundle.main.localizedInfoDictionary
        var app = [String: Any]()
        if let info = info {
            app.merge(info) { (_, new) in new }
        }

        if let localizedInfo = localizedInfo {
            app.merge(localizedInfo) { (_, new) in new }
        }

        if app.count != 0 {
            staticContext["version_name"] = app["CFBundleShortVersionString"] ?? ""
        }

        // platform/device info
        let device = self.device
        staticContext["device_manufacturer"] = device.manufacturer
        staticContext["device_model"] = device.model
        staticContext["idfv"] = device.identifierForVendor
        staticContext["os_name"] = device.os_name
        staticContext["os_version"] = device.os_version
        staticContext["platform"] = device.platform
        if Locale.preferredLanguages.count > 0 {
            staticContext["language"] = Locale.preferredLanguages[0]
        }

        var carrier = "Unknown"
        #if os(iOS) && !targetEnvironment(simulator)
        let networkInfo = CTTelephonyNetworkInfo()
        if let providers = networkInfo.serviceSubscriberCellularProviders {
            for (_, provider) in providers where provider.mobileNetworkCode != nil {
                carrier = provider.carrierName ?? carrier
                // As long as we get one carrier information, we break.
                break
            }
        }
        #endif
        staticContext["carrier"] = carrier

        if Locale.preferredLanguages.count > 0 {
            staticContext["country"] = Locale.current.regionCode
        }

        return staticContext
    }

    internal func mergeContext(event: BaseEvent, context: [String: Any]) {
        if event.insertId == nil {
            event.insertId = NSUUID().uuidString
        }
        if event.library == nil {
            event.library = context["library"] as? String
        }
        if event.partnerId == nil {
            if let pId = self.amplitude?.configuration.partnerId {
                event.partnerId = pId
            }
        }
        let configuration = self.amplitude?.configuration
        let trackingOptions = configuration?.trackingOptions

        if configuration?.enableCoppaControl ?? false {
            trackingOptions?.mergeIn(other: TrackingOptions.forCoppaControl())
        }

        if trackingOptions?.shouldTrackVersionName() ?? false {
            event.versionName = context["version_name"] as? String
        }
        if trackingOptions?.shouldTrackOsName() ?? false {
            event.osName = context["os_name"] as? String
        }
        if trackingOptions?.shouldTrackOsVersion() ?? false {
            event.osVersion = context["os_version"] as? String
        }
        if trackingOptions?.shouldTrackDeviceManufacturer() ?? false {
            event.deviceManufacturer = context["device_manufacturer"] as? String
        }
        if trackingOptions?.shouldTrackDeviceModel() ?? false {
            event.deviceModel = context["device_model"] as? String
        }
        if trackingOptions?.shouldTrackCarrier() ?? false {
            event.carrier = context["carrier"] as? String
        }
        if trackingOptions?.shouldTrackIpAddress() ?? false {
            if event.ip == nil  {
                // get the ip in server side if there is no event level ip
                event.ip = "$remote"
            }
        }
        if trackingOptions?.shouldTrackCountry() ?? false && event.ip != "$remote" {
            event.country = context["country"] as? String
        }
        if trackingOptions?.shouldTrackIDFV() ?? false {
            event.idfv = context["idfv"] as? String
        }
        if trackingOptions?.shouldTrackLanguage() ?? false {
            event.language = context["language"] as? String
        }
        if trackingOptions?.shouldTrackPlatform() ?? false {
            event.platform = context["platform"] as? String
        }
        if event.plan == nil {
            if let plan = self.amplitude?.configuration.plan {
                event.plan = plan
            }
        }
        if event.ingestionMetadata == nil {
            if let ingestionMetadata = self.amplitude?.configuration.ingestionMetadata {
                event.ingestionMetadata = ingestionMetadata
            }
        }
    }

    func initializeDeviceId(forceReset: Bool = false) {
        var deviceId = forceReset ? nil : amplitude?.identity.deviceId
        if isValidDeviceId(deviceId) {
            return
        }
        if deviceId == nil, amplitude?.configuration.trackingOptions.shouldTrackIDFV() ?? false {
            if let idfv = staticContext["idfv"] as? String, idfv != "00000000-0000-0000-0000-000000000000" {
                deviceId = idfv
            }
        }
        if deviceId == nil {
            deviceId = NSUUID().uuidString
        }
        amplitude?.setDeviceId(deviceId: deviceId)
    }

    func isValidDeviceId(_ deviceId: String?) -> Bool {
        if deviceId == nil || deviceId == "e3f5536a141811db40efd6400f1d0a4e"
            || deviceId == "04bab7ee75b9a58d39b8dc54e8851084"
        {
            return false
        }
        return true
    }
}
