//
//  QueueTimer.swift
//
//
//  Created by Marvin Liu on 11/29/22.
//

import Foundation

internal class QueueTimer {
    enum State {
        case suspended
        case resumed
    }

    let interval: TimeInterval
    let timer: DispatchSourceTimer
    let queue: DispatchQueue
    let handler: () -> Void

    @AtomicRef var state: State = .suspended

    init(interval: TimeInterval, once: Bool = false, queue: DispatchQueue = .main, handler: @escaping () -> Void) {
        self.interval = interval
        self.queue = queue
        self.handler = handler

        timer = DispatchSource.makeTimerSource(flags: [], queue: queue)
        timer.schedule(deadline: .now() + self.interval, repeating: once ? .infinity : self.interval)
        timer.setEventHandler { [weak self] in
            self?.handler()
        }
        resume()
    }

    deinit {
        timer.setEventHandler {
            // do nothing ...
        }
        // if timer is suspended, we must resume if we're going to cancel.
        timer.cancel()
        resume()
    }

    func suspend() {
        if state == .suspended {
            return
        }
        state = .suspended
        timer.suspend()
    }

    func resume() {
        if state == .resumed {
            return
        }
        state = .resumed
        timer.resume()
    }
}

extension TimeInterval {
    static func milliseconds(_ value: Int) -> TimeInterval {
        return TimeInterval(Double(value) / 1000.0)
    }

    static func seconds(_ value: Int) -> TimeInterval {
        return TimeInterval(value)
    }

}
