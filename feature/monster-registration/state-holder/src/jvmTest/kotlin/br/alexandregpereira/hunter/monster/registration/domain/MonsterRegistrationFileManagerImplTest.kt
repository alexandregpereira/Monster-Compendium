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

package br.alexandregpereira.hunter.monster.registration.domain

import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.FileType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
internal class MonsterRegistrationFileManagerImplTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var originalUserHome: String

    @Before
    fun setUp() {
        originalUserHome = System.getProperty("user.home")
        System.setProperty("user.home", tempFolder.root.absolutePath)
    }

    @After
    fun tearDown() {
        System.setProperty("user.home", originalUserHome)
    }

    private fun imagesDir(): File = File(tempFolder.root, ".monster-compendium/images")

    private fun createSourceFile(name: String, bytes: ByteArray = byteArrayOf(1)): FileEntry {
        val file = tempFolder.newFile(name).apply { writeBytes(bytes) }
        return FileEntry("file://${file.absolutePath}")
    }

    @Test
    fun `saveImage sequential calls with same name leave only one file`() = runTest {
        val fileManager = MonsterRegistrationFileManagerImpl(
            fileManager = RealFileManager(),
            clock = FakeClock(),
        )

        fileManager.saveImage(createSourceFile("source1.png", byteArrayOf(1)), "goblin")
        fileManager.saveImage(createSourceFile("source2.png", byteArrayOf(2)), "goblin")

        val goblinFiles = imagesDir().listFiles()?.filter { it.name.startsWith("goblin") }.orEmpty()
        assertEquals(
            1,
            goblinFiles.size,
            "Expected one file after two sequential saves, got: $goblinFiles"
        )
    }

    @Test
    fun `saveImage saves file with timestamp suffix`() = runTest {
        val fileManager = MonsterRegistrationFileManagerImpl(
            fileManager = RealFileManager(),
            clock = FakeClock(),
        )

        val path = fileManager.saveImage(createSourceFile("source.png"), "goblin")

        val savedFile = imagesDir().listFiles()?.singleOrNull()
        assertEquals(
            savedFile?.name?.matches(Regex("goblin-\\d+\\.png")),
            true,
            "Expected timestamped filename, got: ${savedFile?.name}"
        )
        assertTrue(path.startsWith("file://"), "Expected file:// path, got: $path")
    }

    @Test
    fun `deleteLastSavedImageIfExists deletes the last saved file`() = runTest {
        val fileManager = MonsterRegistrationFileManagerImpl(
            fileManager = RealFileManager(),
            clock = FakeClock(),
        )

        fileManager.saveImage(createSourceFile("source.png"), "goblin")
        assertEquals(1, imagesDir().listFiles()?.size ?: 0)

        fileManager.deleteLastSavedImageIfExists()

        assertTrue(
            imagesDir().listFiles().isNullOrEmpty(),
            "Expected images dir to be empty after delete"
        )
    }

    @Test
    fun `deleteLastSavedImageIfExists when no image was saved is a no-op`() = runTest {
        val fileManager = MonsterRegistrationFileManagerImpl(RealFileManager())

        fileManager.deleteLastSavedImageIfExists()

        assertTrue(
            imagesDir().listFiles().isNullOrEmpty(),
            "Expected images dir to remain empty"
        )
    }

    @Test
    fun `deleteLastSavedImageIfExists after saveImage failure leaves no tracked file`() = runTest {
        val fileManager = MonsterRegistrationFileManagerImpl(
            fileManager = RealFileManager(),
            clock = FakeClock(),
        )

        // Non-existent source file causes copyToAppStorage to fail
        runCatching {
            fileManager.saveImage(FileEntry("file:///nonexistent/goblin.png"), "goblin")
        }

        fileManager.deleteLastSavedImageIfExists()

        assertTrue(
            imagesDir().listFiles().isNullOrEmpty(),
            "Expected no file to be tracked or deleted after a failed save"
        )
    }

    @Test
    fun `clear resets lastFileSaved so subsequent deleteLastSaved is a no-op`() = runTest {
        val fileManager = MonsterRegistrationFileManagerImpl(
            fileManager = RealFileManager(),
            clock = FakeClock(),
        )

        fileManager.saveImage(createSourceFile("source.png"), "goblin")
        fileManager.clear()

        fileManager.deleteLastSavedImageIfExists()

        val goblinFiles = imagesDir().listFiles()?.filter { it.name.startsWith("goblin") }.orEmpty()
        assertEquals(
            1,
            goblinFiles.size,
            "File should remain since lastFileSaved was cleared before deleteLastSavedImageIfExists"
        )
    }

    private inner class RealFileManager : FileManager {
        override suspend fun saveFileToAppStorage(
            bytes: ByteArray,
            fileName: String,
            fileType: FileType,
        ): String {
            val dir = File(tempFolder.root, ".monster-compendium/${fileType.folder}").apply { mkdirs() }
            val file = File(dir, fileName).apply { writeBytes(bytes) }
            return "file://${file.absolutePath}"
        }

        override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {
            File(tempFolder.root, ".monster-compendium/${fileType.folder}/$fileName").delete()
        }

        override suspend fun deleteAllFilesFromAppStorage(fileType: FileType) {
            File(tempFolder.root, ".monster-compendium/${fileType.folder}").deleteRecursively()
        }
    }

    private class FakeClock : Clock {
        private var time = 0L
        override fun now(): Instant = Instant.fromEpochMilliseconds(time++)
    }
}
