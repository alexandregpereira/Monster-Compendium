//
//  PersistentStorage.swift
//
//
//  Created by Marvin Liu on 10/28/22.
//

import Foundation

#if AMPLITUDE_DISABLE_UIKIT
@_spi(Internal) import AmplitudeCoreNoUIKit
#else
@_spi(Internal) import AmplitudeCore
#endif

class PersistentStorage: Storage {
    typealias EventBlock = URL

    static internal func getEventStoragePrefix(_ apiKey: String, _ instanceName: String) -> String {
        return "storage-\(apiKey)-\(instanceName)"
    }

    static internal func getIdentifyStoragePrefix(_ apiKey: String, _ instanceName: String) -> String {
        return "identify-\(apiKey)-\(instanceName)"
    }

    let storagePrefix: String
    let userDefaults: UserDefaults?
    let fileManager: FileManager
    private var outputStream: OutputFileStream?
    // Store event.callback in memory as it cannot be ser/deser in files.
    private var eventCallbackMap: [String: EventCallback]
    private var appPath: String!
    let syncQueue = DispatchQueueHolder.storageQueue
    let storageVersionKey: String
    let logger: (any Logger)?
    let diagonostics: Diagnostics
    let diagonosticsClient: CoreDiagnostics

    init(storagePrefix: String, logger: (any Logger)?, diagonostics: Diagnostics, diagnosticsClient: CoreDiagnostics) {
        self.storagePrefix = storagePrefix == PersistentStorage.DEFAULT_STORAGE_PREFIX || storagePrefix.starts(with: "\(PersistentStorage.DEFAULT_STORAGE_PREFIX)-")
            ? storagePrefix
            : "\(PersistentStorage.DEFAULT_STORAGE_PREFIX)-\(storagePrefix)"
        self.userDefaults = UserDefaults(suiteName: "\(PersistentStorage.AMP_STORAGE_PREFIX).\(self.storagePrefix)")
        self.fileManager = FileManager.default
        self.eventCallbackMap = [String: EventCallback]()
        self.storageVersionKey = "\(PersistentStorage.STORAGE_VERSION).\(self.storagePrefix)"
        self.logger = logger
        self.diagonostics = diagonostics
        self.diagonosticsClient = diagnosticsClient
        // Make sure Amplitude data is sandboxed per app
        self.appPath = Self.getAppPath(sandboxed: isStorageSandboxed())
        handleV1Files()
    }

    func write(key: StorageKey, value: Any?) throws {
        try syncQueue.sync {
            switch key {
            case .EVENTS:
                if let event = value as? BaseEvent {
                    let eventStoreFile = getCurrentEventFile()
                    self.storeEvent(toFile: eventStoreFile, event: event)
                    if let eventCallback = event.callback, let eventInsertId = event.insertId {
                        eventCallbackMap[eventInsertId] = eventCallback
                    }
                }
            default:
                if isBasicType(value: value) {
                    userDefaults?.set(value, forKey: key.rawValue)
                } else {
                    throw Exception.unsupportedType
                }
            }
        }
    }

    func read<T>(key: StorageKey) -> T? {
        syncQueue.sync {
            var result: T?
            switch key {
            case .EVENTS:
                result = getEventFiles() as? T
            default:
                result = userDefaults?.object(forKey: key.rawValue) as? T
            }
            return result
        }
    }

    func getEventsString(eventBlock: EventBlock) -> String? {
        var content: String?
        do {
            content = try String(contentsOf: eventBlock, encoding: .utf8)
            if eventBlock.hasPrefix(PersistentStorage.STORAGE_V2_PREFIX) {
                // v2 file
                return readV2File(content: content!)
            } else {
                // handle v1 file
                return readV1File(content: content!)
            }
        } catch {
            diagonostics.addErrorLog(error.localizedDescription)
            logger?.error(message: error.localizedDescription)
        }
        return content
    }

    func remove(eventBlock: EventBlock) {
        syncQueue.sync {
            do {
                try fileManager.removeItem(atPath: eventBlock.path)
            } catch {
                diagonostics.addErrorLog(error.localizedDescription)
                logger?.error(message: error.localizedDescription)
            }
        }
    }

    func splitBlock(eventBlock: EventBlock, events: [BaseEvent]) {
        syncQueue.sync {
            let total = events.count
            let half = total / 2
            let leftSplit = Array(events[0..<half])
            let rightSplit = Array(events[half..<total])
            storeEventsInNewFile(toFile: eventBlock.appendFileNameSuffix(suffix: "-1"), events: leftSplit)
            storeEventsInNewFile(toFile: eventBlock.appendFileNameSuffix(suffix: "-2"), events: rightSplit)
            do {
                try fileManager.removeItem(atPath: eventBlock.path)
            } catch {
                diagonostics.addErrorLog(error.localizedDescription)
                logger?.error(message: error.localizedDescription)
            }
        }
    }

