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
