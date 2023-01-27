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

package br.alexandregpereira.hunter.monster.lore.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEvent
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEventListener
import br.alexandregpereira.hunter.monster.lore.detail.domain.GetMonsterLoreDetailUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class MonsterLoreDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getMonsterLoreUseCase: GetMonsterLoreDetailUseCase,
    private val monsterLoreDetailEventListener: MonsterLoreDetailEventListener,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private var monsterLoreIndex: String
        get() = savedStateHandle["monsterLoreIndex"] ?: ""
        set(value) {
            savedStateHandle["monsterLoreIndex"] = value
        }

    private val _state = MutableStateFlow(savedStateHandle.getState())
    val state: StateFlow<MonsterLoreDetailViewState> = _state

    init {
        observeEvents()
        if (state.value.showDetail && state.value.monsterLore == null) {
            loadMonsterLore(monsterLoreIndex)
        }
    }

    private fun loadMonsterLore(monsterLoreIndex: String) {
        getMonsterLoreUseCase(monsterLoreIndex)
            .map { monsterLore -> monsterLore.asState() }
            .flowOn(dispatcher)
            .onEach { monsterLore ->
                setState { changeMonsterLore(monsterLore) }
            }
            .catch {}
            .launchIn(viewModelScope)
    }

    private fun observeEvents() {
        monsterLoreDetailEventListener.events
            .onEach { event ->
                when (event) {
                    is MonsterLoreDetailEvent.Show -> {
                        monsterLoreIndex = event.index
                        loadMonsterLore(event.index)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onClose() {
        setState { hideDetail() }
    }

    private fun setState(block: MonsterLoreDetailViewState.() -> MonsterLoreDetailViewState) {
        _state.value = state.value.block().saveState(savedStateHandle)
    }
}