    func getResponseHandler(
        configuration: Configuration,
        eventPipeline: EventPipeline,
        eventBlock: EventBlock,
        eventsString: String
    ) -> ResponseHandler {
        return PersistentStorageResponseHandler(
            configuration: configuration,
            storage: self,
            eventPipeline: eventPipeline,
            eventBlock: eventBlock,
            eventsString: eventsString,
            diagnosticsClient: diagonosticsClient
        )
    }

    func reset() {
        syncQueue.sync {
            let urls = getEventFiles(includeUnfinished: true)
            let keys = userDefaults?.dictionaryRepresentation().keys
            keys?.forEach { key in
                userDefaults?.removeObject(forKey: key)
            }
            for url in urls {
                try? fileManager.removeItem(atPath: url.path)
            }
        }
    }

    func rollover() {
        syncQueue.sync {
            if getCurrentEventFileIndex() == nil {
                return
            }
            let currentFile = getCurrentEventFile()
            if fileManager.fileExists(atPath: currentFile.path) == false {
                return
            }
            if let attributes = try? fileManager.attributesOfItem(atPath: currentFile.path),
                let fileSize = attributes[FileAttributeKey.size] as? UInt64,
                fileSize >= 0
            {
                finish(file: currentFile)
            }
        }
    }

    func getEventCallback(insertId: String) -> EventCallback? {
        return eventCallbackMap[insertId]
    }

    func removeEventCallback(insertId: String) {
        eventCallbackMap.removeValue(forKey: insertId)
    }

    func isBasicType(value: Any?) -> Bool {
        var result = false
        if value == nil {
            result = true
        } else {
            switch value {
            case is NSNull, is Int, is Float, is Double, is Decimal, is NSNumber, is Bool, is String, is NSString:
                result = true
            default:
                break
            }
        }
        return result
    }

    internal func isStorageSandboxed() -> Bool {
        return SandboxHelper().isSandboxEnabled()
    }

    private static func getAppPath(sandboxed: Bool) -> String {
        if sandboxed {
            return ""
        }
        let appIdentifier = Bundle.main.bundleIdentifier ?? (Bundle.main.executablePath ?? ProcessInfo.processInfo.processName).fnv1a64String()
        return "\(appIdentifier)/"
    }
}

extension PersistentStorage {
    static let DEFAULT_STORAGE_PREFIX = "amplitude-swift"
    static let AMP_STORAGE_PREFIX = "com.amplitude.storage"
    static let MAX_FILE_SIZE = 975000  // 975KB
    static let TEMP_FILE_EXTENSION = "tmp"
    static let DELMITER = "\u{0000}"
    static let STORAGE_VERSION = "amplitude.events.storage.version"
    static let STORAGE_V2_PREFIX = "v2-"

    enum Exception: Error {
        case unsupportedType
    }
}

extension PersistentStorage {
    internal var eventsFileKey: String {
        return "\(storagePrefix).\(StorageKey.EVENTS.rawValue).index"
    }

    private func getCurrentEventFile() -> URL {
        let allOpenFiles = try? fileManager.contentsOfDirectory(
            at: getEventsStorageDirectory(),
            includingPropertiesForKeys: [],
            options: .skipsHiddenFiles
        ).filter { (file) -> Bool in
            return file.pathExtension == PersistentStorage.TEMP_FILE_EXTENSION
        }
        if allOpenFiles != nil && allOpenFiles!.count > 0 {
            return allOpenFiles![0]
        }
        var currentFileIndex = 0
        let index: Int = getCurrentEventFileIndex() ?? 0
        userDefaults?.set(index, forKey: eventsFileKey)
        currentFileIndex = index
        return getEventsFile(index: currentFileIndex)
    }

    private func getCurrentEventFileIndex() -> Int? {
        return userDefaults?.object(forKey: eventsFileKey) as? Int
    }

    private func getEventsFile(index: Int) -> URL {
        let dir = getEventsStorageDirectory()
        let fileURL = dir.appendingPathComponent("\(PersistentStorage.STORAGE_V2_PREFIX)\(index)").appendingPathExtension(
            PersistentStorage.TEMP_FILE_EXTENSION
        )
        return fileURL
    }

