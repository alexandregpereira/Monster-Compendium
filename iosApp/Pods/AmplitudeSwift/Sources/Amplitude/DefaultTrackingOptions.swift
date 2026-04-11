import Foundation

protocol DefaultTrackingOptionsDelegate: AnyObject {
    @available(*, deprecated)
    func didChangeOptions(options: DefaultTrackingOptions)
}

@available(*, deprecated, renamed: "AutocaptureOptions", message: "Please use `AutocaptureOptions` instead")
public class DefaultTrackingOptions {
    public static var ALL: DefaultTrackingOptions {
        DefaultTrackingOptions(sessions: true, appLifecycles: true, screenViews: true)
    }
    public static var NONE: DefaultTrackingOptions {
        DefaultTrackingOptions(sessions: false, appLifecycles: false, screenViews: false)
    }

    public var sessions: Bool {
        didSet {
            delegate?.didChangeOptions(options: self)
        }
    }

    public var appLifecycles: Bool {
        didSet {
            delegate?.didChangeOptions(options: self)
        }
    }

    public var screenViews: Bool {
        didSet {
            delegate?.didChangeOptions(options: self)
        }
    }

    weak var delegate: DefaultTrackingOptionsDelegate?

    var autocaptureOptions: AutocaptureOptions {
        return [
            sessions ? .sessions : [],
            appLifecycles ? .appLifecycles : [],
            screenViews ? .screenViews : []
        ].reduce(into: []) { $0.formUnion($1) }
    }

    public init(
        sessions: Bool = true,
        appLifecycles: Bool = false,
        screenViews: Bool = false
    ) {
        self.sessions = sessions
        self.appLifecycles = appLifecycles
        self.screenViews = screenViews
    }

    convenience init(delegate: DefaultTrackingOptionsDelegate) {
        self.init()
        self.delegate = delegate
    }
}
