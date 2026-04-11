//
//  ObjectFilter.swift
//  Amplitude-Swift
//
//  Created by Jin Xu on 8/27/25.
//

class ObjectFilter {
    typealias KeyPath = [String]

    private let allowKeyPaths: [KeyPath]
    private let blockKeyPaths: [KeyPath]

    init(allowList: [String] = [], blockList: [String] = []) {
        allowKeyPaths = allowList.compactMap { Self.parseKeyPath($0) }
        blockKeyPaths = blockList.compactMap { Self.parseKeyPath($0) }
    }

    private static func parseKeyPath(_ path: String) -> KeyPath? {
        guard !path.isEmpty else { return nil }
        let components = path.components(separatedBy: "/").filter { !$0.isEmpty }
        return components.isEmpty ? nil : components
    }

    func filterd(_ object: Any?) -> Any? {
        guard let object = object, !allowKeyPaths.isEmpty else { return nil }

        switch object {
        case let dict as [String: Any]:
            return filter(dict, at: [])
        case let array as [Any]:
            return filter(array, at: [])
        default:
            return nil
        }
    }

    private func filter(_ dict: [String: Any], at path: KeyPath) -> [String: Any]? {
        var result = [String: Any]()

        for (key, value) in dict {
            let newPath = path + [key]
            guard !isBlocked(newPath), canInclude(newPath) else { continue }

            if let filtered = processValue(value, at: newPath) {
                result[key] = filtered
            } else if shouldPreserveEmpty(newPath, value) {
                result[key] = emptyContainer(for: value)
            }
        }

        return result.isEmpty ? nil : result
    }

    private func filter(_ array: [Any], at path: KeyPath) -> [Any]? {
        var result = [Any]()

        for (index, value) in array.enumerated() {
            let newPath = path + [String(index)]
            guard !isBlocked(newPath), canInclude(newPath) else { continue }

            if let filtered = processValue(value, at: newPath) {
                result.append(filtered)
            } else if shouldPreserveEmpty(newPath, value) {
                result.append(emptyContainer(for: value))
            }
        }

        return result.isEmpty ? nil : result
    }

    private func processValue(_ value: Any, at path: KeyPath) -> Any? {
        guard value is [String: Any] || value is [Any] else {
            return isAllowed(path) ? value : nil
        }

        // Return empty containers that match patterns
        if isAllowed(path) {
            if let dict = value as? [String: Any], dict.isEmpty { return value }
            if let array = value as? [Any], array.isEmpty { return value }
        }

        // Recursively filter containers
        if let dict = value as? [String: Any] { return filter(dict, at: path) }
        if let array = value as? [Any] { return filter(array, at: path) }
        return nil
    }

    private func shouldPreserveEmpty(_ path: KeyPath, _ value: Any) -> Bool {
        guard value is [String: Any] || value is [Any] else { return false }

        // Pattern like "user/*" preserves "user/metadata" as empty
        return allowKeyPaths.contains { pattern in
            pattern.count == path.count &&
            pattern.last == "*" &&
            matches(Array(path.dropLast()), Array(pattern.dropLast()))
        }
    }

    private func emptyContainer(for value: Any) -> Any {
        return value is [Any] ? [Any]() : [String: Any]()
    }

    private func canInclude(_ path: KeyPath) -> Bool {
        return allowKeyPaths.contains { pattern in
            matches(path, pattern) || canMatchDescendants(path, pattern)
        }
    }

    private func canMatchDescendants(_ path: KeyPath, _ pattern: KeyPath) -> Bool {
        guard pattern.count > path.count else {
            for i in 0..<pattern.count {
                if pattern[i] == "**" { return true }
                if pattern[i] != path[i] && pattern[i] != "*" { return false }
            }
            return false
        }

        for i in 0..<path.count {
            if pattern[i] == "**" { return true }
            if pattern[i] != path[i] && pattern[i] != "*" { return false }
        }
        return true
    }

    private func isAllowed(_ path: KeyPath) -> Bool {
        return allowKeyPaths.contains { matches(path, $0) } && !isBlocked(path)
    }

    private func isBlocked(_ path: KeyPath) -> Bool {
        return blockKeyPaths.contains { matches(path, $0) }
    }

    func matches(_ path: KeyPath, _ pattern: KeyPath) -> Bool {
        if path.isEmpty && pattern.isEmpty { return true }
        if pattern == ["**"] { return true }
        return matchImpl(path, pattern, 0, 0)
    }

    private func matchImpl(_ path: KeyPath, _ pattern: KeyPath, _ pIdx: Int, _ patIdx: Int) -> Bool {
        if pIdx == path.count && patIdx == pattern.count { return true }
        if patIdx == pattern.count { return false }

        let current = pattern[patIdx]

        if current == "**" {
            if patIdx == pattern.count - 1 { return true }
            if matchImpl(path, pattern, pIdx, patIdx + 1) { return true }

            for i in pIdx..<path.count where matchImpl(path, pattern, i + 1, patIdx + 1) {
                return true
            }
            return false
        }

        if pIdx == path.count { return false }

        return (current == "*" || current == path[pIdx]) &&
        matchImpl(path, pattern, pIdx + 1, patIdx + 1)
    }
}
