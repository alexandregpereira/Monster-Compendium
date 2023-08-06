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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.spell.GetSpellUseCase
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class SpellDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getSpell: GetSpellUseCase,
    private val spellDetailEventListener: SpellDetailEventListener,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: SpellDetailAnalytics,
) : ViewModel() {

    private var spellIndex: String
        get() = savedStateHandle["spellIndex"] ?: ""
        set(value) {
            savedStateHandle["spellIndex"] = value
        }

    private val _state = MutableStateFlow(savedStateHandle.getState())
    val state: StateFlow<SpellDetailViewState> = _state

    init {
        observeEvents()
        if (state.value.showDetail && state.value.spell == null) {
            loadSpell(spellIndex)
        }
    }

    private fun loadSpell(spellIndex: String) {
        getSpell(spellIndex)
            .map { spell -> spell.asState() }
            .flowOn(dispatcher)
            .onEach { spell ->
                analytics.trackSpellLoaded(spell)
                setState { changeSpell(spell) }
            }
            .catch {
                analytics.logException(it)
            }
            .launchIn(viewModelScope)
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
            .launchIn(viewModelScope)
    }

    fun onClose() {
        analytics.trackSpellClosed()
        setState { hideDetail() }
    }

    private fun setState(block: SpellDetailViewState.() -> SpellDetailViewState) {
        _state.value = state.value.block().saveState(savedStateHandle)
    }
}