    internal func getEventFiles(includeUnfinished: Bool = false) -> [URL] {
        var result = [URL]()

        let eventsStorageDirectory = getEventsStorageDirectory(createDirectory: false)
        if !fileManager.fileExists(atPath: eventsStorageDirectory.path) {
            return result
        }

        // finish out any file in progress
        let currentFile = getCurrentEventFile()
        finish(file: currentFile)

        let allFiles = try? fileManager.contentsOfDirectory(
            at: getEventsStorageDirectory(),
            includingPropertiesForKeys: [],
            options: .skipsHiddenFiles
        )
        var files = allFiles
        if includeUnfinished == false {
            files = allFiles?.filter { (file) -> Bool in
                return file.pathExtension != PersistentStorage.TEMP_FILE_EXTENSION
            }
        }
        let sorted = files?.sorted { (left, right) -> Bool in
            return left.lastPathComponent < right.lastPathComponent
        }
        if let s = sorted {
            result = s
        }
        return result
    }

    internal func getEventsStorageDirectory(createDirectory: Bool = true) -> URL {
        // TODO: Update to use applicationSupportDirectory for all platforms (this will require a migration)
        // let searchPathDirectory = FileManager.SearchPathDirectory.applicationSupportDirectory
        // tvOS doesn't have access to document
        // macOS /Documents dir might be synced with iCloud
        #if os(tvOS) || os(macOS)
            let searchPathDirectory = FileManager.SearchPathDirectory.cachesDirectory
        #else
            let searchPathDirectory = FileManager.SearchPathDirectory.documentDirectory
        #endif

        let urls = fileManager.urls(for: searchPathDirectory, in: .userDomainMask)
        let docUrl = urls[0]
        let storageUrl = docUrl.appendingPathComponent("amplitude/\(appPath ?? "")\(eventsFileKey)/")
        if createDirectory {
            // try to create it, will fail if already exists.
            // tvOS, watchOS regularly clear out data.
            try? FileManager.default.createDirectory(at: storageUrl, withIntermediateDirectories: true, attributes: nil)
        }
        return storageUrl
    }

    private func storeEvent(toFile file: URL, event: BaseEvent) {
        var storeFile = file

        if fileManager.fileExists(atPath: storeFile.path) == false {
            start(file: storeFile)
        } else if outputStream == nil {
            // this can happen if an instance was terminated before finishing a file.
            open(file: storeFile)
        }

        // Verify file size isn't too large
        if let attributes = try? fileManager.attributesOfItem(atPath: storeFile.path),
            let fileSize = attributes[FileAttributeKey.size] as? UInt64,
            fileSize >= PersistentStorage.MAX_FILE_SIZE
        {
            finish(file: storeFile)
            // Set the new file path
            storeFile = getCurrentEventFile()
            start(file: storeFile)
        }

        let jsonString = event.toString().replacingOccurrences(of: PersistentStorage.DELMITER, with: "")
        do {
            if outputStream == nil {
                logger?.error(message: "OutputStream is nil with file: \(storeFile)")
            }
            try outputStream?.write("\(jsonString)\(PersistentStorage.DELMITER)")
        } catch {
            diagonostics.addErrorLog(error.localizedDescription)
            logger?.error(message: error.localizedDescription)
        }
    }

    private func storeEventsInNewFile(toFile file: URL, events: [BaseEvent]) {
        let storeFile = file

        guard fileManager.fileExists(atPath: storeFile.path) != true else {
            diagonostics.addErrorLog("Splited file duplicate for path: \(storeFile.path)")
            return
        }

        start(file: storeFile)

        let jsonString = events.map { $0.toString().replacingOccurrences(of: PersistentStorage.DELMITER, with: "")  }.joined(separator: PersistentStorage.DELMITER)
        do {
            if outputStream == nil {
                logger?.error(message: "OutputStream is nil with file: \(storeFile)")
            }
            try outputStream?.write("\(jsonString)\(PersistentStorage.DELMITER)")
        } catch {
            diagonostics.addErrorLog(error.localizedDescription)
            logger?.error(message: error.localizedDescription)
        }
        finish(file: storeFile)
    }

    private func start(file: URL) {
        do {
            outputStream = try OutputFileStream(fileURL: file)
            try outputStream?.create()
        } catch {
            diagonostics.addErrorLog(error.localizedDescription)
            logger?.error(message: error.localizedDescription)
        }
    }

    private func open(file: URL) {
        if outputStream == nil {
            // this can happen if an instance was terminated before finishing a file.
            do {
                outputStream = try OutputFileStream(fileURL: file)
                if let outputStream = outputStream {
                    try outputStream.open()
                }
            } catch {
                diagonostics.addErrorLog(error.localizedDescription)
                logger?.error(message: error.localizedDescription)
            }
        }
    }

    private func finish(file: URL) {
        guard let outputStream = self.outputStream else {
            return
        }

        do {
            try outputStream.close()
        } catch {
            diagonostics.addErrorLog(error.localizedDescription)
            logger?.error(message: error.localizedDescription)
        }
        self.outputStream = nil

        rename(file)
        let currentFileIndex: Int = (getCurrentEventFileIndex() ?? 0) + 1
        userDefaults?.set(currentFileIndex, forKey: eventsFileKey)
    }

