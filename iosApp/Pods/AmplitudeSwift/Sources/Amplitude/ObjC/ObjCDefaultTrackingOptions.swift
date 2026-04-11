import Foundation

@objc(AMPDefaultTrackingOptions)
@available(*, deprecated, renamed: "AMPAutocaptureOptions", message: "Please use `AMPAutocaptureOptions` instead")
public class ObjCDefaultTrackingOptions: NSObject {
    internal let options: DefaultTrackingOptions

    @objc
    convenience public override init() {
        self.init(DefaultTrackingOptions())
    }

    internal init(_ options: DefaultTrackingOptions) {
        self.options = options
    }

    @objc
    public static var ALL: ObjCDefaultTrackingOptions {
        ObjCDefaultTrackingOptions(DefaultTrackingOptions.ALL)
    }

    @objc
    public static var NONE: ObjCDefaultTrackingOptions {
        ObjCDefaultTrackingOptions(DefaultTrackingOptions.NONE)
    }

    @objc
    public var sessions: Bool {
        get {
            options.sessions
        }
        set(value) {
            options.sessions = value
        }
    }

    @objc
    public var appLifecycles: Bool {
        get {
            options.appLifecycles
        }
        set(value) {
            options.appLifecycles = value
        }
    }

    @objc
    public var screenViews: Bool {
        get {
            options.screenViews
        }
        set(value) {
            options.screenViews = value
        }
    }
}
