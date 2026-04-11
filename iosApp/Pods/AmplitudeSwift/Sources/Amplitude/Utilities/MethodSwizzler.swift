//
//  Swizzler.swift
//  Amplitude-Swift
//
//  Created by Jin Xu on 3/6/25.
//

import Foundation
import ObjectiveC.runtime

final class MethodSwizzler {

    private static let lock = NSLock()

    @discardableResult
    static func swizzleInstanceMethod(for cls: AnyClass, originalSelector: Selector, swizzledSelector: Selector, logger: (any Logger)? = nil) -> Bool {
        let originalSelName = NSStringFromSelector(originalSelector)
        let swizzledSelName = NSStringFromSelector(swizzledSelector)

        guard let originalMethod = class_getInstanceMethod(cls, originalSelector) else {
            logger?.error(message: "Failed to swizzle \(originalSelName) with \(swizzledSelName) on \(cls) because the original method was not found")
            return false
        }

        guard let swizzledMethod = class_getInstanceMethod(cls, swizzledSelector) else {
            logger?.error(message: "Failed to swizzle \(originalSelName) with \(swizzledSelName) on \(cls) because the swizzled method was not found")
            return false
        }

        let originalIMP = method_getImplementation(originalMethod)

        let methodAdded = class_addMethod(
            cls,
            originalSelector,
            method_getImplementation(swizzledMethod),
            method_getTypeEncoding(swizzledMethod)
        )

        if methodAdded {
            class_replaceMethod(
                cls,
                swizzledSelector,
                originalIMP,
                method_getTypeEncoding(originalMethod)
            )
        } else {
            method_exchangeImplementations(originalMethod, swizzledMethod)
        }

        return true
    }

    @discardableResult
    static func unswizzleInstanceMethod(for cls: AnyClass, originalSelector: Selector, swizzledSelector: Selector, logger: (any Logger)? = nil) -> Bool {
        let originalSelName = NSStringFromSelector(originalSelector)
        let swizzledSelName = NSStringFromSelector(swizzledSelector)

        guard let originalMethod = class_getInstanceMethod(cls, originalSelector),
              let swizzledMethod = class_getInstanceMethod(cls, swizzledSelector) else {
            logger?.error(message: "Failed to unswizzle \(originalSelName) with \(swizzledSelName) on \(cls) because the method was not found")
            return false
        }

        method_exchangeImplementations(originalMethod, swizzledMethod)

        return true
    }
}
