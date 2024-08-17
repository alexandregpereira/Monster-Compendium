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
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MonsterLoreDetailStateHolder internal constructor(
    private val stateRecovery: StateRecovery,
    private val getMonsterLoreUseCase: GetMonsterLoreDetailUseCase,
    private val monsterLoreDetailEventListener: MonsterLoreDetailEventListener,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: MonsterLoreDetailAnalytics,
) : UiModel<MonsterLoreDetailState>(stateRecovery.getState()) {

    init {
        observeEvents()
        if (state.value.showDetail && state.value.monsterLore == null) {
            loadMonsterLore(stateRecovery.monsterLoreIndex)
        }
    }

    private fun loadMonsterLore(monsterLoreIndex: String) {
        getMonsterLoreUseCase(monsterLoreIndex)
            .flowOn(dispatcher)
            .onEach { monsterLore ->
                analytics.trackMonsterLoreDetailLoaded(monsterLore)
                setState { changeMonsterLore(monsterLore).saveState(stateRecovery) }
            }
            .catch {
                analytics.logException(it)
            }
            .launchIn(scope)
    }

    private fun observeEvents() {
        monsterLoreDetailEventListener.events
            .onEach { event ->
                when (event) {
                    is MonsterLoreDetailEvent.Show -> {
                        analytics.trackMonsterLoreDetailOpened(event.index)
                        stateRecovery.saveMonsterLoreIndex(event.index)
                        loadMonsterLore(event.index)
                    }
                }
            }
            .launchIn(scope)
    }

    fun onClose() {
        analytics.trackMonsterLoreDetailClosed()
        setState { hideDetail().saveState(stateRecovery) }
    }
}
