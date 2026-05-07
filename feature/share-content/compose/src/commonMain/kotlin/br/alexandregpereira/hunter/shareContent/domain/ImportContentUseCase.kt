/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
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
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.monster.lore.SaveMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.spell.SaveSpells
import br.alexandregpereira.hunter.domain.usecase.SaveMonsterImages
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import br.alexandregpereira.hunter.shareContent.domain.model.ShareContent
import kotlinx.coroutines.flow.single

internal fun interface ImportContent {
    suspend operator fun invoke(compendiumFileContent: CompendiumFileContent): List<String>
}

internal class ImportContentUseCase(
    private val saveMonsters: SaveMonstersUseCase,
    private val saveSpells: SaveSpells,
    private val saveMonstersLore: SaveMonstersLoreUseCase,
    private val saveMonsterImages: SaveMonsterImages,
    private val fileManager: FileManager,
    private val analytics: Analytics,
) : ImportContent {

    override suspend fun invoke(compendiumFileContent: CompendiumFileContent): List<String> {
        val content = compendiumFileContent.shareContent
        compendiumFileContent.saveMonsters()
        compendiumFileContent.shareContent.saveMonstersLore()
        compendiumFileContent.shareContent.saveSpells()
        return content.monsters?.map { it.index }.orEmpty()
    }

    private suspend fun ShareContent.saveMonstersLore() {
        val monstersLore = this.monstersLore ?: return
        saveMonstersLore(
            monsterLore = monstersLore,
            isSync = false,
        ).single()
    }

    private suspend fun ShareContent.saveSpells() {
        val spells = this.spells ?: return
        saveSpells(spells).single()
    }

    private suspend fun CompendiumFileContent.saveMonsters() {
        val compendiumFileContent = this
        val monsters = compendiumFileContent.shareContent.monsters ?: return
        val originalImagePathsByMonsterIndex = saveOriginalImagesToAppStorage(
            monsters = monsters,
            monsterImageFiles = compendiumFileContent.monsterImageFiles,
        )

        try {
            val monsterWithUpdatedImages = monsters.map {
                it.copy(
                    imageData = it.imageData.copy(
                        url = originalImagePathsByMonsterIndex[it.index] ?: it.imageData.url
                    )
                )
            }
            saveMonsters(monsterWithUpdatedImages).single()
            compendiumFileContent.saveMonsterImages()
        } catch (cause: Throwable) {
            compendiumFileContent.monsterImageFiles.forEach { image ->
                fileManager.deleteFileFromAppStorage(
                    fileName = image.name,
                    fileType = FileType.IMAGE,
                )
            }
            throw cause
        }
    }

    private suspend fun CompendiumFileContent.saveMonsterImages() {
        val monsterImages = shareContent.monsterImages ?: return
        val imagePathsByMonsterIndex = saveCustomImagesToAppStorage(
            monsterImages = monsterImages,
            monsterImageFiles = this.monsterImageFiles,
        )
        val monsterImagesWithUpdatedUrls = monsterImages.map {
            it.copy(
                imageUrl = imagePathsByMonsterIndex[it.monsterIndex] ?: it.imageUrl
            )
        }
        saveMonsterImages(*monsterImagesWithUpdatedUrls.toTypedArray())
    }

    private suspend fun saveOriginalImagesToAppStorage(
        monsters: List<Monster>,
        monsterImageFiles: List<FileEntry>,
    ): Map<String, String> {
        val monsterIndexAndImageUrl = monsters.map {
            it.index to it.originalImageData.url
        }
        return saveImagesToAppStorage(
            monsterIndexAndImageUrl = monsterIndexAndImageUrl,
            monsterImageFiles = monsterImageFiles,
        )
    }

    private suspend fun saveCustomImagesToAppStorage(
        monsterImages: List<MonsterImage>,
        monsterImageFiles: List<FileEntry>,
    ): Map<String, String> {
        val monsterIndexAndImageUrl = monsterImages.mapNotNull {
            val imageUrl = it.imageUrl ?: return@mapNotNull null
            it.monsterIndex to imageUrl
        }
        return saveImagesToAppStorage(
            monsterIndexAndImageUrl = monsterIndexAndImageUrl,
            monsterImageFiles = monsterImageFiles,
        )
    }

    private suspend fun saveImagesToAppStorage(
        monsterIndexAndImageUrl: List<Pair<String, String>>,
        monsterImageFiles: List<FileEntry>,
    ): Map<String, String> {
        val monstersByImageName = monsterIndexAndImageUrl.filter { (_, imageUrl) ->
            imageUrl.startsWith("file://")
        }.associateBy { (_, imageUrl) ->
            imageUrl.substringAfterLast("/")
        }

        return monsterImageFiles
            .mapNotNull { image ->
                val (monsterIndex, _) = monstersByImageName[image.name] ?: return@mapNotNull null
                try {
                    val path = fileManager.saveFileToAppStorage(
                        bytes = image.content,
                        fileName = image.name,
                        fileType = FileType.IMAGE,
                    )
                    monsterIndex to path
                } catch (cause: Throwable) {
                    analytics.logException(
                        RuntimeException("Fail to import image ${image.name}", cause)
                    )
                    null
                }
            }.toMap()
    }
}

internal sealed class ImportContentException(message: String) : RuntimeException(message) {
    class InvalidContent(content: String, cause: Throwable) : ImportContentException(
        message = "SerializationException. " +
                "cause = ${cause.message}" +
                "content imported version= ${content.replace("\n", "")}"
    )
}
