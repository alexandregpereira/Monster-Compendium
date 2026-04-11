//
//  MiscellaneousExtension.swift
//  Amplitude-Swift
//
//  Created by Jin Xu on 3/18/25.
//

import Foundation

// Cached ISO8601DateFormatter for better performance on older OS versions
private let cachedISO8601Formatter: ISO8601DateFormatter = {
    let formatter = ISO8601DateFormatter()
    formatter.formatOptions.insert(.withFractionalSeconds)
    return formatter
}()

extension Date {
    func amp_timestamp() -> Int64 {
        return Int64(timeIntervalSince1970 * 1000)
    }

    func amp_iso8601String() -> String {
        if #available(iOS 15.0, macOS 12.0, watchOS 8.0, tvOS 15.0, *) {
            let iso8601Format = Date.ISO8601FormatStyle(includingFractionalSeconds: true)
            return self.formatted(iso8601Format)
        } else {
            // Use cached formatter for older OS versions
            return cachedISO8601Formatter.string(from: self)
        }
    }
}

#if !AMPLITUDE_DISABLE_UIKIT
extension CGRect {
    var amp_center: CGPoint {
        return CGPoint(x: midX, y: midY)
    }
}
#endif
