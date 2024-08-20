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

package br.alexandregpereira.hunter.monster.content

import br.alexandregpereira.hunter.domain.source.AddAlternativeSourceUseCase
import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesUseCase
import br.alexandregpereira.hunter.domain.source.RemoveAlternativeSourceUseCase
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEvent
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventDispatcher
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventListener
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewEventManager
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MonsterContentManagerStateHolder internal constructor(
    private val stateRecovery: StateRecovery,
    private val dispatcher: CoroutineDispatcher,
    private val getAlternativeSourcesUseCase: GetAlternativeSourcesUseCase,
    private val addAlternativeSourceUseCase: AddAlternativeSourceUseCase,
    private val removeAlternativeSourceUseCase: RemoveAlternativeSourceUseCase,
    private val eventDispatcher: MonsterContentManagerEventDispatcher,
    private val eventListener: MonsterContentManagerEventListener,
    private val syncEventDispatcher: SyncEventDispatcher,
    private val analytics: MonsterContentManagerAnalytics,
    private val monsterContentPreviewEventManager: MonsterContentPreviewEventManager,
    private val appLocalization: AppLocalization,
) : UiModel<MonsterContentManagerState>(stateRecovery.getState()) {

    private var hasChanges: Boolean = false

    init {
        observeEvents()
        if (state.value.isOpen && state.value.monsterContents.isEmpty()) {
            load()
        }
    }

    private fun load() {
        setState { copy(isLoading = true) }
        getAlternativeSourcesUseCase(onlyContentEnabled = false)
            .flowOn(dispatcher)
            .onEach { alternativeSources ->
                analytics.trackMonsterContentLoaded(alternativeSources)
                setState {
                    copy(
                        monsterContents = alternativeSources.map { it.asState() },
                        strings = appLocalization.getStrings(),
                        isLoading = false,
                    )
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
                        setState { copy(isOpen = true).saveState(stateRecovery) }
                        load()
                    }
                    is MonsterContentManagerEvent.Hide -> {
                        setState { hide().saveState(stateRecovery) }
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

    fun onPreviewClick(acronym: String, name: String) {
        analytics.trackPreviewContentClick(acronym, name)
        monsterContentPreviewEventManager.show(acronym, title = name)
    }

    fun onClose() {
        analytics.trackClose()
        if (hasChanges) {
            hasChanges = false
            syncEventDispatcher.startSync()
        }
        eventDispatcher.dispatchEvent(MonsterContentManagerEvent.Hide)
    }
}
