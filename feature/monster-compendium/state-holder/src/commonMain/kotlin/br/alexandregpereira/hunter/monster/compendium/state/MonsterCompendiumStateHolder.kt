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

package br.alexandregpereira.hunter.monster.compendium.state

import br.alexandregpereira.hunter.domain.sync.IsFirstTime
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterCompendiumUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.getAlphabetIndexFromCompendiumItemIndex
import br.alexandregpereira.hunter.monster.compendium.domain.getCompendiumIndexFromTableContentIndex
import br.alexandregpereira.hunter.monster.compendium.domain.getTableContentIndexFromAlphabetIndex
import br.alexandregpereira.hunter.monster.compendium.domain.getTableContentIndexFromCompendiumItemIndex
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAction.GoToCompendiumIndex
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumException.NavigateToCompendiumIndexError
import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.monster.event.collectOnMonsterCompendiumChanges
import br.alexandregpereira.hunter.monster.event.collectOnMonsterPageChanges
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlin.native.ObjCName

@ObjCName(name = "MonsterCompendiumStateHolder", exact = true)
class MonsterCompendiumStateHolder internal constructor(
    private val getMonsterCompendiumUseCase: GetMonsterCompendiumUseCase,
    private val getLastCompendiumScrollItemPositionUseCase: GetLastCompendiumScrollItemPositionUseCase,
    private val saveCompendiumScrollItemPositionUseCase: SaveCompendiumScrollItemPositionUseCase,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val monsterEventDispatcher: MonsterEventDispatcher,
    private val syncEventDispatcher: SyncEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: MonsterCompendiumAnalytics,
    private val isFirstTime: IsFirstTime,
    appLocalization: AppLocalization,
) : UiModel<MonsterCompendiumState>(
    initialState = MonsterCompendiumState(strings = appLocalization.getStrings()),
), MutableActionHandler<MonsterCompendiumAction> by MutableActionHandler(),
    MonsterCompendiumIntent {

    var initialScrollItemPosition: Int = 0
        private set
    private var metadata: List<MonsterCompendiumItem> = emptyList()

    init {
        observeEvents()
        loadMonsters()
    }

    private fun loadMonsters() = scope.launch {
        fetchMonsterCompendium()
    }

    private suspend fun fetchMonsterCompendium() {
        if (isFirstTime()) {
            setState { loading(isLoading = true) }
            return
        }

        getMonsterCompendiumUseCase()
            .zip(
                getLastCompendiumScrollItemPositionUseCase()
            ) { compendium, scrollItemPosition ->
                analytics.trackMonsterCompendium(compendium, scrollItemPosition)
                val items = compendium.items
                metadata = items
                val alphabet = compendium.alphabet
                val tableContent = compendium.tableContent
                state.value.complete(
                    items = items.asState(),
                    alphabet = alphabet,
                    tableContent = tableContent,
                    tableContentIndex = tableContent.getTableContentIndexFromCompendiumItemIndex(
                        scrollItemPosition,
                        items,
                    ),
                    alphabetSelectedIndex = alphabet.getAlphabetIndexFromCompendiumItemIndex(
                        scrollItemPosition,
                        items,
                    ),
                ) to scrollItemPosition
            }
            .onStart {
                emit(state.value.loading(isLoading = true) to initialScrollItemPosition)
            }
            .flowOn(dispatcher)
            .catch { error ->
                error.printStackTrace()
                analytics.logException(error)
                emit(state.value.error(error) to initialScrollItemPosition)
            }
            .collect { (state, scrollItemPosition) ->
                initialScrollItemPosition = scrollItemPosition
                setState { state }
            }
    }

    override fun onItemClick(index: String) {
        analytics.trackItemClick(index)
        monsterEventDispatcher.dispatchEvent(
            Show(index, enableMonsterPageChangesEventDispatch = true)
        )
    }

    override fun onItemLongClick(index: String) {
        analytics.trackItemLongClick(index)
        folderPreviewEventDispatcher.dispatchEvent(FolderPreviewEvent.AddMonster(index))
    }

    override fun onFirstVisibleItemChange(position: Int) {
        saveCompendiumScrollItemPosition(position)
    }

    override fun onPopupOpened() {
        analytics.trackPopupOpened()
        setState {
            popupOpened(popupOpened = true)
                .tableContentOpened(false)
        }
    }

    override fun onPopupClosed() {
        analytics.trackPopupClosed()
        setState { popupOpened(popupOpened = false) }
    }

    override fun onAlphabetIndexClicked(position: Int) {
        analytics.trackAlphabetIndexClicked(position)
        navigateToTableContentFromAlphabetIndex(position)
    }

    override fun onTableContentIndexClicked(position: Int) {
        analytics.trackTableContentIndexClicked(position)
        navigateToCompendiumIndexFromTableContentIndex(position)
    }

    override fun onTableContentClosed() {
        analytics.trackTableContentClosed()
        setState { tableContentOpened(false) }
    }

    override fun onErrorButtonClick() {
        analytics.trackErrorButtonClick()
        syncEventDispatcher.startSync()
    }

    private fun saveCompendiumScrollItemPosition(position: Int) {
        val alphabet = state.value.alphabet
        val tableContent = state.value.tableContent
        scope.launch {
            saveCompendiumScrollItemPositionUseCase(position)
                .map {
                    tableContent.getTableContentIndexFromCompendiumItemIndex(position, metadata) to
                            alphabet.getAlphabetIndexFromCompendiumItemIndex(position, metadata)
                }
                .flowOn(dispatcher)
                .collect { (tableContentIndex, alphabetLetter) ->
                    initialScrollItemPosition = position
                    setState { tableContentIndex(tableContentIndex, alphabetLetter) }
                }
        }
    }

    private fun navigateToCompendiumIndexFromMonsterIndex(
        monsterIndex: String,
        shouldAnimate: Boolean,
    ) {
        flowOf(metadata)
            .map { items ->
                items.compendiumIndexOf(monsterIndex)
            }.onEach { compendiumIndex ->
                if (compendiumIndex < 0) throw NavigateToCompendiumIndexError(monsterIndex)
            }
            .flowOn(dispatcher)
            .onEach { compendiumIndex ->
                sendAction(GoToCompendiumIndex(compendiumIndex, shouldAnimate))
            }
            .catch {  error ->
                if (error is NavigateToCompendiumIndexError) {
                    fetchMonsterCompendium()
                    metadata.compendiumIndexOf(monsterIndex).takeIf { it >= 0 }?.let {
                        sendAction(GoToCompendiumIndex(it, shouldAnimate))
                    }
                } else {
                    analytics.logException(error)
                }
            }
            .launchIn(scope)
    }

    private fun List<MonsterCompendiumItem>.compendiumIndexOf(monsterIndex: String): Int {
        return indexOfFirst {
            it is MonsterCompendiumItem.Item && it.monster.index == monsterIndex
        }
    }

    private fun observeEvents() {
        monsterEventDispatcher.collectOnMonsterPageChanges { event ->
            navigateToCompendiumIndexFromMonsterIndex(event.monsterIndex, shouldAnimate = true)
        }.launchIn(scope)

        monsterEventDispatcher.collectOnMonsterCompendiumChanges {
            scope.launch {
                fetchMonsterCompendium()
                it.monsterIndex?.let { monsterIndex ->
                    navigateToCompendiumIndexFromMonsterIndex(monsterIndex, shouldAnimate = false)
                }
            }
        }.launchIn(scope)
    }

    private fun navigateToTableContentFromAlphabetIndex(alphabetIndex: Int) {
        val alphabet = state.value.alphabet
        val currentAlphabetIndex = state.value.alphabetSelectedIndex
        val currentTableContentIndex = state.value.tableContentIndex
        flowOf(state.value.tableContent)
            .getTableContentIndexFromAlphabetIndex(
                alphabet = alphabet,
                alphabetIndex = alphabetIndex,
                currentAlphabetIndex = currentAlphabetIndex,
                currentTableContentIndex = currentTableContentIndex
            )
            .flowOn(dispatcher)
            .onEach { tableContentIndex ->
                setState {
                    tableContentOpened(tableContentOpened = true)
                        .copy(tableContentInitialIndex = tableContentIndex)
                }
            }
            .launchIn(scope)
    }

    private fun navigateToCompendiumIndexFromTableContentIndex(tableContentIndex: Int) {
        setState { popupOpened(popupOpened = false) }
        val tableContent = state.value.tableContent
        scope.launch {
            flowOf(metadata)
                .getCompendiumIndexFromTableContentIndex(tableContent, tableContentIndex)
                .flowOn(dispatcher)
                .collect { compendiumIndex ->
                    sendAction(GoToCompendiumIndex(compendiumIndex, shouldAnimate = false))
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        onActionHandlerClose()
    }
}
