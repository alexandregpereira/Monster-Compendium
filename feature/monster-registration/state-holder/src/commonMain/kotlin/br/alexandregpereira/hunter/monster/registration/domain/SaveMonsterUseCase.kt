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

package br.alexandregpereira.hunter.monster.registration.domain

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.monster.lore.SaveMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreEntry
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreStatus
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import br.alexandregpereira.hunter.domain.settings.AppSettingsImageContentScale
import br.alexandregpereira.hunter.domain.settings.GetAppearanceSettings
import br.alexandregpereira.hunter.domain.usecase.SaveMonsterImages
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull

internal fun interface SaveMonsterUseCase {
    suspend operator fun invoke(
        monster: Monster,
        previousMonster: Monster?,
        monsterLoreEntries: List<MonsterLoreEntry>,
        originalMonsterLore: MonsterLore?,
    )
}

internal class SaveMonsterUseCaseImpl(
    private val saveMonsters: SaveMonstersUseCase,
    private val monsterImageRepository: MonsterImageRepository,
    private val saveMonsterImages: SaveMonsterImages,
    private val saveMonstersLoreUseCase: SaveMonstersLoreUseCase,
    private val monsterLocalRepository: MonsterLocalRepository,
    private val getAppearanceSettings: GetAppearanceSettings,
) : SaveMonsterUseCase {

    override suspend fun invoke(
        monster: Monster,
        previousMonster: Monster?,
        monsterLoreEntries: List<MonsterLoreEntry>,
        originalMonsterLore: MonsterLore?,
    ) {
        val currentLocalMonster = getCurrentLocalMonster(monster.index)
        monster.changeStatus(previousMonster)
            .rollbackImageData(currentLocalMonster)
            .let { newMonster ->
                saveMonsters(monsters = listOf(newMonster)).collect()
                saveMonsterLore(newMonster, monsterLoreEntries, originalMonsterLore)
            }

        saveMonsterImage(
            previousMonster = previousMonster,
            monster = monster,
            currentLocalMonster = currentLocalMonster,
        )
    }

    private suspend fun saveMonsterImage(
        previousMonster: Monster?,
        monster: Monster,
        currentLocalMonster: Monster?,
    ) {
        val previousImageData = previousMonster?.imageData
        val newImageData = monster.imageData
        val alreadyExistMonster = currentLocalMonster != null
        if (alreadyExistMonster && previousImageData != newImageData) {
            val currentLocalMonsterImage = monsterImageRepository.getLocalMonsterImage(
                monsterIndex = monster.index
            )
            saveMonsterImageIfIsDifferent(
                newMonster = monster,
                currentLocalMonster = currentLocalMonster,
                currentLocalMonsterImage = currentLocalMonsterImage,
            )
        }
    }

    private fun Monster.changeStatus(
        previousMonster: Monster?
    ): Monster {
        val originalImageData = previousMonster?.imageData
        val monsterWithoutImageDataToCompare = this.copy(
            imageData = originalImageData ?: this.imageData
        )
        return when (this.status) {
            MonsterStatus.Original -> {
                val status = if (monsterWithoutImageDataToCompare != previousMonster) {
                    MonsterStatus.Edited
                } else MonsterStatus.Original

                this.copy(status = status)
            }

            MonsterStatus.Edited,
            MonsterStatus.Imported,
            MonsterStatus.Clone -> this
        }
    }

    private fun Monster.rollbackImageData(
        currentLocalMonster: Monster?,
    ): Monster {
        return copy(
            imageData = currentLocalMonster?.imageData ?: imageData
        )
    }

    private suspend fun getCurrentLocalMonster(index: String): Monster? {
        return monsterLocalRepository.getMonsterPreview(index)
    }

    private suspend fun saveMonsterLore(
        monster: Monster,
        monsterLoreEntries: List<MonsterLoreEntry>,
        originalMonsterLore: MonsterLore?,
    ) {
        val hasChangeLore = monsterLoreEntries != originalMonsterLore?.entries

        val monsterLoreList = listOf(
            MonsterLore(
                index = monster.index,
                name = monster.name,
                entries = monsterLoreEntries,
                status = if (hasChangeLore) {
                    MonsterLoreStatus.Edited
                } else {
                    originalMonsterLore.status
                },
            )
        )
        saveMonstersLoreUseCase(
            monsterLore = monsterLoreList,
            isSync = false
        ).collect()
    }

    private suspend fun saveMonsterImageIfIsDifferent(
        newMonster: Monster,
        currentLocalMonster: Monster,
        currentLocalMonsterImage: MonsterImage?,
    ) {
        val imageData = newMonster.imageData
        val backgroundColor = imageData.backgroundColor.takeImageValueIfIsDifferent(
            currentLocalMonsterValue = currentLocalMonster.imageData.backgroundColor,
            currentLocalMonsterImageValue = currentLocalMonsterImage?.backgroundColor,
        )
        val isHorizontalImage = imageData.isHorizontal.takeImageValueIfIsDifferent(
            currentLocalMonsterValue = currentLocalMonster.imageData.isHorizontal,
            currentLocalMonsterImageValue = currentLocalMonsterImage?.isHorizontalImage,
        )
        val imageUrl = imageData.url.takeImageValueIfIsDifferent(
            currentLocalMonsterValue = currentLocalMonster.imageData.url,
            currentLocalMonsterImageValue = currentLocalMonsterImage?.imageUrl,
        )
        val currentImageContentScaleFromSettings = getAppearanceSettings()
            .firstOrNull()
            ?.imageContentScale
            ?.toImageContentScale()
        val contentScale = imageData.contentScale.takeImageValueIfIsDifferent(
            currentLocalMonsterValue = currentLocalMonster.imageData.contentScale,
            currentLocalMonsterImageValue = currentLocalMonsterImage?.contentScale,
            currentValueFromSettings = currentImageContentScaleFromSettings,
        )
        val newMonsterImage = MonsterImage(
            monsterIndex = newMonster.index,
            backgroundColor = backgroundColor,
            isHorizontalImage = isHorizontalImage,
            imageUrl = imageUrl,
            contentScale = contentScale,
        )
        saveMonsterImages(newMonsterImage)
    }

    private fun <Value> Value.takeImageValueIfIsDifferent(
        currentLocalMonsterValue: Value?,
        currentLocalMonsterImageValue: Value?,
        currentValueFromSettings: Value? = null,
    ): Value? {
        return this.let { value ->
            val currentLocalMonsterOrSettingsValue = currentLocalMonsterValue
                ?: currentValueFromSettings
            if (currentLocalMonsterImageValue == null) {
                value.takeIf {
                    it != currentLocalMonsterOrSettingsValue
                }
            } else {
                val newValue = value.takeIf {
                    it != currentLocalMonsterImageValue
                } ?: currentLocalMonsterImageValue

                newValue.takeIf {
                    it != currentLocalMonsterOrSettingsValue
                }
            }
        }
    }

    private fun AppSettingsImageContentScale.toImageContentScale(): MonsterImageContentScale? {
        return when (this) {
            AppSettingsImageContentScale.Fit -> MonsterImageContentScale.Fit
            AppSettingsImageContentScale.Crop -> MonsterImageContentScale.Crop
        }
    }
}
