//
//  NetworkTrackingPlugin.swift
//  Amplitude-Swift
//
//  Created by Jin Xu on 3/17/25.
//

import Foundation
import ObjectiveC

private let SAFE_HEADERS: [String] = [
    "access-control-allow-origin",
    "access-control-allow-credentials",
    "access-control-expose-headers",
    "access-control-max-age",
    "access-control-allow-methods",
    "access-control-allow-headers",
    "accept-patch",
    "accept-ranges",
    "age",
    "allow",
    "alt-svc",
    "cache-control",
    "connection",
    "content-disposition",
    "content-encoding",
    "content-language",
    "content-length",
    "content-location",
    "content-md5",
    "content-range",
    "content-type",
    "date",
    "delta-base",
    "etag",
    "expires",
    "im",
    "last-modified",
    "link",
    "location",
    "permanent",
    "p3p",
    "pragma",
    "proxy-authenticate",
    "public-key-pins",
    "retry-after",
    "server",
    "status",
    "strict-transport-security",
    "trailer",
    "transfer-encoding",
    "tk",
    "upgrade",
    "vary",
    "via",
    "warning",
    "www-authenticate",
    "x-b3-traceid",
    "x-frame-options",
]

private let BLOCK_HEADERS = [
    "authorization",
    "cookie",
    "proxy-authorization"
]

public struct NetworkTrackingOptions {
    public enum URLPattern {
        case exact(String)
        case regex(String)
    }

    public struct CaptureHeader: Decodable {
        public let allowlist: [String]
        public let captureSafeHeaders: Bool

        public init(allowlist: [String] = [], captureSafeHeaders: Bool = true) {
            self.allowlist = allowlist
            self.captureSafeHeaders = captureSafeHeaders
        }
    }

    public struct CaptureBody: Decodable {
        public let allowlist: [String]
        public let excludelist: [String]

        public init(allowlist: [String], excludelist: [String] = []) {
            self.allowlist = allowlist
            self.excludelist = excludelist
        }

        @available(*, deprecated, renamed: "init(allowlist:excludelist:)", message: "Deprecated, use 'excludelist' instead")
        public init(allowlist: [String], blocklist: [String]) {
            self.allowlist = allowlist
            self.excludelist = blocklist
        }
    }

    public struct CaptureRule: Decodable {
        public var hosts: [String]
        public private(set) var urls: [URLPattern]
        public private(set) var methods: [String]
        public var statusCodeRange: String

        public let requestHeaders: CaptureHeader?
        public let responseHeaders: CaptureHeader?

        public let requestBody: CaptureBody?
        public let responseBody: CaptureBody?

        // Custom CodingKeys to handle the urls/urlsRegex split in JSON
        private enum CodingKeys: String, CodingKey {
            case hosts
            case urls
            case urlsRegex
            case methods
            case statusCodeRange
            case requestHeaders
            case responseHeaders
            case requestBody
            case responseBody
        }

        public init(from decoder: Decoder) throws {
            let container = try decoder.container(keyedBy: CodingKeys.self)

            self.hosts = try container.decodeIfPresent([String].self, forKey: .hosts) ?? []
            self.methods = try container.decodeIfPresent([String].self, forKey: .methods) ?? ["*"]
            self.statusCodeRange = try container.decodeIfPresent(String.self, forKey: .statusCodeRange) ?? "500-599"

            // Decode URLs - combine urls (exact) and urlsRegex (regex) into URLPattern array
            var urlPatterns: [URLPattern] = []
            if let exactUrls = try container.decodeIfPresent([String].self, forKey: .urls) {
                urlPatterns.append(contentsOf: exactUrls.map { .exact($0) })
            }
            if let regexUrls = try container.decodeIfPresent([String].self, forKey: .urlsRegex) {
                urlPatterns.append(contentsOf: regexUrls.map { .regex($0) })
            }
            self.urls = urlPatterns

            self.requestHeaders = try container.decodeIfPresent(CaptureHeader.self, forKey: .requestHeaders)
            self.responseHeaders = try container.decodeIfPresent(CaptureHeader.self, forKey: .responseHeaders)
            self.requestBody = try container.decodeIfPresent(CaptureBody.self, forKey: .requestBody)
            self.responseBody = try container.decodeIfPresent(CaptureBody.self, forKey: .responseBody)
        }

