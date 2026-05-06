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

import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.FileType
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.monster.lore.SaveMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import br.alexandregpereira.hunter.domain.spell.SaveSpells
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.single

internal fun interface ImportContent {
    suspend operator fun invoke(compendiumFileContent: CompendiumFileContent): List<String>
}

internal class ImportContentUseCase(
    private val saveMonsters: SaveMonstersUseCase,
    private val saveSpells: SaveSpells,
    private val saveMonstersLore: SaveMonstersLoreUseCase,
    private val monsterImageRepository: MonsterImageRepository,
    private val fileManager: FileManager,
    private val analytics: Analytics,
) : ImportContent {

    override suspend fun invoke(compendiumFileContent: CompendiumFileContent): List<String> {
        val content = compendiumFileContent.shareContent
        content.monsters?.let { monsters ->
            val monstersByImageName = monsters.filter {
                it.imageData.url.startsWith("file://")
            }.associateBy {
                it.imageData.url.substringAfterLast("/")
            }

            val pathsByMonsterIndex = compendiumFileContent.monsterImageFiles
                .mapNotNull { image ->
                    val monster = monstersByImageName[image.name] ?: return@mapNotNull null
                    try {
                        val path = fileManager.saveFileToAppStorage(
                            bytes = image.content,
                            fileName = image.name,
                            fileType = FileType.IMAGE,
                        )
                        monster.index to path
                    } catch (cause: Throwable) {
                        analytics.logException(
                            RuntimeException("Fail to import image ${image.name}", cause)
                        )
                        null
                    }
                }.toMap()

            saveMonsters(
                monsters.map {
                    it.copy(
                        imageData = it.imageData.copy(
                            url =pathsByMonsterIndex[it.index] ?: it.imageData.url
                        )
                    )
                }
            ).catch { cause ->
                compendiumFileContent.monsterImageFiles.forEach { image ->
                    fileManager.deleteFileFromAppStorage(
                        fileName = image.name,
                        fileType = FileType.IMAGE,
                    )
                }
                throw cause
            }.single()

            pathsByMonsterIndex.keys.takeIf { it.isNotEmpty() }?.toList()?.let {
                monsterImageRepository.deleteMonsterImages(monsterIndexes = it)
            }
        }
        content.monstersLore?.let { monstersLore ->
            saveMonstersLore(
                monsterLore = monstersLore,
                isSync = false,
            ).single()
        }
        content.spells?.let { spells ->
            saveSpells(spells).single()
        }

        return content.monsters?.map { it.index }.orEmpty()
    }
}

internal sealed class ImportContentException(message: String) : RuntimeException(message) {
    class InvalidContent(content: String, cause: Throwable) : ImportContentException(
        message = "SerializationException. " +
                "cause = ${cause.message}" +
                "content imported version= ${content.replace("\n", "")}"
    )
}
