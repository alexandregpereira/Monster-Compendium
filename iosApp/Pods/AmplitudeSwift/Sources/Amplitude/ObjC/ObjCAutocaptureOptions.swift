import Foundation

@objc(AMPAutocaptureOptions)
public final class ObjCAutocaptureOptions: NSObject, @unchecked Sendable {
    internal var _options: AutocaptureOptions

    public override init() {
        _options = AutocaptureOptions()
        super.init()
    }

    @objc
    public convenience init(optionsToUnion: [ObjCAutocaptureOptions]) {
        self.init()
        for option in optionsToUnion {
            formUnion(option)
        }
    }

    internal convenience init(options: AutocaptureOptions) {
        self.init()
        _options = options
    }

    internal var options: AutocaptureOptions {
        get {
            return _options
        }
        set {
            _options = newValue
        }
    }

    @objc
    public static let sessions = ObjCAutocaptureOptions(options: .sessions)

    @objc
    public static let appLifecycles = ObjCAutocaptureOptions(options: .appLifecycles)

    @objc
    public static let screenViews = ObjCAutocaptureOptions(options: .screenViews)

    @objc
    public static let elementInteractions = ObjCAutocaptureOptions(options: .elementInteractions)

    @objc
    public static let networkTracking = ObjCAutocaptureOptions(options: .networkTracking)

    @objc
    public static let frustrationInteractions = ObjCAutocaptureOptions(options: .frustrationInteractions)

    @objc
    public static let all: ObjCAutocaptureOptions = ObjCAutocaptureOptions(options: .all)

    @objc
    public static var `default`: ObjCAutocaptureOptions {
        return ObjCAutocaptureOptions(options: Configuration.Defaults.autocaptureOptions)
    }

    // MARK: NSObject

    public override var hash: Int {
        return _options.rawValue
    }

    public override func isEqual(_ object: Any?) -> Bool {
        guard let that = object as? ObjCAutocaptureOptions else {
            return false
        }
        return _options == that._options
    }

    // MARK: OptionSet-like behavior

    @objc
    public func formUnion(_ other: ObjCAutocaptureOptions) {
        _options.formUnion(other._options)
    }

    @objc
    public func formIntersection(_ other: ObjCAutocaptureOptions) {
        _options.formIntersection(other._options)
    }

    @objc
    public func formSymmetricDifference(_ other: ObjCAutocaptureOptions) {
        _options.formSymmetricDifference(other._options)
    }

    // MARK: Convenience methods for Objective-C

    @objc
    public func contains(_ option: ObjCAutocaptureOptions) -> Bool {
        return _options.contains(option._options)
    }

    @objc
    public func union(_ option: ObjCAutocaptureOptions) -> ObjCAutocaptureOptions {
        return ObjCAutocaptureOptions(options: _options.union(option._options))
    }

    @objc
    public func intersect(_ option: ObjCAutocaptureOptions) -> ObjCAutocaptureOptions {
        return ObjCAutocaptureOptions(options: _options.intersection(option._options))
    }
}
