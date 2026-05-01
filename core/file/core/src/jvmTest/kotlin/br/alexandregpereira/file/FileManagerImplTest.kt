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

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class FileManagerImplTest {

    @Test
    fun `saveFileToAppStorage sequential calls with same name leave only one file`() = runTest {
        val storage = mutableListOf<String>()
        val fileManager = FileManagerImpl(
            platformFileManager = FakeFileManager(storage),
            dispatcher = UnconfinedTestDispatcher(testScheduler),
        )

        fileManager.saveFileToAppStorage(byteArrayOf(1), "goblin", FileType.PNG)
        fileManager.saveFileToAppStorage(byteArrayOf(2), "goblin", FileType.PNG)

        val goblinFiles = storage.filter { it.startsWith("goblin") }
        assertEquals(1, goblinFiles.size, "Expected one file after two sequential saves, got: $goblinFiles")
    }

    @Test
    fun `saveFileToAppStorage concurrent calls with same name leave only one file`() = runTest {
        val storage = mutableListOf<String>()
        storage.add("goblin-existing.png")
        // Gate that suspends the first coroutine after it reads the file list, giving
        // the second coroutine a chance to attempt interleaving — the mutex prevents the race.
        val proceed = CompletableDeferred<Unit>()
        var listCallCount = 0

        val fakePlatform = object : FileManager {
            override suspend fun saveFileToAppStorage(
                bytes: ByteArray,
                fileName: String,
                fileType: FileType,
            ): String {
                storage.add(fileName)
                return "file://$fileName"
            }

            override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {
                storage.removeAll { it == fileName }
            }

            override suspend fun getFileNamesFromAppStorage(): List<String> {
                val result = storage.toList()
                listCallCount++
                if (listCallCount == 1) proceed.await()
                return result
            }
        }

        val fileManager = FileManagerImpl(
            platformFileManager = fakePlatform,
            dispatcher = UnconfinedTestDispatcher(testScheduler),
        )

        // Use UnconfinedTestDispatcher on launches so they start immediately (not deferred).
        // Coroutine A acquires the mutex, reads the list, then suspends at proceed.await().
        launch(UnconfinedTestDispatcher(testScheduler)) {
            fileManager.saveFileToAppStorage(byteArrayOf(1), "goblin", FileType.PNG)
        }
        // Coroutine B tries to acquire the mutex but blocks until A releases it.
        launch(UnconfinedTestDispatcher(testScheduler)) {
            fileManager.saveFileToAppStorage(byteArrayOf(2), "goblin", FileType.PNG)
        }
        // Unblock A — it finishes its save and releases the mutex, then B runs sequentially.
        proceed.complete(Unit)
        advanceUntilIdle()

        val goblinFiles = storage.filter { it.startsWith("goblin") }
        assertEquals(1, goblinFiles.size, "Expected exactly one file after concurrent saves, got: $goblinFiles")
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

        override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {
            storage.removeAll { it == fileName }
        }

        override suspend fun getFileNamesFromAppStorage(): List<String> = storage.toList()
    }
}
