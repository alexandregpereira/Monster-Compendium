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

import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEvent
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEventListener
import br.alexandregpereira.hunter.monster.lore.detail.domain.GetMonsterLoreDetailUseCase
import br.alexandregpereira.hunter.state.ScopeManager
import br.alexandregpereira.hunter.state.StateHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MonsterLoreDetailStateHolder internal constructor(
    stateRecovery: MonsterLoreDetailStateRecovery,
    private val getMonsterLoreUseCase: GetMonsterLoreDetailUseCase,
    private val monsterLoreDetailEventListener: MonsterLoreDetailEventListener,
    private val monsterLoreIndexStateRecovery: MonsterLoreIndexStateRecovery,
    private val dispatcher: CoroutineDispatcher
) : ScopeManager(), StateHolder<MonsterLoreDetailState> {

    private val _state = MutableStateFlow(stateRecovery.getState())
    override val state: StateFlow<MonsterLoreDetailState> = _state

    init {
        observeEvents()
        if (state.value.showDetail && state.value.monsterLore == null) {
            loadMonsterLore(monsterLoreIndexStateRecovery.getState())
        }
    }

    private fun loadMonsterLore(monsterLoreIndex: String) {
        getMonsterLoreUseCase(monsterLoreIndex)
            .flowOn(dispatcher)
            .onEach { monsterLore ->
                setState { changeMonsterLore(monsterLore) }
            }
            .catch {}
            .launchIn(scope)
    }

    private fun observeEvents() {
        monsterLoreDetailEventListener.events
            .onEach { event ->
                when (event) {
                    is MonsterLoreDetailEvent.Show -> {
                        monsterLoreIndexStateRecovery.saveState(event.index)
                        loadMonsterLore(event.index)
                    }
                }
            }
            .launchIn(scope)
    }

    fun onClose() {
        setState { hideDetail() }
    }

    private fun setState(block: MonsterLoreDetailState.() -> MonsterLoreDetailState) {
        _state.value = state.value.block()
    }
}
