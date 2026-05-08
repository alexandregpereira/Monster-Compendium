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

package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.FileType
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.factory.MonsterFactory
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import br.alexandregpereira.hunter.shareContent.domain.model.ShareContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ImportContentUseCaseTest {

    @Test
    fun `when saveMonsters throws, all previously saved image files are deleted`() = runTest {
        val savedFiles = mutableListOf<String>()
        val deletedFiles = mutableListOf<String>()

        val imageFile1 = FileEntry("goblin.png", byteArrayOf(1, 2, 3))
        val imageFile2 = FileEntry("orc.png", byteArrayOf(4, 5, 6))

        val monster1 = MonsterFactory.createEmpty("goblin").copy(
            imageData = MonsterFactory.createEmpty("goblin").imageData.copy(url = "file://goblin.png")
        )
        val monster2 = MonsterFactory.createEmpty("orc").copy(
            imageData = MonsterFactory.createEmpty("orc").imageData.copy(url = "file://orc.png")
        )

        val content = CompendiumFileContent(
            name = "test.compendium",
            shareContent = ShareContent(monsters = listOf(monster1, monster2), monstersLore = null, spells = null, monsterImages = null),
            monsterImages = listOf(
                CompendiumFileContent.MonsterImage(index = "goblin", name = "Goblin", file = imageFile1),
                CompendiumFileContent.MonsterImage(index = "orc", name = "Orc", file = imageFile2),
            ),
            sizeFormatted = "100 KB",
        )

        val useCase = ImportContentUseCase(
            saveMonsters = FailingSaveMonstersUseCase(),
            saveSpells = { flowOf(Unit) },
            saveMonstersLore = { _, _ -> flowOf(Unit) },
            saveMonsterImages = { },
            fileManager = TrackingFileManager(savedFiles, deletedFiles),
            analytics = NoOpAnalytics(),
        )

        val result = runCatching { useCase(content) }

        assertTrue(result.isFailure)
        assertEquals(setOf("goblin.png", "orc.png"), deletedFiles.toSet())
    }

    private class FailingSaveMonstersUseCase : SaveMonstersUseCase {
        override fun invoke(monsters: List<Monster>, isSync: Boolean): Flow<Unit> {
            throw RuntimeException("save failed")
        }
    }

    private class TrackingFileManager(
        private val saved: MutableList<String>,
        private val deleted: MutableList<String>,
    ) : FileManager {
        override suspend fun saveFileToAppStorage(bytes: ByteArray, fileName: String, fileType: FileType): String {
            saved.add(fileName)
            return "file://$fileName"
        }

        override suspend fun createZipFile(zipEntryFiles: List<FileEntry>, zipFileName: String): String =
            TODO("Not needed")

        override suspend fun getFileFromAppStorage(filePath: String): FileEntry =
            TODO("Not needed")

        override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {
            deleted.add(fileName)
        }

        override suspend fun deleteAllFilesFromAppStorage(fileType: FileType) {}

        override suspend fun getFileNamesFromAppStorage(fileType: FileType): List<String> = emptyList()

        override suspend fun extractZipFile(bytes: ByteArray): List<FileEntry> =
            TODO("Not needed")
    }

    private class NoOpAnalytics : Analytics {
        override fun track(eventName: String, params: Map<String, Any?>) {}
        override fun logException(throwable: Throwable) {}
    }
}
