import Foundation

public class DefaultEventUtils {

    private static let lock = NSLock()
    private static var instanceNamesThatSentAppUpdatedInstalled: Set<String> = []

    private weak var amplitude: Amplitude?

    private var trackAppLifecycles: Bool {
        amplitude?.autocaptureManager.isEnabled(.appLifecycles) ?? false
    }

    public init(amplitude: Amplitude) {
        self.amplitude = amplitude
    }

    public func trackAppUpdatedInstalledEvent() {
        guard let amplitude = amplitude else {
            return
        }

        let info = Bundle.main.infoDictionary
        let currentBuild = info?["CFBundleVersion"] as? String
        let currentVersion = info?["CFBundleShortVersionString"] as? String
        let previousBuild: String? = amplitude.storage.read(key: StorageKey.APP_BUILD)
        let previousVersion: String? = amplitude.storage.read(key: StorageKey.APP_VERSION)

        if currentBuild != previousBuild {
            try? amplitude.storage.write(key: StorageKey.APP_BUILD, value: currentBuild)
        }
        if currentVersion != previousVersion {
            try? amplitude.storage.write(key: StorageKey.APP_VERSION, value: currentVersion)
        }

        guard trackAppLifecycles else {
            return
        }

        // Only send one app installed / updated event per instance name, no matter how many times we are
        // reinitialized
        let instanceName = amplitude.configuration.instanceName
        let shouldSendAppInstalled = Self.lock.withLock {
            if !Self.instanceNamesThatSentAppUpdatedInstalled.contains(instanceName) {
                Self.instanceNamesThatSentAppUpdatedInstalled.insert(instanceName)
                return true
            }
            return false
        }

        guard shouldSendAppInstalled else {
            return
        }

        if previousBuild == nil || previousVersion == nil {
            amplitude.track(eventType: Constants.AMP_APPLICATION_INSTALLED_EVENT, eventProperties: [
                Constants.AMP_APP_BUILD_PROPERTY: currentBuild ?? "",
                Constants.AMP_APP_VERSION_PROPERTY: currentVersion ?? "",
            ])
        } else if currentBuild != previousBuild || currentVersion != previousVersion {
            amplitude.track(eventType: Constants.AMP_APPLICATION_UPDATED_EVENT, eventProperties: [
                Constants.AMP_APP_BUILD_PROPERTY: currentBuild ?? "",
                Constants.AMP_APP_VERSION_PROPERTY: currentVersion ?? "",
                Constants.AMP_APP_PREVIOUS_BUILD_PROPERTY: previousBuild ?? "",
                Constants.AMP_APP_PREVIOUS_VERSION_PROPERTY: previousVersion ?? "",
            ])
        }
    }

    func trackAppOpenedEvent(fromBackground: Bool = false) {
        guard let amplitude = amplitude, trackAppLifecycles else {
            return
        }

        let info = Bundle.main.infoDictionary
        let currentBuild = info?["CFBundleVersion"] as? String
        let currentVersion = info?["CFBundleShortVersionString"] as? String
        amplitude.track(eventType: Constants.AMP_APPLICATION_OPENED_EVENT, eventProperties: [
            Constants.AMP_APP_BUILD_PROPERTY: currentBuild ?? "",
            Constants.AMP_APP_VERSION_PROPERTY: currentVersion ?? "",
            Constants.AMP_APP_FROM_BACKGROUND_PROPERTY: fromBackground,
        ])
    }

}
