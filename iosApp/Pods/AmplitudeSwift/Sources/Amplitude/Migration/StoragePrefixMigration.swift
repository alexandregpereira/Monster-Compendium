import Foundation

class StoragePrefixMigration {
    let source: PersistentStorage
    let destination: PersistentStorage
    let logger: (any Logger)?

    init(source: PersistentStorage, destination: PersistentStorage, logger: (any Logger)?) {
        self.source = source
        self.destination = destination
        self.logger = logger
    }

    func execute(skipEventFiles: Bool = false) {
        if source.storagePrefix == destination.storagePrefix {
            return
        }

        if !skipEventFiles {
            moveSourceEventFilesToDestination()
        }

        moveUserDefaults()
    }

    private func moveUserDefaults() {
        moveStringProperty(StorageKey.DEVICE_ID)
        moveStringProperty(StorageKey.USER_ID)
        moveIntegerProperty(StorageKey.PREVIOUS_SESSION_ID)
        moveIntegerProperty(StorageKey.LAST_EVENT_TIME)
        moveIntegerProperty(StorageKey.LAST_EVENT_ID)
        moveEventsFileKey()
    }

    private func moveSourceEventFilesToDestination() {
        let sourceEventFiles = source.getEventFiles(includeUnfinished: true)
        if sourceEventFiles.count == 0 {
            return
        }
        // Ensure destination directory exists.
        _ = destination.getEventsStorageDirectory(createDirectory: true)

        let fileManager = FileManager.default
        for sourceEventFile in sourceEventFiles {
            var destinationEventFile = sourceEventFile.path.replacingOccurrences(of: "/\(source.eventsFileKey)/", with: "/\(destination.eventsFileKey)/")
            if fileManager.fileExists(atPath: destinationEventFile) {
                var fileExtension = sourceEventFile.pathExtension
                if fileExtension != "" {
                    fileExtension = ".\(fileExtension)"
                }
                destinationEventFile = "\((destinationEventFile as NSString).deletingPathExtension)-\(NSUUID().uuidString)\(fileExtension)"
            }
            do {
                try fileManager.moveItem(atPath: sourceEventFile.path, toPath: destinationEventFile)
            } catch {
                logger?.warn(message: "Can't move \(sourceEventFile) to \(destinationEventFile): \(error)")
            }
        }
    }

    private func moveStringProperty(_ key: StorageKey) {
        guard let sourceValue: String = source.read(key: key) else {
            return
        }

        let destinationValue: String? = destination.read(key: key)
        if destinationValue == nil {
            do {
                try destination.write(key: key, value: sourceValue)
            } catch {
                logger?.warn(message: "can't write destination \(key): \(error)")
            }
        }

        do {
            try source.write(key: key, value: nil)
        } catch {
            logger?.warn(message: "can't write source \(key): \(error)")
        }
    }

    private func moveIntegerProperty(_ key: StorageKey) {
        guard let sourceValue: Int = source.read(key: key) else {
            return
        }

        let destinationValue: Int? = destination.read(key: key)
        if destinationValue == nil || destinationValue! < sourceValue {
            do {
                try destination.write(key: key, value: sourceValue)
            } catch {
                logger?.warn(message: "can't write destination \(key): \(error)")
            }
        }

        do {
            try source.write(key: key, value: nil)
        } catch {
            logger?.warn(message: "can't clear source \(key): \(error)")
        }
    }

    private func moveEventsFileKey() {
        if let sourceEventFileKey: Int = source.userDefaults?.integer(forKey: source.eventsFileKey) {
            let destinationEventFileKey: Int? = destination.userDefaults?.integer(forKey: destination.eventsFileKey)
            if destinationEventFileKey == nil || destinationEventFileKey! < sourceEventFileKey {
                destination.userDefaults?.set(sourceEventFileKey, forKey: destination.eventsFileKey)
            }
        }
        source.userDefaults?.removeObject(forKey: source.eventsFileKey)
    }
}
