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
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.sync.event.SyncEvent.Finished
import br.alexandregpereira.hunter.sync.event.SyncEvent.Start
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SyncStateHolder internal constructor(
    private val dispatcher: CoroutineDispatcher,
    private val syncEventManager: SyncEventManager,
    private val syncUseCase: SyncUseCase,
    private val analytics: SyncAnalytics
) : UiModel<SyncState>(SyncState()) {

    init {
        observeEvents()
        sync(forceSync = false)
    }

    private fun observeEvents() {
        syncEventManager.events
            .onEach { event ->
                when (event) {
                    is Start -> {
                        analytics.trackStartSync()
                        sync()
                    }
                    is Finished -> {
                        analytics.trackFinishSync()
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
                analytics.logException(error)
                setState { copy(hasError = true) }
            }
            .onEach { status ->
                when (status) {
                    SyncStatus.SYNCED -> {
                        analytics.trackSyncStatus(status, forceSync)
                        syncEventManager.dispatchEvent(Finished)
                        hide()
                    }
                    SyncStatus.IDLE -> {
                        hide()
                    }
                    SyncStatus.BUSY -> {
                        analytics.trackSyncStatus(status, forceSync)
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
        analytics.trackTryAgain()
        syncEventManager.startSync()
    }
}
