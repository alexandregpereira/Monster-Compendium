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

package br.alexandregpereira.hunter.spell.detail

import br.alexandregpereira.hunter.domain.spell.GetSpellUseCase
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventListener
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class SpellDetailViewModel(
    private val getSpell: GetSpellUseCase,
    private val spellDetailEventListener: SpellDetailEventListener,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: SpellDetailAnalytics,
    private val appLocalization: AppLocalization,
) : UiModel<SpellDetailViewState>(SpellDetailViewState()) {

    private var spellIndex: String = ""

    init {
        observeEvents()
    }

    private fun loadSpell(spellIndex: String) {
        val strings = appLocalization.getStrings()
        getSpell(spellIndex)
            .map { spell -> spell.asState(strings) }
            .flowOn(dispatcher)
            .onEach { spell ->
                analytics.trackSpellLoaded(spell)
                setState { changeSpell(spell, strings) }
            }
            .catch {
                analytics.logException(it)
            }
            .launchIn(scope)
    }

    private fun observeEvents() {
        spellDetailEventListener.events
            .onEach { event ->
                when (event) {
                    is SpellDetailEvent.ShowSpell -> {
                        analytics.trackSpellShown(event.index)
                        spellIndex = event.index
                        loadSpell(event.index)
                    }
                }
            }
            .launchIn(scope)
    }

    fun onClose() {
        analytics.trackSpellClosed()
        setState { hideDetail() }
    }
}
