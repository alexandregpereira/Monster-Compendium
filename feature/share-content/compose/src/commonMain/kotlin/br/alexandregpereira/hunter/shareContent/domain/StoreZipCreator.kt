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

package br.alexandregpereira.hunter.shareContent.domain

// Pure-Kotlin STORE-mode (no compression) ZIP archive with a single entry.
// Used on platforms that lack java.util.zip (e.g. iOS Kotlin/Native).
internal fun createStoreZip(content: ByteArray, entryName: String): ByteArray {
    val nameBytes = entryName.encodeToByteArray()
    val crc = crc32(content)
    val size = content.size
    val centralDirOffset = 30 + nameBytes.size + size

    val zip = ZipBuilder()

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
    zip.bytes(content)

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
    zip.le32(0) // offset of local header
    zip.bytes(nameBytes)

    val centralDirSize = zip.size() - centralDirOffset

    // End of central directory
    zip.le32(0x06054b50)
    zip.le16(0)
    zip.le16(0)
    zip.le16(1)
    zip.le16(1)
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
    private val data = mutableListOf<Byte>()

    fun le32(value: Int) {
        data.add((value and 0xFF).toByte())
        data.add(((value shr 8) and 0xFF).toByte())
        data.add(((value shr 16) and 0xFF).toByte())
        data.add(((value shr 24) and 0xFF).toByte())
    }

    fun le16(value: Int) {
        data.add((value and 0xFF).toByte())
        data.add(((value shr 8) and 0xFF).toByte())
    }

    fun bytes(src: ByteArray) {
        for (b in src) data.add(b)
    }

    fun size(): Int = data.size

    fun toByteArray(): ByteArray = data.toByteArray()
}
