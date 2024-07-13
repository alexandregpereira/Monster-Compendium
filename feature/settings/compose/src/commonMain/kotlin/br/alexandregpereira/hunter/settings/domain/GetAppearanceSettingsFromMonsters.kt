package br.alexandregpereira.hunter.settings.domain

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.settings.AppearanceSettings
import br.alexandregpereira.hunter.domain.settings.GetAppearanceSettings
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

internal fun interface GetAppearanceSettingsFromMonsters {
    operator fun invoke(): Flow<AppearanceSettings>
}

internal fun GetAppearanceSettingsFromMonsters(
    getAppearanceSettings: GetAppearanceSettings,
    getMonsters: GetMonsterPreviewsUseCase,
): GetAppearanceSettingsFromMonsters = GetAppearanceSettingsFromMonsters {
    flow {
        val appearanceSettings = getAppearanceSettings().single()
        emit(value = appearanceSettings)

        val monsters = getMonsters().single()
        val (mostCommonBackgroundLight, mostCommonBackgroundDark) = monsters.getMostCommonColors()

        appearanceSettings.copy(
            defaultLightBackground = mostCommonBackgroundLight,
            defaultDarkBackground = mostCommonBackgroundDark
        ).also {
            emit(value = it)
        }
    }
}

internal fun List<Monster>.getMostCommonColors(): Pair<String, String> {
    val mostCommonBackgroundLight = this
        .map { it.imageData.backgroundColor.light }
        .groupingBy { it }
        .eachCount()
        .maxBy { it.value }.key

    val mostCommonBackgroundDark = this.map { it.imageData.backgroundColor.dark }
        .groupingBy { it }
        .eachCount()
        .maxBy { it.value }.key

    return mostCommonBackgroundLight to mostCommonBackgroundDark
}
