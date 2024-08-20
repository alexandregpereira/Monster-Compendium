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

package br.alexandregpereira.hunter.monster.content.preview

import br.alexandregpereira.hunter.monster.compendium.domain.GetRemoteMonsterCompendiumUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.getAlphabetIndexFromCompendiumItemIndex
import br.alexandregpereira.hunter.monster.compendium.domain.getCompendiumIndexFromTableContentIndex
import br.alexandregpereira.hunter.monster.compendium.domain.getTableContentIndexFromCompendiumItemIndex
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewAction.Companion.goToCompendiumIndex
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MonsterContentPreviewStateHolder internal constructor(
    private val stateRecovery: StateRecovery,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: MonsterContentPreviewAnalytics,
    private val getRemoteMonsterCompendiumUseCase: GetRemoteMonsterCompendiumUseCase,
    private val monsterContentPreviewEventManager: MonsterContentPreviewEventManager,
) : UiModel<MonsterContentPreviewState>(stateRecovery.getState()),
    MutableActionHandler<MonsterContentPreviewAction> by MutableActionHandler() {

    init {
        observeEvents()
        if (state.value.isOpen && state.value.monsterCompendiumItems.isEmpty()) {
            load()
        }
    }

    private fun observeEvents() {
        monsterContentPreviewEventManager.events
            .onEach { event ->
                when (event) {
                    is MonsterContentPreviewEvent.Show -> {
                        stateRecovery.sourceAcronym = event.sourceAcronym
                        setState {
                            copy(isOpen = true, title = event.title).saveState(stateRecovery)
                        }
                        load()
                    }
                }
            }
            .launchIn(scope)
    }

    private fun load() {
        setState { copy(isLoading = true) }
        getRemoteMonsterCompendiumUseCase(stateRecovery.sourceAcronym)
            .map { monsterCompendium ->
                val tableContent = monsterCompendium.tableContent
                val alphabet = monsterCompendium.alphabet
                state.value.copy(
                    monsterCompendiumItems = monsterCompendium.items,
                    alphabet = monsterCompendium.alphabet,
                    tableContent = monsterCompendium.tableContent,
                    tableContentSelectedIndex = tableContent.getTableContentIndexFromCompendiumItemIndex(
                        itemIndex = 0,
                        items = monsterCompendium.items,
                    ),
                    alphabetSelectedIndex = alphabet.getAlphabetIndexFromCompendiumItemIndex(
                        itemIndex = 0,
                        items = monsterCompendium.items,
                    ),
                    isOpen = true,
                    isLoading = false
                )
            }
            .flowOn(dispatcher)
            .catch { error ->
                analytics.logException(error, stateRecovery.sourceAcronym)
                setState { copy(isLoading = false) }
            }
            .onEach { state ->
                analytics.trackLoaded(stateRecovery.sourceAcronym, state.monsterCompendiumItems)
                setState { state }
            }
            .launchIn(scope)
    }

    fun onClose() {
        analytics.trackClose(stateRecovery.sourceAcronym)
        setState { hide().saveState(stateRecovery) }
    }

    fun onTableContentOpenButtonClick() {
        analytics.trackTableContentOpened()
        setState {
            copy(tableContentOpened = true)
        }
    }

    fun onTableContentClose() {
        analytics.trackTableContentClosed()
        setState {
            copy(tableContentOpened = false)
        }
    }

    fun onTableContentClick(tableContentIndex: Int) {
        val tableContent = state.value.tableContent
        analytics.trackTableContentIndexClicked(tableContentIndex, tableContent)
        setState { copy(tableContentOpened = false) }
        scope.launch {
            flowOf(state.value.monsterCompendiumItems)
                .getCompendiumIndexFromTableContentIndex(tableContent, tableContentIndex)
                .flowOn(dispatcher)
                .collect { compendiumIndex ->
                    sendAction(goToCompendiumIndex(compendiumIndex))
                }
        }
    }

    fun onFirstVisibleItemChange(compendiumItemIndex: Int) {
        val items = state.value.monsterCompendiumItems
        val alphabet = state.value.alphabet
        val tableContent = state.value.tableContent
        flowOf(compendiumItemIndex)
            .map {
                tableContent.getTableContentIndexFromCompendiumItemIndex(compendiumItemIndex, items) to
                        alphabet.getAlphabetIndexFromCompendiumItemIndex(compendiumItemIndex, items)
            }
            .flowOn(dispatcher)
            .onEach { (tableContentIndex, alphabetIndex) ->
                setState {
                    copy(
                        tableContentSelectedIndex = tableContentIndex,
                        alphabetSelectedIndex = alphabetIndex
                    )
                }
            }
            .launchIn(scope)
    }

    override fun onCleared() {
        super.onCleared()
        onActionHandlerClose()
    }
}
