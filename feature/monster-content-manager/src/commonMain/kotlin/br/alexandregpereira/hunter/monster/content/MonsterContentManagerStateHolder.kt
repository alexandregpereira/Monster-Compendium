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

package br.alexandregpereira.hunter.monster.content

import br.alexandregpereira.hunter.domain.source.AddAlternativeSourceUseCase
import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesUseCase
import br.alexandregpereira.hunter.domain.source.RemoveAlternativeSourceUseCase
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEvent
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventDispatcher
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventListener
import br.alexandregpereira.hunter.state.ScopeManager
import br.alexandregpereira.hunter.state.StateHolder
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MonsterContentManagerStateHolder internal constructor(
    stateRecovery: MonsterContentManagerStateRecovery,
    private val dispatcher: CoroutineDispatcher,
    private val getAlternativeSourcesUseCase: GetAlternativeSourcesUseCase,
    private val addAlternativeSourceUseCase: AddAlternativeSourceUseCase,
    private val removeAlternativeSourceUseCase: RemoveAlternativeSourceUseCase,
    private val eventDispatcher: MonsterContentManagerEventDispatcher,
    private val eventListener: MonsterContentManagerEventListener,
    private val syncEventDispatcher: SyncEventDispatcher,
    private val analytics: MonsterContentManagerAnalytics,
) : ScopeManager(), StateHolder<MonsterContentManagerState> {

    private val _state = MutableStateFlow(stateRecovery.getState())
    override val state: StateFlow<MonsterContentManagerState> = _state
    private var hasChanges: Boolean = false

    init {
        observeEvents()
        if (state.value.isOpen && state.value.monsterContents.isEmpty()) {
            load()
        }
    }

    private fun load() {
        getAlternativeSourcesUseCase(onlyContentEnabled = false)
            .flowOn(dispatcher)
            .onEach { alternativeSources ->
                analytics.trackMonsterContentLoaded(alternativeSources)
                setState {
                    copy(monsterContents = alternativeSources)
                }
            }
            .catch {
                analytics.logException(it)
            }
            .launchIn(scope)
    }

    private fun observeEvents() {
        eventListener.events
            .onEach { event ->
                when (event) {
                    is MonsterContentManagerEvent.Show -> {
                        setState { copy(isOpen = true) }
                        load()
                    }
                    is MonsterContentManagerEvent.Hide -> {
                        setState { hide() }
                    }
                }
            }
            .launchIn(scope)
    }

    fun onAddContentClick(acronym: String) {
        analytics.trackAddContentClick(acronym)
        hasChanges = true
        addAlternativeSourceUseCase(acronym)
            .flowOn(dispatcher)
            .onEach {
                load()
            }
            .catch {}
            .launchIn(scope)
    }

    fun onRemoveContentClick(acronym: String) {
        analytics.trackRemoveContentClick(acronym)
        hasChanges = true
        removeAlternativeSourceUseCase(acronym)
            .flowOn(dispatcher)
            .onEach {
                load()
            }
            .catch {}
            .launchIn(scope)
    }

    fun onClose() {
        analytics.trackClose()
        if (hasChanges) {
            hasChanges = false
            syncEventDispatcher.startSync()
        }
        eventDispatcher.dispatchEvent(MonsterContentManagerEvent.Hide)
    }

    private fun setState(block: MonsterContentManagerState.() -> MonsterContentManagerState) {
        _state.value = state.value.block()
    }
}
