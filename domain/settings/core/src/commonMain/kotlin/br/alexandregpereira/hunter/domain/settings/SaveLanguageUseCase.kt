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

import br.alexandregpereira.hunter.domain.settings.GetLanguageUseCase.Companion.SETTING_LANGUAGE_KEY
import br.alexandregpereira.hunter.localization.MutableAppLocalization
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class SaveLanguageUseCase(
    private val settingsRepository: SettingsRepository,
    private val mutableAppLanguage: MutableAppLocalization,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(lang: String): Flow<Unit> {
        if (IsLanguageSupported(lang).not()) return flowOf(Unit)
        return flow {
            emit(mutableAppLanguage.setLanguage(lang))
        }.flatMapLatest {
            settingsRepository.saveSettings(mapOf(SETTING_LANGUAGE_KEY to lang))
        }
    }
}