        public init(hosts: [String], statusCodeRange: String = "500-599") {
            self.hosts = hosts
            self.urls = []
            self.statusCodeRange = statusCodeRange
            self.methods = ["*"]
            self.requestHeaders = nil
            self.responseHeaders = nil
            self.requestBody = nil
            self.responseBody = nil
        }

        public init(urls: [URLPattern],
                    methods: [String] = ["*"],
                    statusCodeRange: String = "500-599",
                    requestHeaders: CaptureHeader? = nil,
                    responseHeaders: CaptureHeader? = nil,
                    requestBody: CaptureBody? = nil,
                    responseBody: CaptureBody? = nil

        ) {
            self.hosts = []
            self.urls = urls
            self.methods = methods
            self.statusCodeRange = statusCodeRange
            self.requestHeaders = requestHeaders
            self.responseHeaders = responseHeaders
            self.requestBody = requestBody
            self.responseBody = responseBody
        }

        private init(hosts: [String],
                     urls: [URLPattern],
                     methods: [String],
                     statusCodeRange: String,
                     requestHeaders: CaptureHeader?,
                     responseHeaders: CaptureHeader?,
                     requestBody: CaptureBody?,
                     responseBody: CaptureBody?
        ) {
            self.hosts = hosts
            self.urls = urls
            self.methods = methods
            self.statusCodeRange = statusCodeRange
            self.requestHeaders = requestHeaders
            self.responseHeaders = responseHeaders
            self.requestBody = requestBody
            self.responseBody = responseBody
        }

        static func fromRemoteConfig(_ configs: [[String: Any]]) -> [Self]? {
            guard !configs.isEmpty,
                  let data = try? JSONSerialization.data(withJSONObject: configs),
                  let rules = try? JSONDecoder().decode([CaptureRule].self, from: data) else {
                return nil
            }
            return rules
        }
    }

    public var captureRules: [CaptureRule]
    public var ignoreHosts: [String]
    public var ignoreAmplitudeRequests: Bool

    public init(captureRules: [CaptureRule], ignoreHosts: [String] = [], ignoreAmplitudeRequests: Bool = true) {
        self.captureRules = captureRules
        self.ignoreHosts = ignoreHosts
        self.ignoreAmplitudeRequests = ignoreAmplitudeRequests
    }

    public static var `default`: NetworkTrackingOptions {
        return NetworkTrackingOptions(captureRules: [CaptureRule(hosts: ["*"])])
    }
}

class NetworkTrackingPlugin: UtilityPlugin, NetworkTaskListener {

    @AtomicRef var options: CompiledNetworkTrackingOptions?
    @AtomicRef var optOut = true
    @AtomicRef var originalOptions: NetworkTrackingOptions?
    var ruleCache: [String: CompiledNetworkTrackingOptions.CaptureRule?] = [:]

    let networkTrackingQueue = DispatchQueue(label: "com.amplitude.analytics.networkTracking", attributes: .concurrent)

    private let ruleCacheLock: NSLock = NSLock()

    var logger: (any Logger)? {
        amplitude?.logger
    }

    override func setup(amplitude: Amplitude) {
        super.setup(amplitude: amplitude)

#if os(watchOS)
        logger?.warn(message: "NetworkTrackingPlugin is not supported on watchOS yet.")
        optOut = true
#else
        optOut = !amplitude.autocaptureManager.isEnabled(.networkTracking)
        originalOptions = amplitude.configuration.networkTrackingOptions

        if amplitude.configuration.enableAutoCaptureRemoteConfig {
            amplitude.autocaptureManager.onChange { [weak self, weak amplitude] config in
                guard let self, let amplitude else { return }

                optOut = !amplitude.autocaptureManager.isEnabled(.networkTracking)

                if let config {
                    updateConfig(config)
                } else if let originalOptions {
                    compileConfig(originalOptions)
                }
            }
        } else {
            if let originalOptions {
                compileConfig(originalOptions)
            }
        }
#endif
    }

    private func updateConfig(_ config: [String: Any]?) {
        guard var updatedOptions = originalOptions else {
            return
        }

        let options = config?["networkTracking"] as? [String: Any]
        if let ignoreHosts = options?["ignoreHosts"] as? [String] {
            updatedOptions.ignoreHosts = ignoreHosts
        }
        if let ignoreAmplitudeRequests = options?["ignoreAmplitudeRequests"] as? Bool {
            updatedOptions.ignoreAmplitudeRequests = ignoreAmplitudeRequests
        }
        if let captureRules = options?["captureRules"] as? [[String: Any]],
           let rules = NetworkTrackingOptions.CaptureRule.fromRemoteConfig(captureRules) {
            updatedOptions.captureRules = rules
        }

        originalOptions = updatedOptions
        compileConfig(updatedOptions)
    }

