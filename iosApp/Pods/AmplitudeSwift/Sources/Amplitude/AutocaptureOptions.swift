import Foundation

public struct AutocaptureOptions: OptionSet {
    public let rawValue: Int

    public init(rawValue: Int) {
        self.rawValue = rawValue
    }

    public static let sessions            = AutocaptureOptions(rawValue: 1 << 0)
    public static let appLifecycles       = AutocaptureOptions(rawValue: 1 << 1)
    public static let screenViews         = AutocaptureOptions(rawValue: 1 << 2)
    public static let elementInteractions = AutocaptureOptions(rawValue: 1 << 3)
    /// Won't work on watchOS
    public static let networkTracking     = AutocaptureOptions(rawValue: 1 << 4)
    /// Rage Click and Dead Click detection
    public static let frustrationInteractions = AutocaptureOptions(rawValue: 1 << 5)

    public static let all: AutocaptureOptions = [
        .sessions,
        .appLifecycles,
        .screenViews,
        .elementInteractions,
        .networkTracking,
        .frustrationInteractions,
    ]
}

extension AutocaptureOptions {
    func stringRepresentation() -> String {
        guard rawValue != 0 else { return "none" }

        var options: [String] = []

        if contains(.sessions) {
            options.append("sessions")
        }
        if contains(.appLifecycles) {
            options.append("appLifecycles")
        }
        if contains(.screenViews) {
            options.append("screenViews")
        }
        if contains(.elementInteractions) {
            options.append("elementInteractions")
        }
        if contains(.networkTracking) {
            options.append("networkTracking")
        }
        if contains(.frustrationInteractions) {
            options.append("frustrationInteractions")
        }

        return options.joined(separator: ",")
    }
}
