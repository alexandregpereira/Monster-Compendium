//
//  ObjCNetworkTrackingOptions.swift
//  Amplitude-Swift
//
//  Created by Jin Xu on 4/24/25.
//

import Foundation

@objc(AMPNetworkTrackingCaptureRule)
public class ObjCCaptureRule: NSObject {
    var captureRule: NetworkTrackingOptions.CaptureRule

    @objc
    public var hosts: [String] {
        get {
            return captureRule.hosts
        }
        set {
            captureRule.hosts = newValue
        }
    }

    @objc
    public var statusCodeRange: String {
        get {
            return captureRule.statusCodeRange
        }
        set {
            captureRule.statusCodeRange = newValue
        }
    }

    @objc
    public convenience init(hosts: [String], statusCodeRange: String = "500-599") {
        self.init(.init(hosts: hosts, statusCodeRange: statusCodeRange))
    }

    init(_ captureRule: NetworkTrackingOptions.CaptureRule) {
        self.captureRule = captureRule
        super.init()
    }
}

@objc(AMPNetworkTrackingOptions)
public class ObjCNetworkTrackingOptions: NSObject {
    var options: NetworkTrackingOptions

    @objc
    public convenience init(captureRules: [ObjCCaptureRule], ignoreHosts: [String] = [], ignoreAmplitudeRequests: Bool = true) {
        self.init(.init(
            captureRules: captureRules.map { $0.captureRule },
            ignoreHosts: ignoreHosts,
            ignoreAmplitudeRequests: ignoreAmplitudeRequests
        ))
    }

    init(_ options: NetworkTrackingOptions) {
        self.options = options
        super.init()
    }

    @objc
    public var captureRules: [ObjCCaptureRule] {
        get {
            return options.captureRules.map { ObjCCaptureRule($0) }
        }
        set {
            options.captureRules = newValue.map { $0.captureRule }
        }
    }

    @objc
    public var ignoreHosts: [String] {
        get {
            return options.ignoreHosts
        }
        set {
            options.ignoreHosts = newValue
        }
    }

    @objc
    public var ignoreAmplitudeRequests: Bool {
        get {
            return options.ignoreAmplitudeRequests
        }
        set {
            options.ignoreAmplitudeRequests = newValue
        }
    }

    @objc
    public static func defaultOptions() -> ObjCNetworkTrackingOptions {
        return ObjCNetworkTrackingOptions(NetworkTrackingOptions.default)
    }
}
