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
