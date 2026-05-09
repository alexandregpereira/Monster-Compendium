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

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class FileEntryTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    @Test
    fun `filePath on a real file returns file-scheme prefixed absolute path`() {
        val file = tempFolder.newFile("monsters.json")
        val entry = FileEntry("file://${file.absolutePath}")
        assertEquals("file://${file.absolutePath}", entry.filePath)
        assertFalse(entry.filePath.contains("file://file://"))
    }

    @Test
    fun `filePath on a path without file-scheme prefix adds file scheme`() {
        val file = tempFolder.newFile("monsters.json")
        val entry = FileEntry(file.absolutePath)
        assertEquals("file://${file.absolutePath}", entry.filePath)
    }

    @Test
    fun `filePath does not double-prefix when path already starts with file-scheme`() {
        val file = tempFolder.newFile("test.compendium")
        val entry = FileEntry("file://${file.absolutePath}")
        assertFalse(entry.filePath.startsWith("file://file://"))
        assertEquals("file://${file.absolutePath}", entry.filePath)
    }

    @Test
    fun `filePath never produces a double file-scheme prefix`() {
        // Ensure that no matter how the path is constructed, file:// is never doubled.
        val file = tempFolder.newFile("double.json")
        val entry1 = FileEntry("file://${file.absolutePath}")
        val entry2 = FileEntry(file.absolutePath)
        assertFalse(entry1.filePath.startsWith("file://file://"))
        assertFalse(entry2.filePath.startsWith("file://file://"))
        assertEquals(entry1.filePath, entry2.filePath)
    }
}
