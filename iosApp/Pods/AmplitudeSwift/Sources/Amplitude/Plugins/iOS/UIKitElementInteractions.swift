#if (os(iOS) || os(tvOS) || os(visionOS) || targetEnvironment(macCatalyst)) && !AMPLITUDE_DISABLE_UIKIT
import UIKit
import AmplitudeCore

class UIKitElementInteractions {
    struct EventData {
        enum Source {
            case actionMethod

            case gestureRecognizer
        }

        let screenName: String?

        let accessibilityLabel: String?

        let accessibilityIdentifier: String?

        let targetViewIdentifier: ObjectIdentifier

        let targetViewClass: String

        let targetText: String?

        let hierarchy: String

        fileprivate func elementInteractionEvent(for action: String, from source: Source? = nil, withName sourceName: String? = nil) -> ElementInteractionEvent {
            return ElementInteractionEvent(
                screenName: screenName,
                accessibilityLabel: accessibilityLabel,
                accessibilityIdentifier: accessibilityIdentifier,
                action: action,
                targetViewClass: targetViewClass,
                targetText: targetText,
                hierarchy: hierarchy,
                actionMethod: source == .actionMethod ? sourceName : nil,
                gestureRecognizer: source == .gestureRecognizer ? sourceName : nil
            )
        }
    }

    fileprivate static let amplitudeInstances = NSHashTable<Amplitude>.weakObjects()
    fileprivate static var rageClickDetectors: [ObjectIdentifier: RageClickDetector] = [:]
    fileprivate static var deadClickDetectors: [ObjectIdentifier: DeadClickDetector] = [:]
    fileprivate static let lock = NSLock()

