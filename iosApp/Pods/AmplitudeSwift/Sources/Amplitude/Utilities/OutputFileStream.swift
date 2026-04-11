//
//  OutputFileStream.swift
//
//
//  Created by Marvin Liu on 11/22/22.
//
//  Originally from Segment: https://github.com/segmentio/analytics-swift, under MIT license.

import Foundation

#if os(Linux)
    import Glibc
#else
    import Darwin.C
#endif

internal class OutputFileStream {
    enum OutputStreamError: Error {
        case invalidPath(String)
        case unableToOpen(String)
        case unableToWrite(String)
        case unableToCreate(String)
        case unableToClose(String)
    }

    var fileHandle: FileHandle?
    let fileURL: URL

    init(fileURL: URL) throws {
        self.fileURL = fileURL
        let path = fileURL.path
        guard path.isEmpty == false else { throw OutputStreamError.invalidPath(path) }
    }

    func create() throws {
        let path = fileURL.path
        if FileManager.default.fileExists(atPath: path) {
            throw OutputStreamError.unableToCreate(path)
        } else {
            let created = FileManager.default.createFile(atPath: fileURL.path, contents: nil)
            if created == false {
                throw OutputStreamError.unableToCreate(path)
            } else {
                try open()
            }
        }
    }

    func open() throws {
        if fileHandle != nil { return }
        do {
            fileHandle = try FileHandle(forWritingTo: fileURL)
            seekToEnd()
        } catch {
            throw OutputStreamError.unableToOpen(fileURL.path)
        }
    }

    func seekToEnd() {
        if fileHandle == nil {
            return
        }
        if #available(macOS 10.15.4, iOS 13.4, macCatalyst 13.4, tvOS 13.4, watchOS 6.2, *) {
            _ = try? fileHandle?.seekToEnd()
        } else if #available(tvOS 13.0, *) {
            try? fileHandle?.seek(toOffset: .max)
        } else {
            fileHandle?.seekToEndOfFile()
        }
    }

    func write(_ data: Data) throws {
        guard data.isEmpty == false else { return }
        if #available(macOS 10.15.4, iOS 13.4, macCatalyst 13.4, tvOS 13.4, watchOS 6.2, *) {
            do {
                try fileHandle?.write(contentsOf: data)
            } catch {
                throw OutputStreamError.unableToWrite(fileURL.path)
            }
        } else {
            fileHandle?.write(data)
        }
    }

    func write(_ string: String, _ append: Bool = true) throws {
        guard string.isEmpty == false else { return }
        if let data = string.data(using: .utf8) {
            if append {
                seekToEnd()
            }
            try write(data)
        }
    }

    func close() throws {
        do {
            let existing = fileHandle
            fileHandle = nil
            if #available(tvOS 13.0, *) {
                try existing?.synchronize()
                try existing?.close()
            } else {
                existing?.synchronizeFile()
                existing?.closeFile()
            }
        } catch {
            throw OutputStreamError.unableToClose(fileURL.path)
        }
    }
}
