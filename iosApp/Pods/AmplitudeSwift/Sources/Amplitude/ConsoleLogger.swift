//
//  ConsoleLogger.swift
//
//
//  Created by Marvin Liu on 10/28/22.
//

import Foundation
import os.log

@preconcurrency
public class ConsoleLogger: Logger, @unchecked Sendable {
    public typealias LogLevel = LogLevelEnum

    public var logLevel: Int
    private var logger: OSLog

    public init(logLevel: Int = LogLevelEnum.off.rawValue) {
        self.logLevel = logLevel
        self.logger = OSLog(subsystem: "Amplitude", category: "Logging")
    }

    public func error(message: String) {
        if logLevel >= LogLevel.error.rawValue {
            os_log("Error: %@", log: logger, type: .error, message)
        }
    }

    public func warn(message: String) {
        if logLevel >= LogLevel.warn.rawValue {
            os_log("Warn: %@", log: logger, type: .default, message)
        }
    }

    public func log(message: String) {
        if logLevel >= LogLevel.log.rawValue {
            os_log("Log: %@", log: logger, type: .info, message)
        }
    }

    public func debug(message: String) {
        if logLevel >= LogLevel.debug.rawValue {
            os_log("Debug: %@", log: logger, type: .debug, message)
        }
    }
}
