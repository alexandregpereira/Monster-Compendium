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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class StoreZipTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var originalUserHome: String
    private lateinit var fileManager: JvmFileManager

    @Before
    fun setUp() {
        originalUserHome = System.getProperty("user.home")
        System.setProperty("user.home", tempFolder.root.absolutePath)
        fileManager = JvmFileManager(dispatcher = UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        System.setProperty("user.home", originalUserHome)
    }

    private fun createTempEntry(name: String, bytes: ByteArray): FileEntry {
        val file = tempFolder.newFile(name).apply { writeBytes(bytes) }
        return FileEntry("file://${file.absolutePath}")
    }

    @Test
    fun `createStoreZip and extractStoreZip round-trip with single entry`() = runTest {
        val contentBytes = """{"key":"value"}""".encodeToByteArray()
        val entries = listOf(createTempEntry("content.json", contentBytes))

        val zip = createStoreZip(entries)
        val extracted = fileManager.extractStoreZip(zip)

        assertEquals(1, extracted.size)
        assertEquals("content.json", extracted[0].name)
        assertEquals(contentBytes.toList(), extracted[0].readBytes().toList())
    }

    @Test
    fun `createStoreZip and extractStoreZip round-trip with multiple entries`() = runTest {
        val contentBytes = """{"key":"value"}""".encodeToByteArray()
        val goblinBytes = byteArrayOf(1, 2, 3)
        val orcBytes = byteArrayOf(4, 5, 6, 7)
        val entries = listOf(
            createTempEntry("content.json", contentBytes),
            createTempEntry("goblin.png", goblinBytes),
            createTempEntry("orc.webp", orcBytes),
        )

        val zip = createStoreZip(entries)
        val extracted = fileManager.extractStoreZip(zip)

        assertEquals(3, extracted.size)
        assertEquals("content.json", extracted[0].name)
        assertEquals(contentBytes.toList(), extracted[0].readBytes().toList())
        assertEquals("goblin.png", extracted[1].name)
        assertEquals(goblinBytes.toList(), extracted[1].readBytes().toList())
        assertEquals("orc.webp", extracted[2].name)
        assertEquals(orcBytes.toList(), extracted[2].readBytes().toList())
    }

    @Test
    fun `createStoreZip output is readable by JVM ZipInputStream`() = runTest {
        val contentBytes = """{"hello":"world"}""".encodeToByteArray()
        val imageBytes = byteArrayOf(1, 2, 3)
        val entries = listOf(
            createTempEntry("content.json", contentBytes),
            createTempEntry("image.png", imageBytes),
        )

        val zip = createStoreZip(entries)
        val extractedContent = mutableMapOf<String, ByteArray>()
        java.util.zip.ZipInputStream(zip.inputStream()).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                extractedContent[entry.name] = zis.readBytes()
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }

        assertEquals(2, extractedContent.size)
        assertEquals(contentBytes.toList(), extractedContent["content.json"]?.toList())
        assertEquals(imageBytes.toList(), extractedContent["image.png"]?.toList())
    }

    @Test
    fun `createStoreZip with empty entry list produces valid empty ZIP`() = runTest {
        val zip = createStoreZip(emptyList())
        val extracted = fileManager.extractStoreZip(zip)
        assertEquals(emptyList(), extracted)
    }

    @Test
    fun `JVM ZipOutputStream STORED zip is readable by extractStoreZip`() = runTest {
        val contentBytes = """{"key":"value"}""".encodeToByteArray()
        val goblinBytes = byteArrayOf(1, 2, 3)
        val entries = listOf(
            createTempEntry("content.json", contentBytes),
            createTempEntry("goblin.png", goblinBytes),
        )
        val baos = java.io.ByteArrayOutputStream()
        java.util.zip.ZipOutputStream(baos).use { zos ->
            entries.forEach {
                val bytes = it.readBytes()
                val crc = java.util.zip.CRC32().also { c -> c.update(bytes) }
                val entry = java.util.zip.ZipEntry(it.name).also { e ->
                    e.method = java.util.zip.ZipEntry.STORED
                    e.size = bytes.size.toLong()
                    e.compressedSize = bytes.size.toLong()
                    e.crc = crc.value
                }
                zos.putNextEntry(entry)
                zos.write(bytes)
                zos.closeEntry()
            }
        }
        val extracted = fileManager.extractStoreZip(baos.toByteArray())
        assertEquals(2, extracted.size)
        assertEquals("content.json", extracted[0].name)
        assertEquals(contentBytes.toList(), extracted[0].readBytes().toList())
        assertEquals("goblin.png", extracted[1].name)
        assertEquals(goblinBytes.toList(), extracted[1].readBytes().toList())
    }
}
