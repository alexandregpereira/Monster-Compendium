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

package br.alexandregpereira.hunter.home

import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * The view state is mocked by [homeMockViewState] until the Home is connected to the domain layer.
 */
internal class HomeStateHolder(
    private val appLocalization: AppReactiveLocalization,
    private val homeEventDispatcher: HomeEventDispatcher,
) : UiModel<HomeState>(
    HomeState(
        viewState = homeMockViewState,
        strings = appLocalization.getHomeStrings(),
    )
) {

    private var intentJob: Job? = null

    init {
        observeLanguageChanges()
    }

    fun onIntent(intent: HomeIntent) {
        // An intent can keep collecting results while its screen is open, like the spell
        // compendium clicks. Those screens cover the Home, so when a new intent arrives the
        // previous screen is already closed and its collection can be cancelled.
        intentJob?.cancel()
        intentJob = featureScope.launch {
            homeEventDispatcher.onIntent(intent)
        }
    }

    private fun observeLanguageChanges() {
        appLocalization.languageFlow
            .onEach { language ->
                setState { copy(strings = language.getHomeStrings()) }
            }
            .launchIn(scope)
    }
}