    private func rename(_ file: URL) {
        let fileWithoutTemp = file.deletingPathExtension()
        var updatedFile = fileWithoutTemp
        if !fileManager.fileExists(atPath: file.path) {
            logger?.debug(message: "Try to rename non exist file.")
            return
        }
        if fileManager.fileExists(atPath: fileWithoutTemp.path) {
            logger?.debug(message: "File already exists \(fileWithoutTemp.path), handle gracefully.")
            let suffix = "-\(Date().timeIntervalSince1970)-\(Int.random(in: 0..<1000))"
            updatedFile = fileWithoutTemp.appendFileNameSuffix(suffix: suffix)
        }
        do {
            try fileManager.moveItem(at: file, to: updatedFile)
        } catch {
            diagonostics.addErrorLog(error.localizedDescription)
            logger?.error(message: "Unable to rename file: \(file.path)")
        }
    }

    private func handleV1Files() {
        syncQueue.sync {
            let currentStorageVersion = (userDefaults?.object(forKey: self.storageVersionKey) as? Int) ?? 0
            if currentStorageVersion > 1 {
                return
            }
            let allFiles = self.getEventFiles(includeUnfinished: true)
            for file in allFiles {
                do {
                    if file.hasPrefix(PersistentStorage.STORAGE_V2_PREFIX) {
                        logger?.debug(message: "file already migrated")
                        return
                    }
                    let content = try String(contentsOf: file, encoding: .utf8)
                    if content.hasSuffix(PersistentStorage.DELMITER) {
                        break // already handled and in v2 format
                    }

                    let normalizedContent = "[\(content.trimmingCharacters(in: ["[", ",", "]"]))]"
                    let events = BaseEvent.fromArrayString(jsonString: normalizedContent)
                    if events != nil {
                        migrateFile(file: file, events: events!)
                    }
                    if file.pathExtension != "" {
                        finish(file: file)
                    }
                    migrateFileName(file)
                } catch {
                    diagonostics.addErrorLog("Error migrating file: \(file.path) for \(error.localizedDescription)")
                    logger?.error(message: error.localizedDescription)
                }
            }
            userDefaults?.setValue(2, forKey: self.storageVersionKey)
        }
    }

    private func migrateFile(file: URL, events: [BaseEvent]) {
        guard fileManager.fileExists(atPath: file.path) == true else {
            diagonostics.addErrorLog("File to migrate not exists any more : \(file.path)")
            return
        }

        do {
            let jsonString = events.map { $0.toString().replacingOccurrences(of: PersistentStorage.DELMITER, with: "")  }.joined(separator: PersistentStorage.DELMITER)
            let finalString = "\(jsonString)\(PersistentStorage.DELMITER)"
            try finalString.write(to: file, atomically: true, encoding: .utf8)
        } catch {
            diagonostics.addErrorLog(error.localizedDescription)
            logger?.error(message: error.localizedDescription)
        }
    }

    private func migrateFileName(_ file: URL) {
        let fileNameV2 = file.appendFileNamePrefix(prefix: PersistentStorage.STORAGE_V2_PREFIX).deletingPathExtension()
        if fileManager.fileExists(atPath: fileNameV2.path) {
            logger?.debug(message: "Migrate found an existing file.")
            return
        }
        do {
            try fileManager.moveItem(at: file, to: fileNameV2)
        } catch {
            diagonostics.addErrorLog(error.localizedDescription)
            logger?.error(message: "Unable to migrate file: \(file.path)")
        }
    }

    private func readV2File(content: String) -> String {
        var events: [BaseEvent] = [BaseEvent]()
        content.components(separatedBy: PersistentStorage.DELMITER).forEach{
            let currentString = String($0)
            if currentString.isEmpty {
                return
            }
            if let event = BaseEvent.fromString(jsonString: String($0)) {
                events.append(event)
            } else {
                diagonostics.addMalformedEvent(String($0))
            }
        }
        return eventsToJSONString(events: events)
    }

    private func eventsToJSONString(events: [BaseEvent]) -> String {
        var result = ""
        do {
            let encoder = JSONEncoder()
            encoder.outputFormatting = .sortedKeys
            let json = try encoder.encode(events)
            if let printed = String(data: json, encoding: .utf8) {
                result = printed
            }
        } catch {
            diagonostics.addErrorLog(error.localizedDescription)
        }
        return result
    }

    private func readV1File(content: String) -> String {
        var result = ""
        let normalizedContent = "[\(content.trimmingCharacters(in: ["[", ",", "]"]))]"
        let events = BaseEvent.fromArrayString(jsonString: normalizedContent)
        if events != nil {
            result = normalizedContent
        }
        return result
    }
}