    func compileConfig(_ originalOptions: NetworkTrackingOptions) {
        guard let originalOptions = self.originalOptions else { return }

        do {
            options = try CompiledNetworkTrackingOptions(options: originalOptions)

            ruleCacheLock.withLock {
                ruleCache.removeAll()
            }

            if optOut {
                NetworkSwizzler.shared.removeListener(self)
            } else {
                NetworkSwizzler.shared.addListener(self)
            }
        } catch {
            logger?.error(message: "NetworkTrackingPlugin: Failed to parse options: \(originalOptions), error: \(error.localizedDescription)")
            optOut = true
            NetworkSwizzler.shared.removeListener(self)
        }
    }

    override func teardown() {
        super.teardown()
        NetworkSwizzler.shared.removeListener(self)
    }

    func ruleForRequest(_ request: URLRequest) -> CompiledNetworkTrackingOptions.CaptureRule? {
        guard let options = options,
              let url = request.url else { return nil }

        let host = url.host
        let urlString = url.absoluteString
        let method = request.httpMethod

        // Check ignore hosts first
        if let host = host, options.ignoreHosts.matches(host) {
            return nil
        }

        // Create cache key combining host, url, and method
        let cacheKey = "\(urlString)||\(method ?? "")"

        ruleCacheLock.lock()
        defer { ruleCacheLock.unlock() }

        if let rule = ruleCache[cacheKey] {
            return rule
        }

        // Find matching rule
        let rule = options.captureRules.last { rule in
            rule.matchesRequest(host: host, url: urlString, method: method)
        }

        ruleCache[cacheKey] = rule
        return rule
    }

    func onTaskResume(_ task: URLSessionTask) {
        guard isListening(task),
              task.state != .completed,
              task.state != .canceling,
              let request = task.originalRequest,
              let url = request.url,
              ruleForRequest(request) != nil
        else { return }

        logger?.debug(message: "NetworkTrackingPlugin: onTaskResume(\(task)) for \(url)")

        if task.amp_requestTimestamp == nil {
            task.amp_requestTimestamp = Int64(NSDate().timeIntervalSince1970 * 1000)
        }
    }

    func onTask(_ task: URLSessionTask, setState state: URLSessionTask.State) {
        guard isListening(task),
              let request = task.originalRequest,
              let url: URL = request.url,
              let rule = ruleForRequest(request) else { return }

        logger?.debug(message: "NetworkTrackingPlugin: setState: \(state) for \(url)")

        let response = task.response as? HTTPURLResponse
        let statusCode = response?.statusCode ?? 0

        guard rule.statusCodeIndexSet.contains(statusCode) else { return }

        guard let method = request.httpMethod else { return }

        guard task.state == .running, state == .completed else { return }

        let responseTimestamp = Int64(NSDate().timeIntervalSince1970 * 1000)

        let sendEventAction = { [self] in
            let requestHeaders = rule.requestHeaders?.filterHeaders(request.allHTTPHeaderFields)
            let responseHeaders = rule.responseHeaders?.filterHeaders(response?.allHeaderFields as? [String: String])
            let requestBody = rule.requestBody?.filterBodyData(request.httpBody)

            var responseBody: String?
            if let dataTask = task as? URLSessionDataTask,
               let responseData = dataTask.amp_responseData {
                responseBody = rule.responseBody?.filterBodyData(responseData)
            }

            let event = NetworkRequestEvent(url: url,
                                            method: method,
                                            statusCode: response?.statusCode,
                                            error: task.error as? NSError,
                                            startTime: task.amp_requestTimestamp,
                                            completionTime: responseTimestamp,
                                            requestBodySize: task.countOfBytesSent,
                                            responseBodySize: task.countOfBytesReceived,
                                            requestHeaders: requestHeaders,
                                            responseHeaders: responseHeaders,
                                            requestBody: requestBody,
                                            responseBody: responseBody
            )
            amplitude?.track(event: event)
        }

        if rule.responseBody != nil {
            // onDataTaskCompletion is called earlier than onTask, add delay to make response body capture more stable
            networkTrackingQueue.asyncAfter(deadline: .now() + 0.1, execute: sendEventAction)
        } else {
            networkTrackingQueue.async(execute: sendEventAction)
        }
    }

