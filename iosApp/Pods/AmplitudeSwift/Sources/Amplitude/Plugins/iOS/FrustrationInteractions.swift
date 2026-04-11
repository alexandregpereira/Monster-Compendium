//
//  FrustrationInteractions.swift
//  Amplitude-Swift
//
//  Created by Jin Xu on 6/19/25.
//

#if (os(iOS) || os(tvOS) || os(visionOS) || targetEnvironment(macCatalyst)) && !AMPLITUDE_DISABLE_UIKIT
import UIKit
import AmplitudeCore

struct FrustrationClickData {
    let time: Date
    let eventData: UIKitElementInteractions.EventData
    let location: CGPoint
    let action: String
    let source: UIKitElementInteractions.EventData.Source?
    let sourceName: String?

    func generateEventProperties() -> [String: Any] {
        var eventProperties: [String: Any] = [:]
        eventProperties[Constants.AMP_APP_SCREEN_NAME_PROPERTY] = eventData.screenName
        eventProperties[Constants.AMP_APP_TARGET_AXLABEL_PROPERTY] = eventData.accessibilityLabel
        eventProperties[Constants.AMP_APP_TARGET_AXIDENTIFIER_PROPERTY] = eventData.accessibilityIdentifier
        eventProperties[Constants.AMP_APP_ACTION_PROPERTY] = action
        eventProperties[Constants.AMP_APP_TARGET_VIEW_CLASS_PROPERTY] = eventData.targetViewClass
        eventProperties[Constants.AMP_APP_TARGET_TEXT_PROPERTY] = eventData.targetText
        eventProperties[Constants.AMP_APP_HIERARCHY_PROPERTY] = eventData.hierarchy
        eventProperties[Constants.AMP_APP_ACTION_METHOD_PROPERTY] = source == .actionMethod ? sourceName : nil
        eventProperties[Constants.AMP_APP_GESTURE_RECOGNIZER_PROPERTY] = source == .gestureRecognizer ? sourceName : nil
        return eventProperties
    }
}

class DeadClickDetector: InterfaceSignalReceiver, @unchecked Sendable {
    private let CLICK_TIMEOUT = 3.0
    // Check all pending clicks to see if this UI change is related to any of them
    // Account for slight delay between click and UI change (typically < 500ms)
    private let UI_CHANGE_MAX_DELAY = 0.5 // 500ms max delay between click and UI change

    private var pendingClicks: [FrustrationClickData] = []
    private var timer: Timer?
    private let lock = NSLock()
    private weak var amplitude: Amplitude?

    init(amplitude: Amplitude) {
        self.amplitude = amplitude
        self.amplitude?.interfaceSignalProvider?.addInterfaceSignalReceiver(self)
    }

    func interfaceSignalProviderDidChange(from oldProvider: InterfaceSignalProvider?, to newProvider: InterfaceSignalProvider?) {
        oldProvider?.removeInterfaceSignalReceiver(self)
        newProvider?.addInterfaceSignalReceiver(self)
    }

    func processClick(_ clickData: FrustrationClickData) {
        lock.withLock {
            guard self.amplitude?.interfaceSignalProvider?.isProviding == true else {
                return
            }

            pendingClicks.append(clickData)

            if timer?.isValid != true {
                refreshTimer()
            }
        }
    }

    @objc func onInterfaceChanged(signal: InterfaceChangeSignal) {
        lock.withLock {
            let interfaceChangeTimestamp = signal.time.timeIntervalSince1970

            timer?.invalidate()

            // remove the clicks happend before UI change
            pendingClicks.removeAll { clickData in
                return clickData.time.timeIntervalSince1970 < interfaceChangeTimestamp
            }

            refreshTimer()
        }
    }

    func refreshTimer() {
        guard let firstClick = pendingClicks.first else { return }

        let timeoutInterval = CLICK_TIMEOUT + UI_CHANGE_MAX_DELAY - Date().timeIntervalSince(firstClick.time)
        if timeoutInterval > 0 {
            timer = Timer.scheduledTimer(withTimeInterval: timeoutInterval, repeats: false) { [weak self] _ in
                self?.lock.withLock {
                    self?.triggerDeadClick()
                }
            }
        } else {
            triggerDeadClick()
        }
    }

    func onStartProviding() {
        // do nothing
    }

    func onStopProviding() {
        // drop all unsend clicks because we can not get ui change any more
        reset()
    }

