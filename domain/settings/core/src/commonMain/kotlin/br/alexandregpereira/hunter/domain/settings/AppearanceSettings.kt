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
            "imageContentScale" to when (appearance.imageContentScale) {
                AppSettingsImageContentScale.Fit -> "Fit"
                AppSettingsImageContentScale.Crop -> "Crop"
            }
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
        val imageContentScale = settings.getString("imageContentScale").single()?.let {
            when (it) {
                "Fit" -> AppSettingsImageContentScale.Fit
                "Crop" -> AppSettingsImageContentScale.Crop
                else -> null
            }
        } ?: AppSettingsImageContentScale.Fit
        AppearanceSettings(
            forceLightImageBackground = forceLightImageBackground,
            defaultLightBackground = defaultLightBackground,
            defaultDarkBackground = defaultDarkBackground,
            imageContentScale = imageContentScale,
        ).let {
            emit(it)
        }
    }
}

data class AppearanceSettings(
    val forceLightImageBackground: Boolean,
    val defaultLightBackground: String,
    val defaultDarkBackground: String,
    val imageContentScale: AppSettingsImageContentScale,
)

enum class AppSettingsImageContentScale {
    Fit,
    Crop,
}
