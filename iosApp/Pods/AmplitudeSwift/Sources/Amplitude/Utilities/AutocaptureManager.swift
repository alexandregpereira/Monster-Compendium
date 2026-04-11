#if AMPLITUDE_DISABLE_UIKIT
@_spi(Internal) import AmplitudeCoreNoUIKit
#else
@_spi(Internal) import AmplitudeCore
#endif
import Foundation

class AutocaptureManager {

    typealias ChangeCallback = ([String: Any]?) -> Void

    private let context: AmplitudeContext
    private let trackingQueue: DispatchQueue
    private var remoteConfigSubscription: Any?
    private let lock = NSLock()

    private var _enabledOptions: AutocaptureOptions = .init(rawValue: 0)
    private var enabledOptions: AutocaptureOptions {
        get { lock.withLock { _enabledOptions } }
        set { lock.withLock { _enabledOptions = newValue } }
    }

    private var _rageClickEnabled: Bool = false
    private var _deadClickEnabled: Bool = false

    var rageClickEnabled: Bool {
        lock.withLock { _rageClickEnabled }
    }

    var deadClickEnabled: Bool {
        lock.withLock { _deadClickEnabled }
    }

    private var changeCallbacks: [ChangeCallback] = []

    init(context: AmplitudeContext,
         trackingQueue: DispatchQueue,
         autocapture: AutocaptureOptions,
         rageClickEnabled: Bool,
         deadClickEnabled: Bool,
         enableRemoteConfig: Bool) {
        self.context = context
        self.trackingQueue = trackingQueue
        self._enabledOptions = autocapture
        self._rageClickEnabled = rageClickEnabled
        self._deadClickEnabled = deadClickEnabled

        if enableRemoteConfig {
            subscribeToRemoteConfig()
        }
    }

    func isEnabled(_ option: AutocaptureOptions) -> Bool {
        return enabledOptions.contains(option)
    }

    func onChange(_ callback: @escaping ChangeCallback) {
        lock.withLock {
            changeCallbacks.append(callback)
        }
    }

    func updateDiagnostics() {
        context.diagnosticsClient.setTag(
            name: "autocapture.enabled",
            value: enabledOptions.stringRepresentation()
        )
    }

    // MARK: - Remote Config

    private func subscribeToRemoteConfig() {
        remoteConfigSubscription = context.remoteConfigClient
            .subscribe(key: Constants.RemoteConfig.Key.autocapture) { [weak self] config, _, _ in
                self?.handleRemoteConfig(config)
            }
    }

    private func handleRemoteConfig(_ config: [String: Any]?) {
        if let config {
            // Update enabled options from config
            lock.withLock {
                if let sessions = config["sessions"] as? Bool {
                    if sessions {
                        _enabledOptions.formUnion(.sessions)
                    } else {
                        _enabledOptions.subtract(.sessions)
                    }
                }

                if let appLifecycles = config["appLifecycles"] as? Bool {
                    if appLifecycles {
                        _enabledOptions.formUnion(.appLifecycles)
                    } else {
                        _enabledOptions.subtract(.appLifecycles)
                    }
                }

                if let pageViews = config["pageViews"] as? Bool {
                    if pageViews {
                        _enabledOptions.formUnion(.screenViews)
                    } else {
                        _enabledOptions.subtract(.screenViews)
                    }
                }

                if let elementInteractions = config["elementInteractions"] as? Bool {
                    if elementInteractions {
                        _enabledOptions.formUnion(.elementInteractions)
                    } else {
                        _enabledOptions.subtract(.elementInteractions)
                    }
                }

                if let frustrationInteractions = config["frustrationInteractions"] as? [String: Any] {
                    if let enabled = frustrationInteractions["enabled"] as? Bool {
                        if enabled {
                            _enabledOptions.formUnion(.frustrationInteractions)
                        } else {
                            _enabledOptions.subtract(.frustrationInteractions)
                        }
                    }

                    if let rageClick = frustrationInteractions["rageClick"] as? [String: Any],
                       let enabled = rageClick["enabled"] as? Bool {
                        _rageClickEnabled = enabled
                    }

                    if let deadClick = frustrationInteractions["deadClick"] as? [String: Any],
                       let enabled = deadClick["enabled"] as? Bool {
                        _deadClickEnabled = enabled
                    }
                }

                if let networkTracking = config["networkTracking"] as? [String: Any],
                   let enabled = networkTracking["enabled"] as? Bool {
                    if enabled {
                        _enabledOptions.formUnion(.networkTracking)
                    } else {
                        _enabledOptions.subtract(.networkTracking)
                    }
                }
            }
        }

        // Notify callbacks with raw config
        let callbacks = lock.withLock { changeCallbacks }
        for callback in callbacks {
            callback(config)
        }

        // Update diagnostics
        trackingQueue.async { [weak self] in
            self?.updateDiagnostics()
        }
    }

    deinit {
        if let remoteConfigSubscription {
            context.remoteConfigClient.unsubscribe(remoteConfigSubscription)
        }
        remoteConfigSubscription = nil
    }
}