    private func triggerDeadClick() {
        guard let clickData = pendingClicks.first,
              let amplitude = amplitude else { return }

        var eventProperties = clickData.generateEventProperties()
        eventProperties[Constants.AMP_COORDINATE_X] = clickData.location.x
        eventProperties[Constants.AMP_COORDINATE_Y] = clickData.location.y

        amplitude.track(event: BaseEvent(timestamp: clickData.time.amp_timestamp(),
                                         eventType: Constants.AMP_DEAD_CLICK_EVENT,
                                         eventProperties: eventProperties))

        trim(for: clickData.eventData.targetViewIdentifier)

        timer?.invalidate()

        refreshTimer()
    }

    func trim(for elementIdentifier: ObjectIdentifier) {
        pendingClicks.removeAll { clickData in
            clickData.eventData.targetViewIdentifier == elementIdentifier
        }
    }

    func reset() {
        lock.withLock {
            timer?.invalidate()
            pendingClicks.removeAll()
        }
    }
}

class RageClickDetector {
    private let CLICK_RANGE_THRESHOLD: CGFloat = 50
    private let CLICK_COUNT_THRESHOLD: Int = 4
    private let CLICK_TIMEOUT: TimeInterval = 1.0

    private var clickQueue: [FrustrationClickData] = []
    private var debounceTimer: Timer?
    private let lock = NSLock()
    private weak var amplitude: Amplitude?

    init(amplitude: Amplitude) {
        self.amplitude = amplitude
    }

    func processClick(_ clickData: FrustrationClickData) {
        lock.withLock {
            let timeoutInterval = CLICK_TIMEOUT

            // Trigger a rage click if the last click is not the same element or location
            if let lastClick = clickQueue.last,
               !isSameElement(lastClick, clickData) || !isWithinRange(lastClick.location, clickData.location) {
                triggerRageClick()
            }

            // Check if the last click satisfy the rage click threshold in timeout interval
            let thresholdIndex = clickQueue.count - (CLICK_COUNT_THRESHOLD - 1)
            if thresholdIndex >= 0 {
                let thresholdClick = clickQueue[thresholdIndex]
                let timeSinceThresholdClick = clickData.time.timeIntervalSince(thresholdClick.time)

                if timeSinceThresholdClick >= timeoutInterval {
                    if thresholdIndex == 0 {
                        clickQueue.removeFirst()
                    } else {
                        triggerRageClick()
                    }
                }
            }

            clickQueue.append(clickData)

            debounceTimer?.invalidate()
            debounceTimer = Timer.scheduledTimer(withTimeInterval: timeoutInterval, repeats: false) { [weak self] _ in
                self?.triggerRageClick()
            }
        }
    }

    private func isSameElement(_ data1: FrustrationClickData, _ data2: FrustrationClickData) -> Bool {
        return data1.eventData.targetViewIdentifier == data2.eventData.targetViewIdentifier
    }

    private func isWithinRange(_ point1: CGPoint, _ point2: CGPoint) -> Bool {
        let distance = sqrt(pow(point1.x - point2.x, 2) + pow(point1.y - point2.y, 2))
        return distance <= CLICK_RANGE_THRESHOLD
    }

    private func triggerRageClick() {
        defer { clickQueue.removeAll() }

        guard clickQueue.count >= CLICK_COUNT_THRESHOLD,
              let amplitude = amplitude,
              let firstClick = clickQueue.first,
              let lastClick = clickQueue.last
        else { return }

        let clicks = clickQueue.map { clickData in
            [
                "X": clickData.location.x,
                "Y": clickData.location.y,
                "Time": clickData.time.amp_iso8601String()
            ]
        }

        var eventProperties: [String: Any] = firstClick.generateEventProperties()

        // Add rage click specific properties
        eventProperties[Constants.AMP_BEGIN_TIME_PROPERTY] = firstClick.time.amp_iso8601String()
        eventProperties[Constants.AMP_END_TIME_PROPERTY] = lastClick.time.amp_iso8601String()
        let duration = lastClick.time.timeIntervalSince(firstClick.time) * 1000 // Convert to milliseconds
        eventProperties[Constants.AMP_DURATION_PROPERTY] = duration
        eventProperties[Constants.AMP_CLICKS_PROPERTY] = clicks
        eventProperties[Constants.AMP_CLICK_COUNT_PROPERTY] = clicks.count

        amplitude.track(event: BaseEvent(timestamp: firstClick.time.amp_timestamp(),
                                         eventType: Constants.AMP_RAGE_CLICK_EVENT,
                                         eventProperties: eventProperties))
    }

    func reset() {
        lock.withLock {
            debounceTimer?.invalidate()
            debounceTimer = nil
            clickQueue.removeAll()
        }
    }
}

#endif
