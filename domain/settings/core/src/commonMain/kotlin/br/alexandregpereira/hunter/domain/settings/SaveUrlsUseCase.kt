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

class SaveUrlsUseCase(
    private val settingsRepository: SettingsRepository,
) {

    operator fun invoke(imageBaseUrl: String, alternativeSourceBaseUrl: String): Flow<Unit> {
        return settingsRepository.saveSettings(
            mapOf(
                IMAGE_BASE_URL_KEY to imageBaseUrl.normalizeUrl(),
                ALTERNATIVE_SOURCE_BASE_URL_KEY to alternativeSourceBaseUrl.normalizeUrl()
            )
        )
    }

    private fun String.normalizeUrl(): String {
        return this.removeSuffix("/")
    }

    companion object {
        internal const val IMAGE_BASE_URL_KEY = "imageBaseUrl"
        internal const val ALTERNATIVE_SOURCE_BASE_URL_KEY = "alternativeSourceBaseUrl"
    }
}