    private static let addNotificationObservers: Void = {
        NotificationCenter.default.addObserver(UIKitElementInteractions.self, selector: #selector(didEndEditing), name: UITextField.textDidEndEditingNotification, object: nil)
        NotificationCenter.default.addObserver(UIKitElementInteractions.self, selector: #selector(didEndEditing), name: UITextView.textDidEndEditingNotification, object: nil)
    }()

    private static let setupMethodSwizzling: Void = {
        swizzleMethod(UIApplication.self, from: #selector(UIApplication.sendAction), to: #selector(UIApplication.amp_sendAction))
        swizzleMethod(UIGestureRecognizer.self, from: #selector(setter: UIGestureRecognizer.state), to: #selector(UIGestureRecognizer.amp_setState))
    }()

    static func register(_ amplitude: Amplitude) {
        let manager = amplitude.autocaptureManager

        lock.withLock {
            amplitudeInstances.add(amplitude)
            let identifier = ObjectIdentifier(amplitude)
            let frustrationInteractions = manager.isEnabled(.frustrationInteractions)

            if frustrationInteractions, manager.rageClickEnabled {
                rageClickDetectors[identifier] = RageClickDetector(amplitude: amplitude)
            } else if let rageClickDetector = rageClickDetectors.removeValue(forKey: identifier) {
                rageClickDetector.reset()
            }

            if frustrationInteractions, manager.deadClickEnabled {
                deadClickDetectors[identifier] = DeadClickDetector(amplitude: amplitude)
            } else if let deadClickDetector = deadClickDetectors.removeValue(forKey: identifier) {
                deadClickDetector.reset()
            }
        }
        setupMethodSwizzling
        addNotificationObservers
    }

    static func unregister(_ amplitude: Amplitude) {
        lock.withLock {
            amplitudeInstances.remove(amplitude)
            let identifier = ObjectIdentifier(amplitude)

            if let rageClickDetector = rageClickDetectors.removeValue(forKey: identifier) {
                rageClickDetector.reset()
            }

            if let deadClickDetector = deadClickDetectors.removeValue(forKey: identifier) {
                deadClickDetector.reset()
            }
        }
    }

    @objc static func didEndEditing(_ notification: NSNotification) {
        guard let view = notification.object as? UIView else { return }
        // Text fields in SwiftUI are identifiable only after the text field is edited.

        // Track element interaction events only if .elementInteractions is enabled
        lock.withLock {
            for amplitude in amplitudeInstances.allObjects where amplitude.autocaptureManager.isEnabled(.elementInteractions) {
                let elementInteractionEvent = view.eventData.elementInteractionEvent(for: "didEndEditing")
                amplitude.track(event: elementInteractionEvent)
            }
        }
    }

    private static func swizzleMethod(_ cls: AnyClass?, from original: Selector, to swizzled: Selector) {
        guard
            let originalMethod = class_getInstanceMethod(cls, original),
            let swizzledMethod = class_getInstanceMethod(cls, swizzled)
        else { return }

        let originalImp = method_getImplementation(originalMethod)
        let swizzledImp = method_getImplementation(swizzledMethod)

        class_replaceMethod(cls,
            swizzled,
            originalImp,
            method_getTypeEncoding(originalMethod))
        class_replaceMethod(cls,
            original,
            swizzledImp,
            method_getTypeEncoding(swizzledMethod))
    }

    static func interfaceChangeProviderDidChange(for amplitude: Amplitude, from oldProvider: InterfaceSignalProvider?, to newProvider: InterfaceSignalProvider?) {
        lock.withLock {
            let identifier = ObjectIdentifier(amplitude)
            self.deadClickDetectors[identifier]?.interfaceSignalProviderDidChange(from: oldProvider, to: newProvider)
        }
    }

    fileprivate static func processFrustrationInteractionForView(_ view: UIView,
                                                                 location: CGPoint,
                                                                 action: String,
                                                                 source: EventData.Source?,
                                                                 sourceName: String?) {
        let clickData = FrustrationClickData(
            time: Date(),
            eventData: view.eventData,
            location: location,
            action: action,
            source: source,
            sourceName: sourceName
        )

        lock.withLock {
            for amplitude in amplitudeInstances.allObjects {
                let identifier = ObjectIdentifier(amplitude)

                // Check if rage click detector exists (enabled via remote config or local config)
                if let rageClickDetector = rageClickDetectors[identifier],
                   !view.amp_ignoreRageClick {
                    rageClickDetector.processClick(clickData)
                }

                // Check if dead click detector exists (enabled via remote config or local config)
                if let deadClickDetector = deadClickDetectors[identifier],
                   !view.amp_ignoreDeadClick {
                    deadClickDetector.processClick(clickData)
                }
            }
        }
    }
}

extension Configuration {
    var isRageClickEnabled: Bool {
        autocapture.contains(.frustrationInteractions) && interactionsOptions.rageClick.enabled
    }

    var isDeadClickEnabled: Bool {
        autocapture.contains(.frustrationInteractions) && interactionsOptions.deadClick.enabled
    }
}

extension UIApplication {
    @objc func amp_sendAction(_ action: Selector, to target: Any?, from sender: Any?, for event: UIEvent?) -> Bool {
        let sendActionResult = amp_sendAction(action, to: target, from: sender, for: event)

        // TODO: Reduce SwiftUI noise by finding the unique view that the action method is attached to.
        // Currently, the action methods pointing to a SwiftUI target are blocked.
        let targetClass = String(cString: object_getClassName(target))
        if targetClass.contains("SwiftUI") {
            return sendActionResult
        }

        guard sendActionResult,
              let control = sender as? UIControl,
              control.amp_shouldTrack(action, for: target),
              let actionEvent = control.event(for: action, to: target)?.description
        else { return sendActionResult }

        // Track element interaction events only if .elementInteractions is enabled
        UIKitElementInteractions.lock.withLock {
            for amplitude in UIKitElementInteractions.amplitudeInstances.allObjects where amplitude.autocaptureManager.isEnabled(.elementInteractions) {
                let elementInteractionEvent = control.eventData.elementInteractionEvent(for: actionEvent, from: .actionMethod, withName: NSStringFromSelector(action))
                amplitude.track(event: elementInteractionEvent)
            }
        }

        if actionEvent == "touch", !control.amp_ignoreRageClick || !control.amp_ignoreDeadClick {
            var location = CGPoint.zero

            if let event = event, let touch = event.allTouches?.first {
                // For UIControl events, get location relative to the main window
                if let window = control.window {
                    location = touch.location(in: window)
                } else {
                    location = touch.location(in: control)
                }
            } else {
                // Fallback: use the center of the view in window coordinates
                if let window = control.window {
                    location = control.convert(control.bounds.amp_center, to: window)
                } else {
                    location = control.bounds.amp_center
                }
            }

            UIKitElementInteractions.processFrustrationInteractionForView(control, location: location, action: actionEvent, source: .actionMethod, sourceName: NSStringFromSelector(action))
        }

        return sendActionResult
    }
}

extension UIGestureRecognizer {
    @objc func amp_setState(_ state: UIGestureRecognizer.State) {
        amp_setState(state)

        guard state == .ended, let view else { return }

        // Block scroll and zoom events for `UIScrollView`.
        if let scrollView = view as? UIScrollView {
            if self === scrollView.panGestureRecognizer {
                return
            }

#if !os(tvOS)
            if self === scrollView.pinchGestureRecognizer {
                return
            }
#endif
        }

        let gestureAction: String?
        switch self {
        case is UITapGestureRecognizer:
            gestureAction = "tap"
        case is UISwipeGestureRecognizer:
            gestureAction = "swipe"
        case is UIPanGestureRecognizer:
            gestureAction = "pan"
        case is UILongPressGestureRecognizer:
            gestureAction = "longPress"
#if !os(tvOS)
        case is UIPinchGestureRecognizer:
            gestureAction = "pinch"
        case is UIRotationGestureRecognizer:
            gestureAction = "rotation"
        case is UIHoverGestureRecognizer:
            gestureAction = nil
#endif
#if !os(tvOS) && !os(visionOS)
        case is UIScreenEdgePanGestureRecognizer:
            gestureAction = "screenEdgePan"
#endif
        default:
            if view is UIWindow {
                gestureAction = nil
            } else {
                gestureAction = String(describing: type(of: self))
            }
        }

        guard let gestureAction else { return }

        // Track element interaction events only if .elementInteractions is enabled
        UIKitElementInteractions.lock.withLock {
            for amplitude in UIKitElementInteractions.amplitudeInstances.allObjects where amplitude.autocaptureManager.isEnabled(.elementInteractions) {
                let elementInteractionEvent = view.eventData.elementInteractionEvent(for: gestureAction, from: .gestureRecognizer, withName: descriptiveTypeName)
                amplitude.track(event: elementInteractionEvent)
            }
        }

        if !view.amp_ignoreDeadClick || !view.amp_ignoreRageClick {
            let location = self.location(in: nil)
            UIKitElementInteractions.processFrustrationInteractionForView(view, location: location, action: gestureAction, source: .gestureRecognizer, sourceName: descriptiveTypeName)
        }
    }
}

extension UIView {
    private static let viewHierarchyDelimiter = " → "

    var eventData: UIKitElementInteractions.EventData {
        return UIKitElementInteractions.EventData(
            screenName: owningViewController
                .flatMap(UIViewController.amp_topViewController)
                .flatMap(UIKitScreenViews.screenName),
            accessibilityLabel: accessibilityLabel,
            accessibilityIdentifier: accessibilityIdentifier,
            targetViewIdentifier: ObjectIdentifier(self),
            targetViewClass: descriptiveTypeName,
            targetText: amp_title,
            hierarchy: sequence(first: self, next: \.superview)
                .map { $0.descriptiveTypeName }
                .joined(separator: UIView.viewHierarchyDelimiter))
    }
}

extension UIControl {
    func event(for action: Selector, to target: Any?) -> UIControl.Event? {
        var events: [UIControl.Event] = [
            .touchDown, .touchDownRepeat, .touchDragInside, .touchDragOutside,
            .touchDragEnter, .touchDragExit, .touchUpInside, .touchUpOutside,
            .touchCancel, .valueChanged, .editingDidBegin, .editingChanged,
            .editingDidEnd, .editingDidEndOnExit, .primaryActionTriggered
        ]
        if #available(iOS 14.0, tvOS 14.0, macCatalyst 14.0, *) {
            events.append(.menuActionTriggered)
        }

        return events.first { event in
            self.actions(forTarget: target, forControlEvent: event)?.contains(action.description) ?? false
        }
    }
}

extension UIControl.Event {
    var description: String? {
        if UIControl.Event.allTouchEvents.contains(self) {
            return "touch"
        } else if UIControl.Event.allEditingEvents.contains(self) {
            return "edit"
        } else if self == .valueChanged {
            return "valueChange"
        } else if self == .primaryActionTriggered {
            return "primaryAction"
        } else if #available(iOS 14.0, tvOS 14.0, macCatalyst 14.0, *), self == .menuActionTriggered {
            return "menuAction"
        }
        return nil
    }
}

extension UIResponder {
    var owningViewController: UIViewController? {
        self as? UIViewController ?? next?.owningViewController
    }
}

extension NSObject {
    var descriptiveTypeName: String {
        String(describing: type(of: self))
    }
}

protocol ActionTrackable {
    var amp_title: String? { get }
    func amp_shouldTrack(_ action: Selector, for target: Any?) -> Bool
}

extension UIView: ActionTrackable {
    @objc var amp_title: String? { nil }
    @objc func amp_shouldTrack(_ action: Selector, for target: Any?) -> Bool { false }
}

extension UIControl {
    override func amp_shouldTrack(_ action: Selector, for target: Any?) -> Bool {
        actions(forTarget: target, forControlEvent: .touchUpInside)?.contains(action.description) ?? false
    }
}

extension UIButton {
    override var amp_title: String? { currentTitle ?? currentImage?.accessibilityIdentifier }
}

extension UISegmentedControl {
    override var amp_title: String? { titleForSegment(at: selectedSegmentIndex) }
    override func amp_shouldTrack(_ action: Selector, for target: Any?) -> Bool {
        actions(forTarget: target, forControlEvent: .valueChanged)?.contains(action.description) ?? false
    }
}

extension UIPageControl {
    override func amp_shouldTrack(_ action: Selector, for target: Any?) -> Bool {
        actions(forTarget: target, forControlEvent: .valueChanged)?.contains(action.description) ?? false
    }
}

#if !os(tvOS)
extension UIDatePicker {
    override func amp_shouldTrack(_ action: Selector, for target: Any?) -> Bool {
        actions(forTarget: target, forControlEvent: .valueChanged)?.contains(action.description) ?? false
    }
}

extension UISwitch {
    override func amp_shouldTrack(_ action: Selector, for target: Any?) -> Bool {
        actions(forTarget: target, forControlEvent: .valueChanged)?.contains(action.description) ?? false
    }
}
#endif

#endif
