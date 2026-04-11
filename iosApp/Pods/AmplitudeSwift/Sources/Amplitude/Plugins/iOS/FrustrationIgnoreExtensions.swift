//
//  FrustrationIgnoreExtensions.swift
//  Amplitude-Swift
//
//  Created by Jin Xu on 5/23/25.
//

#if (os(iOS) || os(tvOS) || os(visionOS) || targetEnvironment(macCatalyst)) && !AMPLITUDE_DISABLE_UIKIT

import SwiftUI
import UIKit

// MARK: - Frustration Click Ignore Extension for UIKit
extension UIView {
    private static var amp_ignoreRageClickKey: UInt8 = 0
    private static var amp_ignoreDeadClickKey: UInt8 = 0

    var amp_ignoreRageClick: Bool {
        return objc_getAssociatedObject(self, &UIView.amp_ignoreRageClickKey) as? Bool ?? false
    }

    var amp_ignoreDeadClick: Bool {
        return objc_getAssociatedObject(self, &UIView.amp_ignoreDeadClickKey) as? Bool ?? false
    }

    /// Mark this view to be ignored for specific interaction events
    /// - Parameter rageClick: Whether to ignore rage click detection for this view
    /// - Parameter deadClick: Whether to ignore dead click detection for this view
    @objc public func amp_ignoreInteractionEvent(rageClick: Bool = true, deadClick: Bool = true) {
        objc_setAssociatedObject(self, &UIView.amp_ignoreRageClickKey, rageClick, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        objc_setAssociatedObject(self, &UIView.amp_ignoreDeadClickKey, deadClick, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
    }
}

// MARK: - Frustration Click Ignore Extension for SwiftUI
// TODO: this can’t work properly on SwiftUI yet; keep it internal for now.

@available(iOS 13.0, tvOS 13.0, macOS 10.15, watchOS 6.0, *)
struct IgnoreInteractionEventModifier: ViewModifier {
    let rageClick: Bool
    let deadClick: Bool

    public func body(content: Content) -> some View {
        content
            .compositingGroup()
            .overlay(
                IgnoreInteractionViewRepresentable(rageClick: rageClick, deadClick: deadClick)
                    .frame(width: 0, height: 0)
            )
    }
}

@available(iOS 13.0, tvOS 13.0, macOS 10.15, watchOS 6.0, *)
struct IgnoreInteractionViewRepresentable: UIViewRepresentable {
    let rageClick: Bool
    let deadClick: Bool

    class AmplitudeIgnoreInteractionMarkerView: UIView {
        let rageClick: Bool
        let deadClick: Bool

        init(rageClick: Bool, deadClick: Bool) {
            self.rageClick = rageClick
            self.deadClick = deadClick
            super.init(frame: .zero)
            isAccessibilityElement = false
            isUserInteractionEnabled = false
        }

        override func didMoveToSuperview() {
            DispatchQueue.main.async {
                // TODO: this works but will block the whole hosting view
                if let superview = self.superview?.superview {
                    superview.amp_ignoreInteractionEvent(rageClick: self.rageClick, deadClick: self.deadClick)
                }
            }
        }

        required init?(coder: NSCoder) {
            fatalError("init(coder:) has not been implemented")
        }
    }

    func makeUIView(context: Context) -> UIView {
        AmplitudeIgnoreInteractionMarkerView(rageClick: rageClick, deadClick: deadClick)
    }

    func updateUIView(_ uiView: UIView, context: Context) {
        // No updates needed
    }
}

@available(iOS 13.0, tvOS 13.0, macOS 10.15, watchOS 6.0, *)
extension View {
    /// Mark this view to be ignored for specific interaction events
    /// - Parameter rageClick: Whether to ignore rage click detection for this view
    func amp_ignoreInteractionEvent(rageClick: Bool = true, deadClick: Bool = true) -> some View {
        self.modifier(IgnoreInteractionEventModifier(rageClick: rageClick, deadClick: deadClick))
    }
}

#endif
