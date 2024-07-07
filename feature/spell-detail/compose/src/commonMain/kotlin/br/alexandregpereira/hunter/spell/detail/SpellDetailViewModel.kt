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
