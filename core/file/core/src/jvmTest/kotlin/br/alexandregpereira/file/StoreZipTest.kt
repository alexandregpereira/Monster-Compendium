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

import kotlin.test.Test
import kotlin.test.assertEquals

class StoreZipTest {

    @Test
    fun `createStoreZip and extractStoreZip round-trip with single entry`() {
        val entries = listOf(
            FileEntry("content.json", """{"key":"value"}""".encodeToByteArray()),
        )

        val zip = createStoreZip(entries)
        val extracted = extractStoreZip(zip)

        assertEquals(1, extracted.size)
        assertEquals("content.json", extracted[0].name)
        assertEquals(entries[0].content.toList(), extracted[0].content.toList())
    }

    @Test
    fun `createStoreZip and extractStoreZip round-trip with multiple entries`() {
        val entries = listOf(
            FileEntry("content.json", """{"key":"value"}""".encodeToByteArray()),
            FileEntry("goblin.png", byteArrayOf(1, 2, 3)),
            FileEntry("orc.webp", byteArrayOf(4, 5, 6, 7)),
        )

        val zip = createStoreZip(entries)
        val extracted = extractStoreZip(zip)

        assertEquals(3, extracted.size)
        assertEquals("content.json", extracted[0].name)
        assertEquals(entries[0].content.toList(), extracted[0].content.toList())
        assertEquals("goblin.png", extracted[1].name)
        assertEquals(entries[1].content.toList(), extracted[1].content.toList())
        assertEquals("orc.webp", extracted[2].name)
        assertEquals(entries[2].content.toList(), extracted[2].content.toList())
    }

    @Test
    fun `createStoreZip output is readable by JVM ZipInputStream`() {
        val entries = listOf(
            FileEntry("content.json", """{"hello":"world"}""".encodeToByteArray()),
            FileEntry("image.png", byteArrayOf(1, 2, 3)),
        )

        val zip = createStoreZip(entries)
        val extracted = mutableListOf<FileEntry>()
        java.util.zip.ZipInputStream(zip.inputStream()).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                extracted.add(FileEntry(name = entry.name, content = zis.readBytes()))
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }

        assertEquals(2, extracted.size)
        assertEquals("content.json", extracted[0].name)
        assertEquals(entries[0].content.toList(), extracted[0].content.toList())
        assertEquals("image.png", extracted[1].name)
        assertEquals(entries[1].content.toList(), extracted[1].content.toList())
    }

    @Test
    fun `createStoreZip with empty entry list produces valid empty ZIP`() {
        val zip = createStoreZip(emptyList())
        val extracted = extractStoreZip(zip)
        assertEquals(emptyList(), extracted)
    }

    @Test
    fun `JVM ZipOutputStream STORED zip is readable by extractStoreZip`() {
        val entries = listOf(
            FileEntry("content.json", """{"key":"value"}""".encodeToByteArray()),
            FileEntry("goblin.png", byteArrayOf(1, 2, 3)),
        )
        val baos = java.io.ByteArrayOutputStream()
        java.util.zip.ZipOutputStream(baos).use { zos ->
            entries.forEach {
                val crc = java.util.zip.CRC32().also { c -> c.update(it.content) }
                val entry = java.util.zip.ZipEntry(it.name).also { e ->
                    e.method = java.util.zip.ZipEntry.STORED
                    e.size = it.content.size.toLong()
                    e.compressedSize = it.content.size.toLong()
                    e.crc = crc.value
                }
                zos.putNextEntry(entry)
                zos.write(it.content)
                zos.closeEntry()
            }
        }
        val extracted = extractStoreZip(baos.toByteArray())
        assertEquals(2, extracted.size)
        assertEquals("content.json", extracted[0].name)
        assertEquals(entries[0].content.toList(), extracted[0].content.toList())
        assertEquals("goblin.png", extracted[1].name)
        assertEquals(entries[1].content.toList(), extracted[1].content.toList())
    }
}
