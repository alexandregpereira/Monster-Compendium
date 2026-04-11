//
//  Diagonostics.swift
//  Amplitude-Swift
//
//  Created by Qingzhuo Zhen on 3/4/24.
//

import Foundation

public class Diagnostics {
    private static let MAX_ERROR_LOGS = 10

    private var malformedEvents: [String] = []
    private var errorLogs = NSMutableOrderedSet(capacity: 10)

    private let lock = NSLock()

    func addMalformedEvent(_ event: String) {
        lock.lock()
        defer { lock.unlock() }

        malformedEvents.append(event)
    }

    func addErrorLog(_ log: String) {
        lock.lock()
        defer { lock.unlock() }

        errorLogs.add(log)

        // trim to MAX_ERROR_LOGS elements
        while errorLogs.count > Self.MAX_ERROR_LOGS {
            errorLogs.removeObject(at: 0)
        }
    }

    /**
     * Extracts the diagnostics as a JSON string.
     * Warning: This will clear stored diagnostics.
     * @return JSON string of diagnostics or empty if no diagnostics are present.
     */
    func extractDiagnosticsToString() -> String? {
        lock.lock()
        defer { lock.unlock() }

        guard !malformedEvents.isEmpty || errorLogs.count > 0 else { return nil }

        var diagnostics = [String: [String]]()
        if !malformedEvents.isEmpty {
            diagnostics["malformed_events"] = malformedEvents
        }
        if errorLogs.count > 0, let errorStrings = errorLogs.array as? [String] {
            diagnostics["error_logs"] = errorStrings
        }
        do {
            let data = try JSONSerialization.data(withJSONObject: diagnostics, options: [])
            malformedEvents.removeAll()
            errorLogs.removeAllObjects()
            return String(data: data, encoding: .utf8)
        } catch {
            return nil
        }
    }
}
