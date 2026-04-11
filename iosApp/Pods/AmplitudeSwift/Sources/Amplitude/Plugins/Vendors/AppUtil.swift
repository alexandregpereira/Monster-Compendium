//
//  AppleUtils.swift
//
//  Created by Hao Yu on 11/16/22.
//

import Foundation

#if (os(iOS) || os(tvOS) || os(visionOS) || targetEnvironment(macCatalyst)) && !AMPLITUDE_DISABLE_UIKIT
    import SystemConfiguration
    import UIKit

    internal class IOSVendorSystem: VendorSystem {
        private let device = UIDevice.current
        override var manufacturer: String {
            return "Apple"
        }

        override var model: String {
            return deviceModel()
        }

        override var identifierForVendor: String? {
            return device.identifierForVendor?.uuidString
        }

        override var os_name: String {
            return device.systemName.lowercased()
        }

        override var os_version: String {
            return device.systemVersion
        }

        override var platform: String {
            #if os(tvOS)
                return "tvOS"
            #elseif os(visionOS)
                return "visionOS"
            #elseif targetEnvironment(macCatalyst)
                return "macOS"
            #else
                return "iOS"
            #endif
        }

        private func getPlatformString() -> String? {
            var name: [Int32] = [CTL_HW, HW_MACHINE]
            var size: Int = 0
            guard sysctl(&name, 2, nil, &size, nil, 0) == 0, size > 0 else {
                return nil
            }
            var hw_machine = [CChar](repeating: 0, count: size + 1)
            guard sysctl(&name, 2, &hw_machine, &size, nil, 0) == 0 else {
                return nil
            }
            return String(cString: hw_machine)
        }

        private func deviceModel() -> String {
            let platform = getPlatformString() ?? "unknown"
            return getDeviceModel(platform: platform)
        }

        override var requiredPlugin: Plugin {
            return IOSLifecycleMonitor()
        }

        override func beginBackgroundTask() -> BackgroundTaskCompletionCallback? {
            if !isRunningInAppExtension, let application = IOSVendorSystem.sharedApplication {
                var id: UIBackgroundTaskIdentifier = .invalid
                let callback = { () in
                    application.endBackgroundTask(id)
                    id = .invalid
                }
                id = application.beginBackgroundTask(withName: "amplitude", expirationHandler: callback)
                return callback
            } else {
                let semaphore = DispatchSemaphore(value: 0)
                ProcessInfo.processInfo.performExpiringActivity(withReason: "Amplitude") { expired in
                    guard !expired else {
                        // If we've expired, just let the system terminate the process
                        return
                    }
                    semaphore.wait()
                }
                return {
                    semaphore.signal()
                }
            }
        }

        private var isRunningInAppExtension: Bool {
            return Bundle.main.bundlePath.hasSuffix(".appex")
        }

        // Extension-safe accessor for sharedApplication. Will return nil if run in an extension.
        // prefer to use individual property accessors which we can override for tests
        private static var sharedApplication: UIApplication? {
            return UIApplication.value(forKeyPath: "sharedApplication") as? UIApplication
        }

        private static var overrideApplicationState: UIApplication.State?

        static func overrideApplicationState(_ applicationState: UIApplication.State?) {
            guard isRunningTests else {
                return
            }
            overrideApplicationState = applicationState
        }

        static var applicationState: UIApplication.State? {
            if let overrideApplicationState {
                return overrideApplicationState
            }

            // Use keypath vs applicationState property to avoid main thread checker warning
            guard let app = sharedApplication,
                  let rawState = app.value(forKey: #keyPath(UIApplication.applicationState)) as? Int,
                  let state = UIApplication.State(rawValue: rawState) else {
                return nil
            }

            return state
        }

        private static var overrideUsesScenes: Bool?

        static func overrideUsesScenes(_ usesScenes: Bool?) {
            guard isRunningTests else {
                return
            }
            overrideUsesScenes = usesScenes
        }

        static var usesScenes: Bool {
            if let overrideUsesScenes {
                return overrideUsesScenes
            }

            let sceneManifest = Bundle.main.infoDictionary?["UIApplicationSceneManifest"] as? [String: Any]
            let sceneConfigurations = sceneManifest?["UISceneConfigurations"] as? [String: Any] ?? [:]
            let hasSceneConfigurations = !sceneConfigurations.isEmpty

            if hasSceneConfigurations {
                return true
            }

            let selector = #selector(UIApplicationDelegate.application(_:configurationForConnecting:options:))
            let usesSceneDelegate = sharedApplication?.delegate?.responds(to: selector) ?? false

            if usesSceneDelegate {
                return true
            }

            return false
        }

        private static var isRunningTests: Bool {
            return NSClassFromString("XCTestCase") != nil
        }
    }
#endif

#if os(macOS)
    import Cocoa

    internal class MacOSVendorSystem: VendorSystem {
        private let device = ProcessInfo.processInfo

        override var manufacturer: String {
            return "Apple"
        }

        override var model: String {
            return deviceModel()
        }

        override var identifierForVendor: String? {
            // apple suggested to use this for receipt validation
            // in MAS, works for this too.
            return macAddress(bsd: "en0")
        }

        override var os_name: String {
            return "macos"
        }

        override var os_version: String {
            return String(
                format: "%ld.%ld.%ld",
                device.operatingSystemVersion.majorVersion,
                device.operatingSystemVersion.minorVersion,
                device.operatingSystemVersion.patchVersion
            )
        }

        override var requiredPlugin: Plugin {
            return MacOSLifecycleMonitor()
        }

        override var platform: String {
            return "macOS"
        }

        private func getPlatformString() -> String {
            var systemInfo = utsname()
            uname(&systemInfo)
            let machineMirror = Mirror(reflecting: systemInfo.machine)
            let identifier = machineMirror.children.reduce("") { identifier, element in
                guard let value = element.value as? Int8, value != 0 else { return identifier }
                return identifier + String(UnicodeScalar(UInt8(value)))
            }
            return identifier
        }

        private func deviceModel() -> String {
            let platform = getPlatformString()
            return getDeviceModel(platform: platform)
        }

        private func macAddress(bsd: String) -> String? {
            let MAC_ADDRESS_LENGTH = 6
            let separator = ":"

            var length: size_t = 0
            var buffer: [CChar]

            let bsdIndex = Int32(if_nametoindex(bsd))
            if bsdIndex == 0 {
                return nil
            }
            let bsdData = Data(bsd.utf8)
            var managementInfoBase = [CTL_NET, AF_ROUTE, 0, AF_LINK, NET_RT_IFLIST, bsdIndex]

            if sysctl(&managementInfoBase, 6, nil, &length, nil, 0) < 0 {
                return nil
            }

            buffer = [CChar](
                unsafeUninitializedCapacity: length,
                initializingWith: { buffer, initializedCount in
                    for x in 0..<length { buffer[x] = 0 }
                    initializedCount = length
                }
            )

            if sysctl(&managementInfoBase, 6, &buffer, &length, nil, 0) < 0 {
                return nil
            }

            let infoData = Data(bytes: buffer, count: length)
            let indexAfterMsghdr = MemoryLayout<if_msghdr>.stride + 1
            let rangeOfToken = infoData[indexAfterMsghdr...].range(of: bsdData)!
            let lower = rangeOfToken.upperBound
            let upper = lower + MAC_ADDRESS_LENGTH
            let macAddressData = infoData[lower..<upper]
            let addressBytes = macAddressData.map { String(format: "%02x", $0) }
            return addressBytes.joined(separator: separator)
        }
    }
#endif

#if os(watchOS)
    import WatchKit

    internal class WatchOSVendorSystem: VendorSystem {
        private let device = WKInterfaceDevice.current()

        override var manufacturer: String {
            return "Apple"
        }

        override var model: String {
            return deviceModel()
        }

        override var identifierForVendor: String? {
            // apple suggested to use this for receipt validation
            // in MAS, works for this too.
            return device.identifierForVendor?.uuidString
        }

        override var os_name: String {
            return "watchos"
        }

        override var os_version: String {
            return device.systemVersion
        }

        override var platform: String {
            return "watchOS"
        }

        private func getPlatformString() -> String? {
            var name: [Int32] = [CTL_HW, HW_MACHINE]
            var size: Int = 0
            guard sysctl(&name, 2, nil, &size, nil, 0) == 0, size > 0 else {
                return nil
            }
            var hw_machine = [CChar](repeating: 0, count: size + 1)
            guard sysctl(&name, 2, &hw_machine, &size, nil, 0) == 0 else {
                return nil
            }
            return String(cString: hw_machine)
        }

        private func deviceModel() -> String {
            let platform = getPlatformString() ?? "unknown"
            return getDeviceModel(platform: platform)
        }

        override var requiredPlugin: Plugin {
            return WatchOSLifecycleMonitor()
        }

        // Per https://developer.apple.com/documentation/technotes/tn3135-low-level-networking-on-watchos,
        // NWPathMonitor is not supported on most WatchOS apps when running on a real device.
        override var networkConnectivityCheckingEnabled: Bool {
            return false
        }
    }
#endif

private func getDeviceModel(platform: String) -> String {
    // use server device mapping except for the following exceptions

    if platform == "i386" || platform == "x86_64" {
        return "Simulator"
    }

    if platform.hasPrefix("MacBookAir") {
        return "MacBook Air"
    }

    if platform.hasPrefix("MacBookPro") {
        return "MacBook Pro"
    }

    if platform.hasPrefix("MacBook") {
        return "MacBook"
    }

    if platform.hasPrefix("MacPro") {
        return "Mac Pro"
    }

    if platform.hasPrefix("Macmini") {
        return "Mac Mini"
    }

    if platform.hasPrefix("iMac") {
        return "iMac"
    }

    if platform.hasPrefix("Xserve") {
        return "Xserve"
    }

    return platform
}
