package br.alexandregpereira.hunter.domain.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

fun interface SaveAppearanceSettings {
    operator fun invoke(appearance: AppearanceSettings): Flow<Unit>
}

fun interface GetAppearanceSettings {
    operator fun invoke(): Flow<AppearanceSettings>
}

internal fun SaveAppearanceSettings(
    settings: SettingsRepository,
) = SaveAppearanceSettings { appearance ->
    settings.saveSettings(
        mapOf(
            "forceLightImageBackground" to appearance.forceLightImageBackground.toString(),
            "defaultLightBackground" to appearance.defaultLightBackground,
            "defaultDarkBackground" to appearance.defaultDarkBackground,
        )
    )
}

internal fun GetAppearanceSettings(
    settings: SettingsRepository,
) = GetAppearanceSettings {
    flow {
        val forceLightImageBackground = settings.getString("forceLightImageBackground")
            .single()?.toBoolean() ?: false
        val defaultLightBackground = settings.getString("defaultLightBackground").single() ?: ""
        val defaultDarkBackground = settings.getString("defaultDarkBackground").single() ?: ""
        AppearanceSettings(
            forceLightImageBackground = forceLightImageBackground,
            defaultLightBackground = defaultLightBackground,
            defaultDarkBackground = defaultDarkBackground,
        ).let {
            emit(it)
        }
    }
}

data class AppearanceSettings(
    val forceLightImageBackground: Boolean = false,
    val defaultLightBackground: String = "",
    val defaultDarkBackground: String = "",
)
