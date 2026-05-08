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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
internal class MonsterRegistrationFileManagerImplTest {

    @Test
    fun `saveImage sequential calls with same name leave only one file`() = runTest {
        val storage = mutableListOf<String>()
        val fileManager = MonsterRegistrationFileManagerImpl(
            fileManager = FakeFileManager(storage),
            clock = FakeClock(),
        )

        fileManager.saveImage(byteArrayOf(1), "goblin")
        fileManager.saveImage(byteArrayOf(2), "goblin")

        val goblinFiles = storage.filter { it.startsWith("goblin") }
        assertEquals(1, goblinFiles.size, "Expected one file after two sequential saves, got: $goblinFiles")
    }

    @Test
    fun `saveImage saves file with timestamp suffix`() = runTest {
        val storage = mutableListOf<String>()
        val fileManager = MonsterRegistrationFileManagerImpl(
            fileManager = FakeFileManager(storage),
            clock = FakeClock(),
        )

        val path = fileManager.saveImage(byteArrayOf(1), "goblin")

        assertTrue(storage.single().matches(Regex("goblin-\\d+\\.png")), "Expected timestamped filename, got: ${storage.single()}")
        assertTrue(path.startsWith("file://"), "Expected file:// path, got: $path")
    }

    @Test
    fun `deleteLastSavedImageIfExists deletes the last saved file`() = runTest {
        val storage = mutableListOf<String>()
        val fileManager = MonsterRegistrationFileManagerImpl(
            fileManager = FakeFileManager(storage),
            clock = FakeClock(),
        )

        fileManager.saveImage(byteArrayOf(1), "goblin")
        assertEquals(1, storage.size)

        fileManager.deleteLastSavedImageIfExists()

        assertTrue(storage.isEmpty(), "Expected storage to be empty after delete, got: $storage")
    }

    @Test
    fun `deleteLastSavedImageIfExists when no image was saved is a no-op`() = runTest {
        val storage = mutableListOf<String>()
        val fileManager = MonsterRegistrationFileManagerImpl(FakeFileManager(storage))

        fileManager.deleteLastSavedImageIfExists()

        assertTrue(storage.isEmpty(), "Expected storage to remain empty")
    }

    @Test
    fun `deleteLastSavedImageIfExists after saveImage failure leaves no tracked file`() = runTest {
        val storage = mutableListOf<String>()
        val fileManager = MonsterRegistrationFileManagerImpl(
            fileManager = FailingOnSaveFileManager(storage),
            clock = FakeClock(),
        )

        runCatching { fileManager.saveImage(byteArrayOf(1), "goblin") }

        fileManager.deleteLastSavedImageIfExists()

        assertTrue(storage.isEmpty(), "Expected no file to be tracked or deleted after a failed save")
    }

    @Test
    fun `clear resets lastFileSaved so subsequent deleteLastSaved is a no-op`() = runTest {
        val storage = mutableListOf<String>()
        val fileManager = MonsterRegistrationFileManagerImpl(
            fileManager = FakeFileManager(storage),
            clock = FakeClock(),
        )

        fileManager.saveImage(byteArrayOf(1), "goblin")
        fileManager.clear()

        fileManager.deleteLastSavedImageIfExists()

        // deleteLastSavedImageIfExists is then a no-op because lastFileSaved was already cleared.
        val goblinFiles = storage.filter { it.startsWith("goblin") }
        assertEquals(1, goblinFiles.size, "File should remain since lastFileSaved was cleared by deleteImageIfExists(null)")
    }

    private class FakeFileManager(private val storage: MutableList<String>) : FileManager {

        override suspend fun saveFileToAppStorage(
            bytes: ByteArray,
            fileName: String,
            fileType: FileType,
        ): String {
            storage.add(fileName)
            return "file://$fileName"
        }

        override suspend fun createZipFile(
            zipEntryFiles: List<FileEntry>,
            zipFileName: String
        ): String {
            TODO("Not yet implemented")
        }

        override suspend fun getFileFromAppStorage(filePath: String): FileEntry {
            TODO("Not yet implemented")
        }

        override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {
            storage.removeAll { it == fileName }
        }

        override suspend fun deleteAllsFilesFromAppStorage(fileType: FileType) {
            TODO("Not yet implemented")
        }

        override suspend fun getFileNamesFromAppStorage(fileType: FileType): List<String> = storage.toList()
        override suspend fun extractZipFile(bytes: ByteArray): List<FileEntry> {
            TODO("Not yet implemented")
        }
    }

    private class FailingOnSaveFileManager(private val storage: MutableList<String>) : FileManager {

        override suspend fun saveFileToAppStorage(
            bytes: ByteArray,
            fileName: String,
            fileType: FileType,
        ): String = throw RuntimeException("disk full")

        override suspend fun createZipFile(
            zipEntryFiles: List<FileEntry>,
            zipFileName: String
        ): String {
            TODO("Not yet implemented")
        }

        override suspend fun getFileFromAppStorage(filePath: String): FileEntry {
            TODO("Not yet implemented")
        }

        override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {
            storage.removeAll { it == fileName }
        }

        override suspend fun deleteAllsFilesFromAppStorage(fileType: FileType) {
            TODO("Not yet implemented")
        }

        override suspend fun getFileNamesFromAppStorage(fileType: FileType): List<String> = storage.toList()
        override suspend fun extractZipFile(bytes: ByteArray): List<FileEntry> {
            TODO("Not yet implemented")
        }
    }

    private class FakeClock : Clock {
        private var time = 0L
        override fun now(): Instant = Instant.fromEpochMilliseconds(time++)
    }
}
