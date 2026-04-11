//
//  DispatchQueueHolder.swift
//  Amplitude-Swift
//
//  Created by Qingzhuo Zhen on 3/8/24.
//

import Foundation

class DispatchQueueHolder {
    static let storageQueue = DispatchQueue(label: "syncPersistentStorage.amplitude.com")
}
