//
//  Atomic.swift
//
//
//  Created by Marvin Liu on 11/29/22.
//

import Foundation

@available(*, deprecated, message: "Atomic struct is not thread-safe when used as a class property. Use NSLock or OSAllocatedUnfairLock (iOS 16+) instead.")
@propertyWrapper
public struct Atomic<T> {
    var value: T
    private let lock = NSLock()

    public init(wrappedValue value: T) {
        self.value = value
    }

    public var wrappedValue: T {
        get { return load() }
        set { store(newValue: newValue) }
    }

    func load() -> T {
        lock.lock()
        defer { lock.unlock() }
        return value
    }

    mutating func store(newValue: T) {
        lock.lock()
        defer { lock.unlock() }
        value = newValue
    }
}

@propertyWrapper
final class AtomicRef<T> {
    private var value: T
    private let lock = NSLock()

    init(wrappedValue value: T) {
        self.value = value
    }

    var wrappedValue: T {
        get { lock.withLock { value } }
        set { lock.withLock { value = newValue } }
    }

    func withLock<R>(_ body: (inout T) -> R) -> R {
        lock.withLock { body(&value) }
    }
}

extension NSLock {
    func synchronizedLazy<T>(_ storage: inout T?, initializer: () -> T) -> T {
        lock()
        defer { unlock() }
        if let existing = storage {
            return existing
        }
        let newValue = initializer()
        storage = newValue
        return newValue
    }
}
