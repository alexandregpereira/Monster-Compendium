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

import br.alexandregpereira.hunter.domain.repository.MonsterCacheRepository
import br.alexandregpereira.hunter.domain.settings.AppearanceSettings
import br.alexandregpereira.hunter.domain.settings.SaveAppearanceSettings
import br.alexandregpereira.hunter.domain.usecase.GetMonstersUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

internal fun interface ApplyAppearanceSettings {
    operator fun invoke(
        appearance: AppearanceSettings,
    ): Flow<Unit>
}

internal fun ApplyAppearanceSettings(
    saveAppearanceSettings: SaveAppearanceSettings,
    saveMonstersUseCase: SaveMonstersUseCase,
    monsterCacheRepository: MonsterCacheRepository,
    getMonsters: GetMonstersUseCase,
): ApplyAppearanceSettings = ApplyAppearanceSettings { appearance ->
    saveAppearanceSettings(appearance)
        .map {
            val monsters = getMonsters().single()
            val (mostCommonLight, mostCommonDark) = monsters.getMostCommonColors()
            monsters.map { monster ->
                val light = getMonsterColorOrNewColor(
                    oldColor = mostCommonLight,
                    newColor = appearance.defaultLightBackground,
                    monsterColor = monster.imageData.backgroundColor.light
                )

                val dark = getMonsterColorOrNewColor(
                    oldColor = mostCommonDark,
                    newColor = if (appearance.forceLightImageBackground) {
                        appearance.defaultLightBackground
                    } else {
                        appearance.defaultDarkBackground
                    },
                    monsterColor = monster.imageData.backgroundColor.dark
                )

                monster.copy(
                    imageData = monster.imageData.copy(
                        backgroundColor = monster.imageData.backgroundColor.copy(
                            light = light,
                            dark = dark
                        )
                    )
                )
            }
        }
        .map { newMonsters ->
            saveMonstersUseCase(newMonsters).single()
            monsterCacheRepository.saveMonsters(newMonsters).single()
        }
}

private fun getMonsterColorOrNewColor(
    oldColor: String,
    newColor: String,
    monsterColor: String,
): String {
    return newColor.takeIf { monsterColor.lowercase() == oldColor.lowercase() } ?: monsterColor
}
