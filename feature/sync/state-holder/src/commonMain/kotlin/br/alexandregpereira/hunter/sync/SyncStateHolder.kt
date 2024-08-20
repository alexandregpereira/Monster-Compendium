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
