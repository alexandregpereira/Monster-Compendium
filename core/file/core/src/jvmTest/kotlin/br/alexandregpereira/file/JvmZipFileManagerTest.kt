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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class JvmZipFileManagerTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var fileManager: JvmFileManager
    private lateinit var zipFileManager: JvmZipFileManager

    @Before
    fun setUp() {
        System.setProperty("user.home", tempFolder.root.absolutePath)
        fileManager = JvmFileManager(dispatcher = UnconfinedTestDispatcher())
        zipFileManager = JvmZipFileManager(dispatcher = UnconfinedTestDispatcher())
    }

    @Test
    fun `createZipFile and extractZipFile round-trip preserves all entries`() = runTest {
        val entries = listOf(
            FileEntry("content.json", """{"key":"value"}""".encodeToByteArray()),
            FileEntry("goblin.png", byteArrayOf(1, 2, 3)),
            FileEntry("orc.webp", byteArrayOf(4, 5, 6, 7)),
        )

        val path = zipFileManager.createZipFile(entries, "test.compendium")
        val zipFile = fileManager.getFileFromAppStorage(path)
        val extracted = zipFileManager.extractZipFile(zipFile.content)

        assertEquals(3, extracted.size)
        assertEquals("content.json", extracted[0].name)
        assertEquals(entries[0].content.toList(), extracted[0].content.toList())
        assertEquals("goblin.png", extracted[1].name)
        assertEquals(entries[1].content.toList(), extracted[1].content.toList())
        assertEquals("orc.webp", extracted[2].name)
        assertEquals(entries[2].content.toList(), extracted[2].content.toList())
    }
}
