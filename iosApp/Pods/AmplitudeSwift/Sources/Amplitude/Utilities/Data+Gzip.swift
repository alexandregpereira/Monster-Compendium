//
//  Data+Gzip.swift
//  Amplitude-Swift
//
//  Created by Chris Leonavicius on 1/27/26.
//

import Foundation
import zlib

extension Data {

    enum CompressionError: Error {
        case memoryAccessFailed
        case zlibFailure
    }

    /// Returns a gzip-compressed copy of this data.
    /// - Throws: `CompressionError` on zlib failure or memory access issues.
    func gzipped() throws -> Data {
        guard !isEmpty else {
            return Data()
        }

        var stream = z_stream()
        var status: Int32 = Z_OK

        return try withUnsafeBytes { (src: UnsafeRawBufferPointer) throws -> Data in
            guard let srcBase = src.baseAddress else {
                throw CompressionError.memoryAccessFailed
            }

            stream.next_in = UnsafeMutablePointer<Bytef>(mutating: srcBase.assumingMemoryBound(to: Bytef.self))
            stream.avail_in = uInt(count)

            // 15 = max window bits, +16 = gzip wrapper
            status = deflateInit2_(
                &stream,
                Z_DEFAULT_COMPRESSION,
                Z_DEFLATED,
                15 + 16,
                8,
                Z_DEFAULT_STRATEGY,
                ZLIB_VERSION,
                Int32(MemoryLayout<z_stream>.size)
            )
            guard status == Z_OK else {
                throw CompressionError.zlibFailure
            }
            defer {
                deflateEnd(&stream)
            }

            let chunkSize = 64 * 1024
            var output = Data()
            output.reserveCapacity(Swift.min(count / 2, 1_048_576)) // heuristic; avoids huge over-reserve

            var buffer = Data(count: chunkSize)

            while true {
                let produced: Int = try buffer.withUnsafeMutableBytes { dst -> Int in
                    guard let dstBase = dst.baseAddress else {
                        throw CompressionError.memoryAccessFailed
                    }

                    stream.next_out = dstBase.assumingMemoryBound(to: Bytef.self)
                    stream.avail_out = uInt(dst.count)

                    status = deflate(&stream, Z_FINISH)

                    return dst.count - Int(stream.avail_out)
                }

                if produced > 0 {
                    output.append(buffer.prefix(produced))
                }

                if status == Z_STREAM_END {
                    return output
                }

                // `deflate(..., Z_FINISH)` should return Z_OK until it ends, otherwise it's an error.
                guard status == Z_OK else {
                    throw CompressionError.zlibFailure
                }

                // If nothing was produced but we're not done, keep looping; zlib may need more iterations.
            }
        }
    }
}