    func onDataTaskCompletion(_ task: URLSessionDataTask, data: Data?, response: URLResponse?, error: Error?) {
        guard isListening(task),
              let request = task.originalRequest,
              ruleForRequest(request)?.responseBody != nil,
              let data = data
        else { return }

        task.amp_responseData = data
    }

    func isListening(_ task: URLSessionTask) -> Bool {
        return !optOut &&
        (task is URLSessionDataTask || task is URLSessionUploadTask || task is URLSessionDownloadTask)
    }
}

// Key for associated object
private var sendTimeKey: UInt8 = 0
private var responseDataKey: UInt8 = 0

extension URLSessionTask {
    // Associate send time with URLSessionTask
    var amp_requestTimestamp: Int64? {
        get {
            return objc_getAssociatedObject(self, &sendTimeKey) as? Int64
        }
        set {
            objc_setAssociatedObject(self, &sendTimeKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
}

extension URLSessionDataTask {
    // Associate response data with URLSessionDataTask
    var amp_responseData: Data? {
        get {
            return objc_getAssociatedObject(self, &responseDataKey) as? Data
        }
        set {
            objc_setAssociatedObject(self, &responseDataKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
}

class CompiledNetworkTrackingOptions {

    class WildcardHosts {
        let hostSet: Set<String>
        let hostPatterns: [NSRegularExpression]

        init(hosts: [String]) throws {
            var hostPatterns: [NSRegularExpression] = []
            var hostSet: Set<String> = []
            for host in hosts {
                if host.contains("*") {
                    let regexPattern = host
                        .replacingOccurrences(of: ".", with: "\\.")
                        .replacingOccurrences(of: "*", with: ".*")
                    hostPatterns.append(try NSRegularExpression(pattern: "^" + regexPattern + "$",
                                                                options: [.caseInsensitive]))
                } else {
                    hostSet.insert(host)
                }
            }
            self.hostPatterns = hostPatterns
            self.hostSet = hostSet
        }

        func matches(_ host: String) -> Bool {
            return hostSet.contains(host) || hostPatterns.contains { regex in
                regex.firstMatch(in: host, range: NSRange(location: 0, length: host.utf16.count)) != nil
            }
        }
    }

    class URLPatternMatcher {
        let plainPatterns: Set<String>
        let regexPatterns: [NSRegularExpression]

        init(patterns: [NetworkTrackingOptions.URLPattern]) throws {
            var plainPatterns = Set<String>()
            var regexPatterns: [NSRegularExpression] = []

            for pattern in patterns {
                switch pattern {
                case .exact(let url):
                    plainPatterns.insert(url)
                case .regex(let regexString):
                    let regex = try NSRegularExpression(pattern: regexString, options: [])
                    regexPatterns.append(regex)
                }
            }

            self.plainPatterns = plainPatterns
            self.regexPatterns = regexPatterns
        }

        func matches(_ urlString: String) -> Bool {
            if plainPatterns.contains(urlString) {
                return true
            }

            for regex in regexPatterns {
                let range = NSRange(location: 0, length: urlString.utf16.count)
                if regex.firstMatch(in: urlString, range: range) != nil {
                    return true
                }
            }

            return false
        }
    }

    class CompiledHeaders {
        let allowSet: Set<String>

        init(header: NetworkTrackingOptions.CaptureHeader) {
            var combinedSet = Set(header.allowlist.map { $0.lowercased() })

            if header.captureSafeHeaders {
                combinedSet.formUnion(SAFE_HEADERS)
            }

            combinedSet.subtract(BLOCK_HEADERS)

            self.allowSet = combinedSet
        }

        func filterHeaders(_ headers: [String: String]?) -> [String: String]? {
            guard let headers, !allowSet.isEmpty else {
                return nil
            }

            var filteredHeader: [String: String] = [:]

            for (key, value) in headers where allowSet.contains(key.lowercased()) {
                filteredHeader[key] = value
            }

            return filteredHeader.isEmpty ? nil : filteredHeader
        }
    }

    class CompiledBody {
        let objectFilter: ObjectFilter

        init(body: NetworkTrackingOptions.CaptureBody) {
            self.objectFilter = ObjectFilter(allowList: body.allowlist, blockList: body.excludelist)
        }

        func filterBody(_ body: Any) -> Any? {
            return objectFilter.filterd(body)
        }

        func filterBodyData(_ bodyData: Data?) -> String? {
            guard let bodyData = bodyData else { return nil }

            if let json = try? JSONSerialization.jsonObject(with: bodyData, options: []),
               let filterBody = filterBody(json),
               let jsonString = try? JSONSerialization.data(withJSONObject: filterBody, options: []) {
                return String(data: jsonString, encoding: .utf8)
            }

            return nil
        }
    }

    class CaptureRule: CustomDebugStringConvertible {
        let hosts: WildcardHosts?
        let urls: URLPatternMatcher?
        let methods: Set<String>
        let statusCodeIndexSet: IndexSet
        let requestHeaders: CompiledHeaders?
        let responseHeaders: CompiledHeaders?
        let requestBody: CompiledBody?
        let responseBody: CompiledBody?

        init(rule: NetworkTrackingOptions.CaptureRule) throws {
            self.hosts = rule.hosts.isEmpty ? nil : try WildcardHosts(hosts: rule.hosts)
            self.urls = rule.urls.isEmpty ? nil : try URLPatternMatcher(patterns: rule.urls)

            if rule.methods.contains("*") {
                self.methods = Set(["*"])
            } else {
                self.methods = Set(rule.methods.map { $0.uppercased() })
            }

            self.statusCodeIndexSet = try IndexSet(fromString: rule.statusCodeRange)
            self.requestHeaders = rule.requestHeaders.map { CompiledHeaders(header: $0) }
            self.responseHeaders = rule.responseHeaders.map { CompiledHeaders(header: $0) }

            self.requestBody = rule.requestBody.map { CompiledBody(body: $0) }
            self.responseBody = rule.responseBody.map { CompiledBody(body: $0) }
        }

        func matchesRequest(host: String?, url: String, method: String?) -> Bool {
            // If URLs are configured, only check the URLs
            if let urls = urls {
                if !urls.matches(url) {
                    return false
                }
            } else if let hosts = hosts, let host = host {
                if !hosts.matches(host) {
                    return false
                }
            } else {
                return false
            }

            // Check method matching
            if let method = method?.uppercased() {
                if !methods.contains("*") && !methods.contains(method) {
                    return false
                }
            }

            return true
        }

        var debugDescription: String {
            return """
            CaptureRule(
                hosts: \(hosts?.hostSet ?? []),
                urls: \(urls?.plainPatterns ?? []),
                methods: \(methods),
                statusCodeIndexSet: \(statusCodeIndexSet)
            )
            """
        }
    }

    let captureRules: [CaptureRule]
    let ignoreHosts: WildcardHosts

    init(options: NetworkTrackingOptions) throws {
        self.captureRules = try options.captureRules.map { try CaptureRule(rule: $0) }

        var ignoreHosts = options.ignoreHosts
        if options.ignoreAmplitudeRequests {
            ignoreHosts.append("*.amplitude.com")
        }
        self.ignoreHosts = try WildcardHosts(hosts: ignoreHosts)
    }
}

extension IndexSet {
    enum ParseError: Error { case invalidFormat }

    /// Creates an `IndexSet` from a string like `"0,200-299,413,500-599"`.
    ///
    /// Accepts:
    /// * 0 -> local errors
    /// * single integers  → `413`
    /// * closed ranges    → `200-299`
    /// * mixed, comma‑separated, with optional spaces
    ///
    /// Throws `ParseError.invalidFormat` if **anything** is malformed.
    init(fromString string: String) throws {
        self.init()

        let trimmed = string.trimmingCharacters(in: .whitespacesAndNewlines)

        for rawPart in trimmed.split(separator: ",") {
            let part = rawPart.trimmingCharacters(in: .whitespaces)
            guard !part.isEmpty else { throw ParseError.invalidFormat }

            if let dash = part.firstIndex(of: "-") {
                let loSub = part[..<dash]
                let hiSub = part[part.index(after: dash)...]

                guard let lo = Int(loSub),
                      let hi = Int(hiSub),
                      lo <= hi
                else { throw ParseError.invalidFormat }

                self.insert(integersIn: lo...hi)
            } else {
                guard let value = Int(part) else {
                    throw ParseError.invalidFormat
                }
                self.insert(value)
            }
        }

        if self.isEmpty {
            throw ParseError.invalidFormat
        }
    }
}
