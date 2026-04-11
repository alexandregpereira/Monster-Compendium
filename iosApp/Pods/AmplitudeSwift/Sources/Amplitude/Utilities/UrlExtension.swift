//
//  UrlExtension.swift
//
//
//  Created by Marvin Liu on 12/7/22.
//

import Foundation

extension URL {
    func appendFileNameSuffix(suffix: String) -> URL {
        var filename = deletingPathExtension().lastPathComponent + "\(suffix)"
        if !pathExtension.isEmpty {
            filename += ".\(pathExtension)"
        }
        return deletingLastPathComponent().appendingPathComponent(filename)
    }

    func appendFileNamePrefix(prefix: String) -> URL {
        var filename = "\(prefix)" + deletingPathExtension().lastPathComponent
        if !pathExtension.isEmpty {
            filename += ".\(pathExtension)"
        }
        return deletingLastPathComponent().appendingPathComponent(filename)
    }

    func hasPrefix(_ prefix: String) -> Bool {
        let filename = deletingPathExtension().lastPathComponent
        return filename.hasPrefix(prefix)
    }
}
