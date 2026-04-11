//
//  WatchOSLifecycleMonitor.swift
//
//
//  Created by Hao Yu on 11/17/22.
//

#if os(watchOS)

    import Foundation
    import WatchKit

    class WatchOSLifecycleMonitor: UtilityPlugin {
        var wasBackgrounded: Bool = false

        private var appNotifications: [NSNotification.Name] = [
            WKExtension.applicationWillEnterForegroundNotification,
            WKExtension.applicationDidEnterBackgroundNotification,
        ]

        override init() {
            super.init()
            setupListeners()
        }

        @objc
        func notificationResponse(notification: NSNotification) {
            switch notification.name {
            case WKExtension.applicationWillEnterForegroundNotification:
                self.applicationWillEnterForeground(notification: notification)
            case WKExtension.applicationDidEnterBackgroundNotification:
                self.applicationDidEnterBackground(notification: notification)
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
                    object: nil
                )
            }
        }

        func applicationWillEnterForeground(notification: NSNotification) {
            let timestamp = Int64(NSDate().timeIntervalSince1970 * 1000)
            self.amplitude?.onEnterForeground(timestamp: timestamp)
        }

        func applicationDidEnterBackground(notification: NSNotification) {
            let timestamp = Int64(NSDate().timeIntervalSince1970 * 1000)
            self.amplitude?.onExitForeground(timestamp: timestamp)
        }
    }

#endif
