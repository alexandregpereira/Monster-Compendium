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

package br.alexandregpereira.hunter.sync

import br.alexandregpereira.hunter.domain.sync.SyncUseCase
import br.alexandregpereira.hunter.domain.sync.model.SyncStatus
import br.alexandregpereira.hunter.state.ScopeManager
import br.alexandregpereira.hunter.state.StateHolder
import br.alexandregpereira.hunter.sync.event.SyncEvent.Finished
import br.alexandregpereira.hunter.sync.event.SyncEvent.Start
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SyncStateHolder internal constructor(
    stateRecovery: SyncStateRecovery,
    private val dispatcher: CoroutineDispatcher,
    private val syncEventManager: SyncEventManager,
    private val syncUseCase: SyncUseCase,
) : ScopeManager(), StateHolder<SyncState> {

    private val _state: MutableStateFlow<SyncState> = MutableStateFlow(
        stateRecovery.getState()
    )
    override val state: StateFlow<SyncState> = _state

    init {
        observeEvents()
        sync(forceSync = false)
    }

    private fun observeEvents() {
        syncEventManager.events
            .onEach { event ->
                when (event) {
                    is Start -> {
                        sync()
                    }
                    is Finished -> {
                        hide()
                    }
                }
            }
            .launchIn(scope)
    }

    private fun sync(forceSync: Boolean = true) {
        syncUseCase(forceSync)
            .flowOn(dispatcher)
            .catch { error ->
                error.printStackTrace()
                setState { copy(hasError = true) }
            }
            .onEach { status ->
                when (status) {
                    SyncStatus.SYNCED -> {
                        syncEventManager.dispatchEvent(Finished)
                        hide()
                    }
                    SyncStatus.IDLE -> {
                        hide()
                    }
                    SyncStatus.BUSY -> {
                        show()
                    }
                }
            }
            .launchIn(scope)
    }

    private fun hide() {
        setState { copy(isOpen = false) }
    }

    private fun show() {
        setState { copy(isOpen = true, hasError = false) }
    }

    fun onTryAgain() {
        syncEventManager.startSync()
    }

    private fun setState(block: SyncState.() -> SyncState) {
        _state.value = state.value.block()
    }
}
