//
//  NetworkSwizzler.swift
//  Amplitude-Swift
//
//  Created by Jin Xu on 3/6/25.
//

import Foundation

typealias DataTaskCompletionHandler = (Data?, URLResponse?, Error?) -> Void
typealias DataTaskWithURLRequestEventHandler  = (URLSession) -> ((URLRequest, @escaping DataTaskCompletionHandler) -> URLSessionDataTask)
typealias DataTaskWithURLEventHandler  = (URLSession) -> ((URL, @escaping DataTaskCompletionHandler) -> URLSessionDataTask)

public protocol NetworkTaskListener: AnyObject {
    func onTaskResume(_ task: URLSessionTask)
    func onTask(_ task: URLSessionTask, setState state: URLSessionTask.State)
    func onDataTaskCompletion(_ task: URLSessionDataTask, data: Data?, response: URLResponse?, error: Error?)
}

class NetworkSwizzler {

    static let shared = NetworkSwizzler()

    private var listeners: [NetworkTaskListener] = []
    private var swizzled: Bool = false
    private let lock = NSLock()

    func swizzle() {
        lock.lock()
        defer { lock.unlock() }

        guard !swizzled else { return }
        swizzled = true

        MethodSwizzler.swizzleInstanceMethod(
            for: URLSessionTask.self,
            originalSelector: #selector(URLSessionTask.resume),
            swizzledSelector: #selector(URLSessionTask.amp_resume)
        )
        MethodSwizzler.swizzleInstanceMethod(
            for: URLSessionTask.self,
            originalSelector: NSSelectorFromString("setState:"),
            swizzledSelector: #selector(URLSessionTask.amp_setState)
        )

        MethodSwizzler.swizzleInstanceMethod(
            for: URLSession.self,
            originalSelector: #selector(URLSession.dataTask(with:completionHandler:) as DataTaskWithURLRequestEventHandler),
            swizzledSelector: #selector(URLSession.amp_dataTaskWithRequest(_:completionHandler:))
        )
        MethodSwizzler.swizzleInstanceMethod(
            for: URLSession.self,
            originalSelector: #selector(URLSession.dataTask(with:completionHandler:) as DataTaskWithURLEventHandler),
            swizzledSelector: #selector(URLSession.amp_dataTaskWithURL(_:completionHandler:))
        )
    }

    func unswizzle() {
        lock.lock()
        defer { lock.unlock() }

        guard swizzled else { return }
        swizzled = false

        MethodSwizzler.unswizzleInstanceMethod(
            for: URLSessionTask.self,
            originalSelector: #selector(URLSessionTask.resume),
            swizzledSelector: #selector(URLSessionTask.amp_resume)
        )
        MethodSwizzler.unswizzleInstanceMethod(
            for: URLSessionTask.self,
            originalSelector: NSSelectorFromString("setState:"),
            swizzledSelector: #selector(URLSessionTask.amp_setState)
        )

        MethodSwizzler.unswizzleInstanceMethod(
            for: URLSession.self,
            originalSelector: #selector(URLSession.dataTask(with:completionHandler:) as DataTaskWithURLRequestEventHandler),
            swizzledSelector: #selector(URLSession.amp_dataTaskWithRequest(_:completionHandler:))
        )
        MethodSwizzler.unswizzleInstanceMethod(
            for: URLSession.self,
            originalSelector: #selector(URLSession.dataTask(with:completionHandler:) as DataTaskWithURLEventHandler),
            swizzledSelector: #selector(URLSession.amp_dataTaskWithURL(_:completionHandler:))
        )
    }

    func addListener(_ listener: NetworkTaskListener) {
        swizzle()
        lock.withLock {
            listeners.append(listener)
        }
    }

    func removeListener(_ listener: NetworkTaskListener) {
        lock.withLock {
            listeners.removeAll { $0 === listener }
        }
    }

    fileprivate func onTaskResume(task: URLSessionTask) {
        let listeners = lock.withLock { return self.listeners }
        for listener in listeners {
            listener.onTaskResume(task)
        }
    }

    fileprivate func onTaskSetState(task: URLSessionTask, state: URLSessionTask.State) {
        let listeners = lock.withLock { return self.listeners }
        for listener in listeners {
            listener.onTask(task, setState: state)
        }
    }

    fileprivate func onDataTaskCompletion(task: URLSessionDataTask, data: Data?, response: URLResponse?, error: Error?) {
        let listeners = lock.withLock { return self.listeners }
        for listener in listeners {
            listener.onDataTaskCompletion(task, data: data, response: response, error: error)
        }
    }
}

extension URLSessionTask {
    @objc fileprivate func amp_resume() {
        NetworkSwizzler.shared.onTaskResume(task: self)
        amp_resume()
    }

    @objc fileprivate func amp_setState(_ state: URLSessionTask.State) {
        NetworkSwizzler.shared.onTaskSetState(task: self, state: state)
        amp_setState(state)
    }
}

extension URLSession {
    @objc fileprivate func amp_dataTaskWithRequest(_ request: URLRequest, completionHandler: @escaping (Data?, URLResponse?, Error?) -> Void) -> URLSessionDataTask {
        var capturedTask: URLSessionDataTask?

        let wrappedHandler: DataTaskCompletionHandler = { data, response, error in
            if let task = capturedTask {
                NetworkSwizzler.shared.onDataTaskCompletion(task: task, data: data, response: response, error: error)
            }
            completionHandler(data, response, error)
        }

        let task = amp_dataTaskWithRequest(request, completionHandler: wrappedHandler)
        capturedTask = task

        return task
    }

    @objc fileprivate func amp_dataTaskWithURL(_ url: URL, completionHandler: @escaping (Data?, URLResponse?, Error?) -> Void) -> URLSessionDataTask {
        var capturedTask: URLSessionDataTask?

        let wrappedHandler: DataTaskCompletionHandler = { data, response, error in
            if let task = capturedTask {
                NetworkSwizzler.shared.onDataTaskCompletion(task: task, data: data, response: response, error: error)
            }
            completionHandler(data, response, error)
        }

        let task = amp_dataTaskWithURL(url, completionHandler: wrappedHandler)
        capturedTask = task

        return task
    }
}
