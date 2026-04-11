//
//  EventPipeline.swift
//
//
//  Created by Marvin Liu on 10/28/22.
//

import Foundation

/// Retry interval capped at 1 minutes
private let MAX_RETRY_INTERVAL: Double = 60

public class EventPipeline {
    var httpClient: HttpClient
    let storage: Storage?
    let logger: (any Logger)?
    let configuration: Configuration

    @AtomicRef internal var eventCount: Int = 0
    internal var flushTimer: QueueTimer?
    private let uploadsQueue = DispatchQueue(label: "uploadsQueue.amplitude.com")

    private var flushCompletions: [() -> Void] = []
    private var currentUpload: URLSessionTask?

    init(amplitude: Amplitude) {
        storage = amplitude.storage
        logger = amplitude.logger
        configuration = amplitude.configuration
        httpClient = HttpClient(configuration: amplitude.configuration,
                                diagnostics: amplitude.configuration.diagonostics,
                                callbackQueue: amplitude.trackingQueue)
        flushTimer = QueueTimer(interval: getFlushInterval(), queue: amplitude.trackingQueue) { [weak self] in
            self?.flush()
        }
    }

    func put(event: BaseEvent, completion: (() -> Void)? = nil) {
        guard let storage = self.storage else { return }
        event.attempts += 1
        do {
            try storage.write(key: StorageKey.EVENTS, value: event)
            _eventCount.withLock { $0 += 1 }
            if eventCount >= getFlushCount() {
                flush()
            }
            completion?()
        } catch {
            logger?.error(message: "Error when storing event: \(error.localizedDescription)")
        }
    }

    func flush(completion: (() -> Void)? = nil) {
        if configuration.offline == true {
            logger?.debug(message: "Skipping flush while offline.")
            return
        }

        logger?.log(message: "Start flushing \(eventCount) events")
        eventCount = 0
        guard let storage = self.storage else { return }
        storage.rollover()

        uploadsQueue.async { [self] in
            if let completion {
                flushCompletions.append(completion)
            }
            self.sendNextEventFile()
        }
    }

    private func sendNextEventFile(failures: Int = 0) {
        autoreleasepool {
            guard currentUpload == nil else {
                logger?.log(message: "Existing upload in progress, skipping...")
                return
            }

            guard let storage = storage,
                  let eventFiles: [URL] = storage.read(key: StorageKey.EVENTS),
                  let nextEventFile = eventFiles.first else {
                flushCompletions.forEach { $0() }
                flushCompletions.removeAll()
                logger?.debug(message: "No event files to upload")
                return
            }

            guard configuration.offline != true else {
                logger?.debug(message: "Skipping flush while offline.")
                return
            }

            guard let eventsString = storage.getEventsString(eventBlock: nextEventFile),
                  !eventsString.isEmpty else {
                logger?.log(message: "Could not read events file: \(nextEventFile)")
                return
            }

            currentUpload = httpClient.upload(events: eventsString) { [self] result in
                let responseHandler = storage.getResponseHandler(
                    configuration: self.configuration,
                    eventPipeline: self,
                    eventBlock: nextEventFile,
                    eventsString: eventsString
                )
                let handled: Bool = responseHandler.handle(result: result)
                var failures = failures

                switch result {
                case .success:
                    failures = 0
                case .failure:
                    if !handled {
                        failures += 1
                    }
                }

                if failures > self.configuration.flushMaxRetries {
                    self.uploadsQueue.async {
                        self.currentUpload = nil
                    }
                    self.configuration.offline = true
                    self.logger?.error(message: "Request failed more than \(self.configuration.flushMaxRetries) times, marking offline")
                } else {
                    // Don't send the next event file if we're being deallocated
                    let nextFileBlock: () -> Void = { [weak self] in
                        guard let self = self else {
                            return
                        }
                        self.currentUpload = nil
                        self.sendNextEventFile(failures: failures)
                    }

                    if failures == 0 || handled {
                        self.uploadsQueue.async(execute: nextFileBlock)
                    } else {
                        let sendingInterval = min(MAX_RETRY_INTERVAL, pow(2, Double(failures - 1)))
                        self.uploadsQueue.asyncAfter(deadline: .now() + sendingInterval, execute: nextFileBlock)
                        self.logger?.error(message: "Request failed \(failures) times, send next event file in \(sendingInterval) seconds")
                    }
                }
            }
        }
    }

    func start() {
        flushTimer?.resume()
    }

    func stop() {
        flushTimer?.suspend()
    }

    private func getFlushInterval() -> TimeInterval {
        return TimeInterval.milliseconds(configuration.flushIntervalMillis)
    }

    private func getFlushCount() -> Int {
        let count = configuration.flushQueueSize
        return count != 0 ? count : 1
    }
}
