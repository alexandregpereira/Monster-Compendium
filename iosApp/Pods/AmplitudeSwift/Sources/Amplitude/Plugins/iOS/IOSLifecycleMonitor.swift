//
//  IOSLifecycleMonitor.swift
//
//
//  Created by Hao Yu on 11/15/22.
//

#if (os(iOS) || os(tvOS) || os(visionOS) || targetEnvironment(macCatalyst)) && !AMPLITUDE_DISABLE_UIKIT

import AmplitudeCore
import Foundation
import SwiftUI

class IOSLifecycleMonitor: UtilityPlugin {

    private var utils: DefaultEventUtils?
    private var sendAppInstalledOnDidBecomeActive = false
    private var sendAppOpenedOnDidBecomeActive = false

    override init() {
        super.init()

        NotificationCenter.default.addObserver(self,
                                               selector: #selector(applicationDidFinishLaunchingNotification(notification:)),
                                               name: UIApplication.didFinishLaunchingNotification,
                                               object: nil)
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(applicationDidBecomeActive(notification:)),
                                               name: UIApplication.didBecomeActiveNotification,
                                               object: nil)
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(applicationWillEnterForeground(notification:)),
                                               name: UIApplication.willEnterForegroundNotification,
                                               object: nil)
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(applicationDidEnterBackground(notification:)),
                                               name: UIApplication.didEnterBackgroundNotification,
                                               object: nil)
    }

    public override func setup(amplitude: Amplitude) {
        super.setup(amplitude: amplitude)
        utils = DefaultEventUtils(amplitude: amplitude)

        let appState = IOSVendorSystem.applicationState
        // If app state is already active, dispatch installed / opened events now
        // we want to dispatch this from the initiating thread to maintain event ordering.
        if appState == .active {
            // this is added in init - launch on trackingQueue to allow identity to be set
            // prior to firing the event
            amplitude.trackingQueue.async { [self] in
                utils?.trackAppUpdatedInstalledEvent()
                amplitude.onEnterForeground(timestamp: currentTimestamp)
                utils?.trackAppOpenedEvent()
            }
        // If app state is inactive, it won't receive didFinishLaunching and willEnterForeground
        // notifications anymore, we need to send install and opened event when become active
        } else if appState == .inactive {
            sendAppInstalledOnDidBecomeActive = true
            sendAppOpenedOnDidBecomeActive = true
        }

        updateAutocaptureSetup()

        // Listen for autocapture config changes from the manager
        amplitude.autocaptureManager.onChange { [weak self] _ in
            self?.updateAutocaptureSetup()
        }
    }

    private func updateAutocaptureSetup() {
        guard let amplitude else { return }
        let manager = amplitude.autocaptureManager

        if manager.isEnabled(.screenViews) {
            UIKitScreenViews.register(amplitude)
        } else {
            UIKitScreenViews.unregister(amplitude)
        }

        // Register UIKitElementInteractions if either element interactions or frustration interactions is enabled
        let needsElementInteractions = manager.isEnabled(.elementInteractions) || manager.isEnabled(.frustrationInteractions)
        if needsElementInteractions {
            UIKitElementInteractions.register(amplitude)
        } else {
            UIKitElementInteractions.unregister(amplitude)
        }
    }

    @objc
    func applicationDidFinishLaunchingNotification(notification: Notification) {
        amplitude?.trackingQueue.async { [self] in
            utils?.trackAppUpdatedInstalledEvent()
        }

        // Pre SceneDelegate apps wil not fire a willEnterForeground notification on app launch.
        // Instead, use the initial applicationDidBecomeActive
        if !IOSVendorSystem.usesScenes {
            sendAppOpenedOnDidBecomeActive = true
        }
    }

    @objc
    func applicationDidBecomeActive(notification: Notification) {
        guard sendAppInstalledOnDidBecomeActive || sendAppOpenedOnDidBecomeActive else {
            return
        }
        let sendInstall = sendAppInstalledOnDidBecomeActive
        let sendOpened = sendAppOpenedOnDidBecomeActive
        sendAppInstalledOnDidBecomeActive = false
        sendAppOpenedOnDidBecomeActive = false

        amplitude?.trackingQueue.async { [self] in
            if sendInstall {
                utils?.trackAppUpdatedInstalledEvent()
            }
            amplitude?.onEnterForeground(timestamp: currentTimestamp)
            if sendOpened {
                utils?.trackAppOpenedEvent()
            }
        }
    }

    @objc
    func applicationWillEnterForeground(notification: Notification) {
        let fromBackground: Bool
        switch IOSVendorSystem.applicationState {
        case nil, .active, .inactive:
            fromBackground = false
        case .background:
            fromBackground = true
        @unknown default:
            fromBackground = false
        }

        amplitude?.onEnterForeground(timestamp: currentTimestamp)
        utils?.trackAppOpenedEvent(fromBackground: fromBackground)
    }

    @objc
    func applicationDidEnterBackground(notification: Notification) {
        guard let amplitude = amplitude else {
            return
        }
        amplitude.onExitForeground(timestamp: currentTimestamp)
        if amplitude.autocaptureManager.isEnabled(.appLifecycles) {
            amplitude.track(eventType: Constants.AMP_APPLICATION_BACKGROUNDED_EVENT)
        }
    }

    private var currentTimestamp: Int64 {
        return Int64(NSDate().timeIntervalSince1970 * 1000)
    }

    override func teardown() {
        super.teardown()

        if let amplitude {
            UIKitScreenViews.unregister(amplitude)
            UIKitElementInteractions.unregister(amplitude)
        }
    }
}

#endif
