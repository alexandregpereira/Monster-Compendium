//
//  MacOSLifecycleMonitor.swift
//
//
//  Created by Hao Yu on 11/17/22.
//

#if os(macOS)
    import Cocoa

    class MacOSLifecycleMonitor: UtilityPlugin {
        private var application: NSApplication
        private var appNotifications: [NSNotification.Name] =
            [
                NSApplication.didBecomeActiveNotification,
                NSApplication.willResignActiveNotification,
            ]

        override init() {
            self.application = NSApplication.shared
            super.init()
            setupListeners()
        }

        @objc
        func notificationResponse(notification: NSNotification) {
            switch notification.name {
            case NSApplication.didBecomeActiveNotification:
                self.applicationDidBecomeActive(notification: notification)
            case NSApplication.willResignActiveNotification:
                self.applicationWillResignActive(notification: notification)
            default:
                break
            }
        }

        func setupListeners() {
            // Configure the current life cycle events
            let notificationCenter = NotificationCenter.default
            for notification in appNotifications {
                notificationCenter.addObserver(
                    self,
                    selector: #selector(notificationResponse(notification:)),
                    name: notification,
                    object: application
                )
            }
        }

        func applicationDidBecomeActive(notification: NSNotification) {
            let timestamp = Int64(NSDate().timeIntervalSince1970 * 1000)
            self.amplitude?.onEnterForeground(timestamp: timestamp)
        }

        func applicationWillResignActive(notification: NSNotification) {
            let timestamp = Int64(NSDate().timeIntervalSince1970 * 1000)
            self.amplitude?.onExitForeground(timestamp: timestamp)
        }
    }

#endif
