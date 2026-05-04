/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.file

// Pure-Kotlin STORE-mode (no compression) ZIP reader.
// Mirrors StoreZipCreator: handles local file headers with method=0.
// TODO: add DEFLATE (method=8) support for cross-platform .compendium files created on Android/JVM.
internal fun extractStoreZip(bytes: ByteArray): List<FileEntry> {
    val result = mutableListOf<FileEntry>()
    var pos = 0

    while (pos + 30 <= bytes.size) {
        val sig = bytes.int32LE(pos)
        if (sig != 0x04034b50) break // not a local file header

        val method = bytes.int16LE(pos + 8)
        val compSize = bytes.int32LE(pos + 18)
        val nameLen = bytes.int16LE(pos + 26)
        val extraLen = bytes.int16LE(pos + 28)

        val nameStart = pos + 30
        val name = bytes.decodeToString(nameStart, nameStart + nameLen)
        val dataStart = nameStart + nameLen + extraLen

        if (method == 0) {
            result.add(FileEntry(name = name, content = bytes.copyOfRange(dataStart, dataStart + compSize)))
        }

        pos = dataStart + compSize
    }

    return result
}

private fun ByteArray.int32LE(offset: Int): Int =
    (this[offset].toInt() and 0xFF) or
            ((this[offset + 1].toInt() and 0xFF) shl 8) or
            ((this[offset + 2].toInt() and 0xFF) shl 16) or
            ((this[offset + 3].toInt() and 0xFF) shl 24)

private fun ByteArray.int16LE(offset: Int): Int =
    (this[offset].toInt() and 0xFF) or
            ((this[offset + 1].toInt() and 0xFF) shl 8)
