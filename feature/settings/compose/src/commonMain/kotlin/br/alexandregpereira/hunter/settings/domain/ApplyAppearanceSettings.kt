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
