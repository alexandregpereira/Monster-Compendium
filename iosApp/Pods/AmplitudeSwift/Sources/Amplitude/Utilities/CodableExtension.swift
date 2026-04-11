//
//  CodableExtension.swift
//
//
//  Created by Marvin Liu on 11/23/22.
//  This file extends the current Codable to support custom JSON, like [String: Any].

import Foundation

struct JSONCodingKeys: CodingKey {
    var stringValue: String
    var intValue: Int?

    // Non-failable variant
    init(_ stringValue: String) {
        self.stringValue = stringValue
    }

    init?(stringValue: String) {
        self.stringValue = stringValue
    }

    init?(intValue: Int) {
        self.init(stringValue: "\(intValue)")
        self.intValue = intValue
    }
}

extension KeyedDecodingContainer {

    func decode(_ type: [String: Any].Type, forKey key: K) throws -> [String: Any] {
        let container = try nestedContainer(keyedBy: JSONCodingKeys.self, forKey: key)
        return try container.asTypedDictionary(type)
    }

    func decodeIfPresent(_ type: [String: Any].Type, forKey key: K) throws -> [String: Any]? {
        guard contains(key) else {
            return nil
        }
        guard try decodeNil(forKey: key) == false else {
            return nil
        }
        return try decode(type, forKey: key)
    }

    func decode(_ type: [Any].Type, forKey key: K) throws -> [Any] {
        var container = try nestedUnkeyedContainer(forKey: key)
        return try container.asTypedArray(type)
    }

    func decodeIfPresent(_ type: [Any].Type, forKey key: K) throws -> [Any]? {
        guard contains(key) else {
            return nil
        }
        guard try decodeNil(forKey: key) == false else {
            return nil
        }
        return try decode(type, forKey: key)
    }

    func asTypedDictionary(_ type: [String: Any].Type) throws -> [String: Any] {
        var dictionary = [String: Any]()
        for key in allKeys {
            if let boolValue = try? decode(Bool.self, forKey: key) {
                dictionary[key.stringValue] = boolValue
            } else if let stringValue = try? decode(String.self, forKey: key) {
                dictionary[key.stringValue] = stringValue
            } else if let intValue = try? decode(Int.self, forKey: key) {
                dictionary[key.stringValue] = intValue
            } else if let doubleValue = try? decode(Double.self, forKey: key) {
                dictionary[key.stringValue] = doubleValue
            } else if let nestedDictionary = try? decode(Dictionary<String, Any>.self, forKey: key) {
                dictionary[key.stringValue] = nestedDictionary
            } else if let nestedArray = try? decode(Array<Any>.self, forKey: key) {
                dictionary[key.stringValue] = nestedArray
            }
        }
        return dictionary
    }
}

extension UnkeyedDecodingContainer {

    mutating func asTypedArray(_ type: [Any].Type) throws -> [Any] {
        var array: [Any] = []
        while isAtEnd == false {
            // See if the current value in the JSON array is `null` first
            // and prevent infite recursion with nested arrays.
            if try decodeNil() {
                continue
            } else if let value = try? decode(Bool.self) {
                array.append(value)
            } else if let value = try? decode(Double.self) {
                array.append(value)
            } else if let value = try? decode(String.self) {
                array.append(value)
            } else if let nestedDictionary = try? decode(Dictionary<String, Any>.self) {
                array.append(nestedDictionary)
            } else if var nestedUnkeyedContainer = try? nestedUnkeyedContainer(),
                      let nestedArray = try? nestedUnkeyedContainer.asTypedArray([Any].self) {
                array.append(nestedArray)
            }
        }
        return array
    }

    mutating func decode(_ type: [String: Any].Type) throws -> [String: Any] {
        let nestedContainer = try self.nestedContainer(keyedBy: JSONCodingKeys.self)
        return try nestedContainer.asTypedDictionary(type)
    }
}

extension UnkeyedEncodingContainer {

