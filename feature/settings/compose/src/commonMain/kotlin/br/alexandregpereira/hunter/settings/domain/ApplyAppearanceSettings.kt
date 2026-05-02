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

package br.alexandregpereira.hunter.settings.domain

import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import br.alexandregpereira.hunter.domain.settings.AppearanceSettings
import br.alexandregpereira.hunter.domain.settings.SaveAppearanceSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal fun interface ApplyAppearanceSettings {
    operator fun invoke(
        appearance: AppearanceSettings,
    ): Flow<Unit>
}

internal fun ApplyAppearanceSettings(
    saveAppearanceSettings: SaveAppearanceSettings,
    monsterRepository: MonsterLocalRepository,
    monsterImageRepository: MonsterImageRepository,
): ApplyAppearanceSettings = ApplyAppearanceSettings { appearance ->
    saveAppearanceSettings(appearance)
        .map {
            val monsters = monsterRepository.getMonsterPreviews().firstOrNull().orEmpty()
            val monsterImages = monsterImageRepository.getLocalMonsterImages().associateBy {
                it.monsterIndex
            }
            val (mostCommonLight, mostCommonDark) = monsters.getMostCommonColors()
            monsters.mapNotNull { monster ->
                val light = getMonsterColorOrNewColor(
                    oldColor = mostCommonLight,
                    newColor = appearance.defaultLightBackground,
                    monsterColor = monster.imageData.backgroundColor.light
                )

                val dark = getMonsterColorOrNewColor(
                    oldColor = mostCommonDark,
                    newColor = appearance.defaultDarkBackground,
                    monsterColor = monster.imageData.backgroundColor.dark
                )

                val newMonsterImageData = monster.imageData.copy(
                    backgroundColor = monster.imageData.backgroundColor.copy(
                        light = light,
                        dark = dark
                    )
                )

                val imageColorsHasChanges = newMonsterImageData.backgroundColor != monster.imageData.backgroundColor
                if (imageColorsHasChanges) {
                    newMonsterImageData.toMonsterImage(
                        monsterIndex = monster.index,
                        monsterImage = monsterImages[monster.index],
                    )
                } else null
            }
        }
        .map { newMonsterImages ->
            monsterImageRepository.saveMonsterImages(newMonsterImages)
        }
}

private fun MonsterImageData.toMonsterImage(
    monsterIndex: String,
    monsterImage: MonsterImage?,
): MonsterImage {
    return MonsterImage(
        monsterIndex = monsterIndex,
        backgroundColor = backgroundColor,
        isHorizontalImage = monsterImage?.isHorizontalImage,
        imageUrl = monsterImage?.imageUrl,
        contentScale = monsterImage?.contentScale,
    )
}

internal fun getMonsterColorOrNewColor(
    oldColor: String,
    newColor: String,
    monsterColor: String,
): String {
    return newColor.takeIf {
        monsterColor.equals(oldColor, ignoreCase = true)
    } ?: monsterColor
}
