/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022. Alexandre Gomes Pereira.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
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

import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge

class SaveUrlsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val settingsMonsterDataRepository: SettingsMonsterDataRepository,
    private val settingsSpellDataRepository: SettingsSpellDataRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    operator fun invoke(imageBaseUrl: String, alternativeSourceBaseUrl: String): Flow<Unit> {
        return settingsRepository.saveSettings(
            mapOf(
                IMAGE_BASE_URL_KEY to imageBaseUrl.normalizeUrl(),
                ALTERNATIVE_SOURCE_BASE_URL_KEY to alternativeSourceBaseUrl.normalizeUrl()
            )
        ).flatMapLatest {
            settingsMonsterDataRepository.deleteData()
                .flatMapMerge { settingsSpellDataRepository.deleteData() }
        }
    }

    private fun String.normalizeUrl(): String {
        return this.removeSuffix("/")
    }

    companion object {
        internal const val IMAGE_BASE_URL_KEY = "imageBaseUrl"
        internal const val ALTERNATIVE_SOURCE_BASE_URL_KEY = "alternativeSourceBaseUrl"
    }
}