    mutating func encodeAny(_ value: Any?) throws {
        guard let value = value else {
            return
        }

        // NSNumber bridges into many different types - try to extract the original.
        // Note that for swift numeric types, this actually does a double conversion (to NSNumber first).
        let encodedValue: Any
        if let numberValue = value as? NSNumber, let swiftValue = numberValue.swiftValue {
            encodedValue = swiftValue
        } else {
            encodedValue = value
        }

        // Based on https://github.com/apple/swift-corelibs-foundation/blob/ca3669eb9ac282c649e71824d9357dbe140c8251/Sources/Foundation/JSONSerialization.swift#L397
        switch encodedValue {
        case let encodable as Encodable:
            try encode(encodable)
        case let str as String:
            try encode(str)
        case let boolValue as Bool:
            try encode(boolValue)
        case let num as Int:
            try encode(num)
        case let num as Int8:
            try encode(num)
        case let num as Int16:
            try encode(num)
        case let num as Int32:
            try encode(num)
        case let num as Int64:
            try encode(num)
        case let num as UInt:
            try encode(num)
        case let num as UInt8:
            try encode(num)
        case let num as UInt16:
            try encode(num)
        case let num as UInt32:
            try encode(num)
        case let num as UInt64:
            try encode(num)
        case let array as [Any?]:
            var container = nestedUnkeyedContainer()
            for element in array {
                try container.encodeAny(element)
            }
        case let dict as [AnyHashable: Any?]:
            var container = nestedContainer(keyedBy: JSONCodingKeys.self)
            for (key, value) in dict {
                try container.encodeAny(value, forKey: JSONCodingKeys(String(describing: key)))
            }
        case let num as Float:
            try encode(num)
        case let num as Double:
            try encode(num)
        case let num as Decimal:
            try encode(num)
        case let num as NSDecimalNumber:
            try encode(num.decimalValue)
        case is NSNull:
            try encodeNil()
        default:
            // Ideally we would throw an error here, but we still want to complete the encode to maintain
            // backwards compatibility.
            try encode("[Non-Encodable]")
        }
    }
}

extension KeyedEncodingContainer {

    mutating func encodeAny(_ value: Any?, forKey key: KeyedEncodingContainer<K>.Key) throws {
        guard let value = value else {
            return
        }

        // NSNumber bridges into many different types - try to extract the original.
        // Note that for swift numeric types, this actually does a double conversion (to NSNumber first).
        let encodedValue: Any
        if let numberValue = value as? NSNumber, let swiftValue = numberValue.swiftValue {
            encodedValue = swiftValue
        } else {
            encodedValue = value
        }

        // Based on https://github.com/apple/swift-corelibs-foundation/blob/ca3669eb9ac282c649e71824d9357dbe140c8251/Sources/Foundation/JSONSerialization.swift#L397
        switch encodedValue {
        case let encodable as Encodable:
            try encode(encodable, forKey: key)
        case let str as String:
            try encode(str, forKey: key)
        case let boolValue as Bool:
            try encode(boolValue, forKey: key)
        case let num as Int:
            try encode(num, forKey: key)
        case let num as Int8:
            try encode(num, forKey: key)
        case let num as Int16:
            try encode(num, forKey: key)
        case let num as Int32:
            try encode(num, forKey: key)
        case let num as Int64:
            try encode(num, forKey: key)
        case let num as UInt:
            try encode(num, forKey: key)
        case let num as UInt8:
            try encode(num, forKey: key)
        case let num as UInt16:
            try encode(num, forKey: key)
        case let num as UInt32:
            try encode(num, forKey: key)
        case let num as UInt64:
            try encode(num, forKey: key)
        case let array as [Any?]:
            var container = nestedUnkeyedContainer(forKey: key)
            for element in array {
                try container.encodeAny(element)
            }
        case let dict as [AnyHashable: Any?]:
            var container = nestedContainer(keyedBy: JSONCodingKeys.self, forKey: key)
            for (key, value) in dict {
                try container.encodeAny(value, forKey: JSONCodingKeys(String(describing: key)))
            }
        case let num as Float:
            try encode(num, forKey: key)
        case let num as Double:
            try encode(num, forKey: key)
        case let num as Decimal:
            try encode(num, forKey: key)
        case let num as NSDecimalNumber:
            try encode(num.decimalValue, forKey: key)
        case is NSNull:
            try encodeNil(forKey: key)
        default:
            // Ideally we would throw an error here, but we still want to complete the encode to maintain
            // backwards compatibility.
            try encode("[Non-Encodable]", forKey: key)
        }
    }
}

fileprivate extension NSNumber {

    var swiftValue: Any? {
        // https://developer.apple.com/documentation/foundation/nsnumber#1776615
        switch String(cString: objCType) {
        case "c":
            return self as? CBool
        case "C":
            return self as? CBool
        case "s":
            return self as? CShort
        case "S":
            return self as? CUnsignedShort
        case "i":
            return self as? CInt
        case "I":
            return self as? CUnsignedInt
        case "l":
            return self as? CLong
        case "L":
            return self as? CUnsignedLong
        case "q":
            return self as? CLongLong
        case "Q":
            return self as? CUnsignedLongLong
        case "f":
            return self as? CFloat
        case "d":
            return self as? CDouble
        default:
            return self
        }
    }
}
