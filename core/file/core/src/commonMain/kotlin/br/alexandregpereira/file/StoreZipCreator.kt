/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
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

// Pure-Kotlin STORE-mode (no compression) ZIP archive with a single entry.
// Used on platforms that lack java.util.zip (e.g. iOS Kotlin/Native).
internal suspend fun createStoreZip(
    zipEntryFiles: List<FileEntry>,
): ByteArray {
    val zip = ZipBuilder()
    val localHeaderOffsets = IntArray(zipEntryFiles.size)

    zipEntryFiles.forEachIndexed { index, entry ->
        val nameBytes = entry.name.encodeToByteArray()
        val crc = crc32(entry.readBytes())
        val size = entry.size.toInt()

        localHeaderOffsets[index] = zip.size()

        // Local file header
        zip.le32(0x04034b50)
        zip.le16(20)
        zip.le16(0)
        zip.le16(0) // STORE
        zip.le16(0)
        zip.le16(0)
        zip.le32(crc)
        zip.le32(size)
        zip.le32(size)
        zip.le16(nameBytes.size)
        zip.le16(0)
        zip.bytes(nameBytes)
        zip.bytes(entry.readBytes())
    }

    val centralDirOffset = zip.size()

    zipEntryFiles.forEachIndexed { index, entry ->
        val nameBytes = entry.name.encodeToByteArray()
        val crc = crc32(entry.readBytes())
        val size = entry.size.toInt()

        // Central directory file header
        zip.le32(0x02014b50)
        zip.le16(20)
        zip.le16(20)
        zip.le16(0)
        zip.le16(0) // STORE
        zip.le16(0)
        zip.le16(0)
        zip.le32(crc)
        zip.le32(size)
        zip.le32(size)
        zip.le16(nameBytes.size)
        zip.le16(0)
        zip.le16(0)
        zip.le16(0)
        zip.le16(0)
        zip.le32(0)
        zip.le32(localHeaderOffsets[index])
        zip.bytes(nameBytes)
    }

    val centralDirSize = zip.size() - centralDirOffset
    val entryCount = zipEntryFiles.size

    // End of central directory
    zip.le32(0x06054b50)
    zip.le16(0)
    zip.le16(0)
    zip.le16(entryCount)
    zip.le16(entryCount)
    zip.le32(centralDirSize)
    zip.le32(centralDirOffset)
    zip.le16(0)

    return zip.toByteArray()
}

private fun crc32(data: ByteArray): Int {
    var crc = 0xFFFFFFFF.toInt()
    for (b in data) {
        val idx = (crc xor (b.toInt() and 0xFF)) and 0xFF
        crc = crcTable[idx] xor (crc ushr 8)
    }
    return crc xor 0xFFFFFFFF.toInt()
}

private val crcTable = IntArray(256) { n ->
    var c = n
    repeat(8) {
        c = if (c and 1 != 0) (0xEDB88320u.toInt() xor (c ushr 1)) else (c ushr 1)
    }
    c
}

private class ZipBuilder {
    private var data = ByteArray(1024)
    private var pos = 0

    private fun ensureCapacity(extra: Int) {
        if (pos + extra > data.size) data = data.copyOf(maxOf(data.size * 2, pos + extra))
    }

    fun le32(value: Int) {
        ensureCapacity(4)
        data[pos++] = (value and 0xFF).toByte()
        data[pos++] = ((value shr 8) and 0xFF).toByte()
        data[pos++] = ((value shr 16) and 0xFF).toByte()
        data[pos++] = ((value shr 24) and 0xFF).toByte()
    }

    fun le16(value: Int) {
        ensureCapacity(2)
        data[pos++] = (value and 0xFF).toByte()
        data[pos++] = ((value shr 8) and 0xFF).toByte()
    }

    fun bytes(src: ByteArray) {
        ensureCapacity(src.size)
        src.copyInto(data, pos)
        pos += src.size
    }

    fun size(): Int = pos

    fun toByteArray(): ByteArray = data.copyOf(pos)
}
