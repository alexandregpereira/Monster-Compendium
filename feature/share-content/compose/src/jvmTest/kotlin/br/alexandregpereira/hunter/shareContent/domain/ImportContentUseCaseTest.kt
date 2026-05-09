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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ImportContentUseCaseTest {

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

    @Test
    fun `when saveMonsters throws, all previously saved image files are deleted`() = runTest {
        val goblinFile = File(tempFolder.root, "goblin.png").apply { writeBytes(byteArrayOf(1, 2, 3)) }
        val orcFile = File(tempFolder.root, "orc.png").apply { writeBytes(byteArrayOf(4, 5, 6)) }

        val imageFile1 = FileEntry("file://${goblinFile.absolutePath}")
        val imageFile2 = FileEntry("file://${orcFile.absolutePath}")

        val monster1 = MonsterFactory.createEmpty("goblin").copy(
            imageData = MonsterFactory.createEmpty("goblin").imageData.copy(url = "file://goblin.png")
        )
        val monster2 = MonsterFactory.createEmpty("orc").copy(
            imageData = MonsterFactory.createEmpty("orc").imageData.copy(url = "file://orc.png")
        )

        val content = CompendiumFileContent(
            name = "test.compendium",
            shareContent = ShareContent(
                monsters = listOf(monster1, monster2),
                monstersLore = null,
                spells = null,
                monsterImages = null
            ),
            monsterImages = listOf(
                CompendiumFileContent.MonsterImage(
                    index = "goblin",
                    name = "Goblin",
                    file = imageFile1
                ),
                CompendiumFileContent.MonsterImage(index = "orc", name = "Orc", file = imageFile2),
            ),
            contentInfo = CompendiumFileContentInfo(
                contentTitle = null,
                contentDescription = null,
                fileSizeFormatted = "100 KB"
            ),
        )

        val useCase = ImportContentUseCase(
            saveMonsters = FailingSaveMonstersUseCase(),
            saveSpells = { flowOf(Unit) },
            saveMonstersLore = { _, _ -> flowOf(Unit) },
            saveMonsterImages = { },
            fileManager = TrackingFileManager(),
            analytics = NoOpAnalytics(),
        )

        val result = runCatching { useCase(content) }

        assertTrue(result.isFailure)
        assertFalse(goblinFile.exists(), "Expected goblin.png source file to be deleted")
        assertFalse(orcFile.exists(), "Expected orc.png source file to be deleted")
    }

    private class FailingSaveMonstersUseCase : SaveMonstersUseCase {
        override fun invoke(monsters: List<Monster>, isSync: Boolean): Flow<Unit> {
            throw RuntimeException("save failed")
        }
    }

    private class TrackingFileManager : FileManager {
        override suspend fun saveFileToAppStorage(
            bytes: ByteArray,
            fileName: String,
            fileType: FileType
        ): String {
            return "file://$fileName"
        }

        override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {}

        override suspend fun deleteAllFilesFromAppStorage(fileType: FileType) {}
    }

    private class NoOpAnalytics : Analytics {
        override fun track(eventName: String, params: Map<String, Any?>) {}
        override fun logException(throwable: Throwable) {}
    }
}
