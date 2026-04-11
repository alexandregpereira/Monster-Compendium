//
//  HttpClient.swift
//
//
//  Created by Marvin Liu on 10/28/22.
//

import Foundation

class HttpClient {
    let configuration: Configuration
    let session: URLSession
    let logger: (any Logger)?
    let diagnostics: Diagnostics
    let callbackQueue: DispatchQueue

    private lazy var dateFormatter: ISO8601DateFormatter = {
        let formatter = ISO8601DateFormatter()
        formatter.formatOptions.insert(.withFractionalSeconds)
        return formatter
    }()

    init(configuration: Configuration, diagnostics: Diagnostics, callbackQueue: DispatchQueue? = nil) {
        self.configuration = configuration
        self.diagnostics = diagnostics
        self.logger = configuration.loggerProvider
        self.callbackQueue = callbackQueue ?? .global()

        let sessionConfiguration = URLSessionConfiguration.default
        sessionConfiguration.httpMaximumConnectionsPerHost = 2
        sessionConfiguration.urlCache = nil
        self.session = URLSession(configuration: sessionConfiguration, delegate: nil, delegateQueue: nil)
    }

    func upload(events: String, completion: @escaping (_ result: Result<Int, Error>) -> Void) -> URLSessionDataTask? {
        var sessionTask: URLSessionDataTask?
        let backgroundTaskCompletion = VendorSystem.current.beginBackgroundTask()
        do {
            let (requestData, isGzipEncoded) = getRequestData(events: events)
            let request = try getRequest(isGzipEncoded: isGzipEncoded)
            sessionTask = session.uploadTask(with: request, from: requestData) { [callbackQueue, configuration, logger] data, response, error in
                callbackQueue.async {
                    if let error = error {
                        let nsError = error as NSError
                        if nsError.domain == NSURLErrorDomain {
                            switch nsError.code {
                            case NSURLErrorCannotConnectToHost, NSURLErrorNetworkConnectionLost, NSURLErrorCannotFindHost, NSURLErrorAppTransportSecurityRequiresSecureConnection, NSURLErrorNotConnectedToInternet, NSURLErrorBadURL:
                                logger?.error(message: "Conection failed with error: \(error.localizedDescription), marking offline")
                                configuration.offline = true
                            default:
                                logger?.error(message: "Request failed with error: \(error.localizedDescription)")
                            }
                        }
                        completion(.failure(error))
                    } else if let httpResponse = response as? HTTPURLResponse {
                        switch httpResponse.statusCode {
                        case 1..<300:
                            completion(.success(httpResponse.statusCode))
                            logger?.debug(message: "Successfully completed request")
                        default:
                            let response = String(data: data ?? Data(), encoding: .utf8) ?? ""
                            logger?.error(message: "Response failed with code\(httpResponse.statusCode): \(response)")
                            completion(.failure(Exception.httpError(code: httpResponse.statusCode, data: data)))
                        }
                    }
                    backgroundTaskCompletion?()
                }
            }
            sessionTask!.resume()
        } catch {
            logger?.debug(message: "Failed to construct request")
            completion(.failure(Exception.httpError(code: 500, data: nil)))
            backgroundTaskCompletion?()
        }
        return sessionTask
    }

    func getUrl() -> String {
        if let url = configuration.serverUrl, !url.isEmpty {
            return url
        }
        if configuration.serverZone == ServerZone.EU {
            return configuration.useBatch ? Constants.EU_BATCH_API_HOST : Constants.EU_DEFAULT_API_HOST
        }
        return configuration.useBatch ? Constants.BATCH_API_HOST : Constants.DEFAULT_API_HOST
    }

    func getRequest(isGzipEncoded: Bool) throws -> URLRequest {
        let url = getUrl()

        let requestUrl: URL?
#if compiler(>=5.9)
        if #available(macOS 14.0, iOS 17.0, watchOS 10.0, tvOS 17.0, *) {
            requestUrl = URL(string: url, encodingInvalidCharacters: false)
        } else {
            requestUrl = URL(string: url)
        }
#else
        requestUrl = URL(string: url)
#endif

        guard let requestUrl else {
            throw Exception.invalidUrl(url: url)
        }
        var request = URLRequest(url: requestUrl, timeoutInterval: 60)
        request.httpMethod = "POST"
        request.addValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.addValue("application/json", forHTTPHeaderField: "Accept")
        if isGzipEncoded {
            request.setValue("gzip", forHTTPHeaderField: "Content-Encoding")
        }
        return request
    }

    func getRequestData(events: String) -> (data: Data?, isGzipEncoded: Bool) {
        let apiKey = configuration.apiKey
        let clientUploadTime: String = dateFormatter.string(from: getDate())
        var requestPayload = """
            {"api_key":"\(apiKey)","client_upload_time":"\(clientUploadTime)","events":\(events)
            """
        if let minIdLength = configuration.minIdLength {
            requestPayload += """
                ,"options":{"min_id_length":\(minIdLength)}
                """
        }
        if let diagnosticsInfo = diagnostics.extractDiagnosticsToString(), !diagnosticsInfo.isEmpty {
            requestPayload += """
                ,"request_metadata":{"sdk":\(diagnosticsInfo)}
                """
        }
        requestPayload += "}"
        guard let data = requestPayload.data(using: .utf8) else {
            return (nil, false)
        }

        if shouldCompressUploadBody {
            do {
                return (try data.gzipped(), !data.isEmpty)
            } catch {
                logger?.error(message: "Error compressing request body: \(error)")
                return (data, false)
            }
        } else {
            return (data, false)
        }
    }

    var shouldCompressUploadBody: Bool {
        // Custom servers may not support gzip compression- only enable it if opted in.
        if let serverUrl = configuration.serverUrl, !serverUrl.isEmpty {
            return configuration.enableRequestBodyCompression
        } else {
            return true
        }
    }

    func getDate() -> Date {
        return Date()
    }
}

extension HttpClient {
    enum HttpStatus: Int {
        case SUCCESS = 200
        case BAD_REQUEST = 400
        case TIMEOUT = 408
        case PAYLOAD_TOO_LARGE = 413
        case TOO_MANY_REQUESTS = 429
        case FAILED = 500
    }

    enum Exception: Error {
        case invalidUrl(url: String)
        case httpError(code: Int, data: Data?)
    }
}
