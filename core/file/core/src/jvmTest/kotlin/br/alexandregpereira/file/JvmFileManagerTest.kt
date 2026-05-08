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
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class JvmFileManagerTest {

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

    @Test
    fun `saveFileToAppStorage creates file and returns a file URI path`() = runTest {
        val bytes = byteArrayOf(1, 2, 3)

        val path = fileManager.saveFileToAppStorage(bytes, "goblin.png", FileType.IMAGE)

        assertTrue(path.startsWith("file://"), "Expected file:// path, got: $path")
        val file = File(path.removePrefix("file://"))
        assertTrue(file.exists(), "Expected file to exist at $path")
        assertEquals(bytes.toList(), file.readBytes().toList())
    }

    @Test
    fun `saveFileToAppStorage creates parent directory if missing`() = runTest {
        val imagesDir = File(tempFolder.root, ".monster-compendium/images")
        assertFalse(imagesDir.exists())

        fileManager.saveFileToAppStorage(byteArrayOf(1), "goblin.png", FileType.IMAGE)

        assertTrue(imagesDir.exists())
    }

    @Test
    fun `saveFileToAppStorage overwrites existing file with same name`() = runTest {
        val firstBytes = byteArrayOf(1, 2, 3)
        val secondBytes = byteArrayOf(4, 5, 6)

        fileManager.saveFileToAppStorage(firstBytes, "goblin.png", FileType.IMAGE)
        val path = fileManager.saveFileToAppStorage(secondBytes, "goblin.png", FileType.IMAGE)

        val file = File(path.removePrefix("file://"))
        assertEquals(secondBytes.toList(), file.readBytes().toList())
    }

    @Test
    fun `deleteFileFromAppStorage removes the file`() = runTest {
        fileManager.saveFileToAppStorage(byteArrayOf(1), "goblin.png", FileType.IMAGE)

        fileManager.deleteFileFromAppStorage("goblin.png", FileType.IMAGE)

        val file = File(tempFolder.root, ".monster-compendium/images/goblin.png")
        assertFalse(file.exists(), "Expected file to be deleted")
    }

    @Test
    fun `deleteFileFromAppStorage is a no-op when file does not exist`() = runTest {
        // Should not throw
        fileManager.deleteFileFromAppStorage("nonexistent.png", FileType.IMAGE)
    }

    @Test
    fun `getFileNamesFromAppStorage returns empty list when folder does not exist`() = runTest {
        val names = fileManager.getFileNamesFromAppStorage(FileType.IMAGE)

        assertEquals(emptyList(), names)
    }

    @Test
    fun `getFileNamesFromAppStorage returns file names in folder`() = runTest {
        fileManager.saveFileToAppStorage(byteArrayOf(1), "goblin.png", FileType.IMAGE)
        fileManager.saveFileToAppStorage(byteArrayOf(2), "orc.png", FileType.IMAGE)

        val names = fileManager.getFileNamesFromAppStorage(FileType.IMAGE)

        assertEquals(setOf("goblin.png", "orc.png"), names.toSet())
    }

    @Test
    fun `getFileNamesFromAppStorage returns only file names not full paths`() = runTest {
        fileManager.saveFileToAppStorage(byteArrayOf(1), "goblin.png", FileType.IMAGE)

        val names = fileManager.getFileNamesFromAppStorage(FileType.IMAGE)

        assertEquals(listOf("goblin.png"), names)
    }

    @Test
    fun `createZipFile and extractZipFile round-trip preserves all entries`() = runTest {
        val entries = listOf(
            FileEntry("content.json", """{"key":"value"}""".encodeToByteArray()),
            FileEntry("goblin.png", byteArrayOf(1, 2, 3)),
            FileEntry("orc.webp", byteArrayOf(4, 5, 6, 7)),
        )

        val path = fileManager.createZipFile(entries, "test.compendium")
        val zipFile = fileManager.getFileFromAppStorage(path)
        val extracted = fileManager.extractZipFile(zipFile.content)

        assertEquals(3, extracted.size)
        assertEquals("content.json", extracted[0].name)
        assertEquals(entries[0].content.toList(), extracted[0].content.toList())
        assertEquals("goblin.png", extracted[1].name)
        assertEquals(entries[1].content.toList(), extracted[1].content.toList())
        assertEquals("orc.webp", extracted[2].name)
        assertEquals(entries[2].content.toList(), extracted[2].content.toList())
    }
}
