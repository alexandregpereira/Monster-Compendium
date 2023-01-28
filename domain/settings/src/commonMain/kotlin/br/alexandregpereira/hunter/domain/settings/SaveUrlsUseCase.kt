/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.domain.settings

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge

class SaveUrlsUseCase(
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
