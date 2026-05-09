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
        zipFileManager = JvmZipFileManager(
            fileManager = fileManager,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `createZipFile and extractZipFile round-trip preserves all entries`() = runTest {
        val contentBytes = """{"key":"value"}""".encodeToByteArray()
        val goblinBytes = byteArrayOf(1, 2, 3)
        val orcBytes = byteArrayOf(4, 5, 6, 7)

        val contentFile = tempFolder.newFile("content.json").apply { writeBytes(contentBytes) }
        val goblinFile = tempFolder.newFile("goblin.png").apply { writeBytes(goblinBytes) }
        val orcFile = tempFolder.newFile("orc.webp").apply { writeBytes(orcBytes) }

        val entries = listOf(
            FileEntry("file://${contentFile.absolutePath}"),
            FileEntry("file://${goblinFile.absolutePath}"),
            FileEntry("file://${orcFile.absolutePath}"),
        )

        val path = zipFileManager.createZipFile(entries, "test.compendium")
        val zipBytes = FileEntry(path).readBytes()
        val extracted = zipFileManager.extractZipFile(zipBytes)

        assertEquals(3, extracted.size)
        assertEquals("content.json", extracted[0].name)
        assertEquals(contentBytes.toList(), extracted[0].readBytes().toList())
        assertEquals("goblin.png", extracted[1].name)
        assertEquals(goblinBytes.toList(), extracted[1].readBytes().toList())
        assertEquals("orc.webp", extracted[2].name)
        assertEquals(orcBytes.toList(), extracted[2].readBytes().toList())
    }
}
