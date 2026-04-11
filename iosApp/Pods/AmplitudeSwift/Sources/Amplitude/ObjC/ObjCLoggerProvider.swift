import Foundation

public typealias ObjCLoggerProvider = (Int, String) -> Void

@preconcurrency
class ObjCLoggerProviderWrapper: Logger, @unchecked Sendable {
    public typealias LogLevel = LogLevelEnum
    public var logLevel: Int

    private let logProvider: ObjCLoggerProvider

    init(
        logLevel: LogLevelEnum,
        logProvider: @escaping ObjCLoggerProvider
    ) {
        self.logLevel = logLevel.rawValue
        self.logProvider = logProvider
    }

    func error(message: String) {
        if logLevel >= LogLevelEnum.error.rawValue {
            logProvider(logLevel, message)
        }
    }

    func warn(message: String) {
        if logLevel >= LogLevelEnum.warn.rawValue {
            logProvider(logLevel, message)
        }
    }

    func log(message: String) {
        if logLevel >= LogLevelEnum.log.rawValue {
            logProvider(logLevel, message)
        }
    }

    func debug(message: String) {
        if logLevel >= LogLevelEnum.debug.rawValue {
            logProvider(logLevel, message)
        }
    }
}
