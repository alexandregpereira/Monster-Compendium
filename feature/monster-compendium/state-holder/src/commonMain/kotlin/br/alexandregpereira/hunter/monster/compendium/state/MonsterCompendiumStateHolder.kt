/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.monster.compendium.state

import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.event.monster.detail.collectOnMonsterCompendiumChanges
import br.alexandregpereira.hunter.event.monster.detail.collectOnMonsterPageChanges
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResult
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResultListener
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterCompendiumUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.getAlphabetIndexFromCompendiumItemIndex
import br.alexandregpereira.hunter.monster.compendium.domain.getCompendiumIndexFromTableContentIndex
import br.alexandregpereira.hunter.monster.compendium.domain.getTableContentIndexFromAlphabetIndex
import br.alexandregpereira.hunter.monster.compendium.domain.getTableContentIndexFromCompendiumItemIndex
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAction.GoToCompendiumIndex
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumException.NavigateToCompendiumIndexError
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.MutableStateHolder
import br.alexandregpereira.hunter.state.ScopeManager
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import br.alexandregpereira.hunter.sync.event.SyncEventListener
import br.alexandregpereira.hunter.sync.event.collectSyncFinishedEvents
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

class MonsterCompendiumStateHolder(
    private val getMonsterCompendiumUseCase: GetMonsterCompendiumUseCase,
    private val getLastCompendiumScrollItemPositionUseCase: GetLastCompendiumScrollItemPositionUseCase,
    private val saveCompendiumScrollItemPositionUseCase: SaveCompendiumScrollItemPositionUseCase,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val folderPreviewResultListener: FolderPreviewResultListener,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    private val monsterDetailEventListener: MonsterDetailEventListener,
    private val syncEventListener: SyncEventListener,
    private val syncEventDispatcher: SyncEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: MonsterCompendiumAnalytics,
    initialState: MonsterCompendiumState = MonsterCompendiumState(),
    loadOnInit: Boolean = true,
) : ScopeManager(),
    MutableStateHolder<MonsterCompendiumState> by MutableStateHolder(initialState),
    MutableActionHandler<MonsterCompendiumAction> by MutableActionHandler() {

    var initialScrollItemPosition: Int = 0
        private set

    init {
        observeEvents()
        if (loadOnInit) loadMonsters()
    }

    fun loadMonsters() = scope.launch {
        getMonsterCompendiumUseCase()
            .zip(
                getLastCompendiumScrollItemPositionUseCase()
            ) { compendium, scrollItemPosition ->
                analytics.trackMonsterCompendium(compendium, scrollItemPosition)
                val items = compendium.items
                val alphabet = compendium.alphabet
                val tableContent = compendium.tableContent
                state.value.complete(
                    items = items,
                    alphabet = alphabet,
                    tableContent = tableContent,
                    tableContentIndex = tableContent.getTableContentIndexFromCompendiumItemIndex(
                        scrollItemPosition,
                        items,
                    ),
                    alphabetSelectedIndex = alphabet.getAlphabetIndexFromCompendiumItemIndex(
                        scrollItemPosition,
                        items,
                    )
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

    fun onItemClick(index: String) {
        analytics.trackItemClick(index)
        monsterDetailEventDispatcher.dispatchEvent(
            Show(index, enableMonsterPageChangesEventDispatch = true)
        )
    }

    fun onItemLongCLick(index: String) {
        analytics.trackItemLongClick(index)
        folderPreviewEventDispatcher.dispatchEvent(FolderPreviewEvent.AddMonster(index))
        folderPreviewEventDispatcher.dispatchEvent(FolderPreviewEvent.ShowFolderPreview)
    }

    fun onFirstVisibleItemChange(position: Int) {
        saveCompendiumScrollItemPosition(position)
    }

    fun onPopupOpened() {
        analytics.trackPopupOpened()
        setState {
            popupOpened(popupOpened = true)
                .tableContentOpened(false)
        }
    }

    fun onPopupClosed() {
        analytics.trackPopupClosed()
        setState { popupOpened(popupOpened = false) }
    }

    fun onAlphabetIndexClicked(position: Int) {
        analytics.trackAlphabetIndexClicked(position)
        navigateToTableContentFromAlphabetIndex(position)
    }

    fun onTableContentIndexClicked(position: Int) {
        analytics.trackTableContentIndexClicked(position)
        navigateToCompendiumIndexFromTableContentIndex(position)
    }

    fun onTableContentClosed() {
        analytics.trackTableContentClosed()
        setState { tableContentOpened(false) }
    }

    fun onErrorButtonClick() {
        analytics.trackErrorButtonClick()
        syncEventDispatcher.startSync()
    }

    private fun saveCompendiumScrollItemPosition(position: Int) {
        val items = state.value.items
        val alphabet = state.value.alphabet
        val tableContent = state.value.tableContent
        scope.launch {
            saveCompendiumScrollItemPositionUseCase(position)
                .map {
                    tableContent.getTableContentIndexFromCompendiumItemIndex(position, items) to
                            alphabet.getAlphabetIndexFromCompendiumItemIndex(position, items)
                }
                .flowOn(dispatcher)
                .collect { (tableContentIndex, alphabetLetter) ->
                    initialScrollItemPosition = position
                    setState { tableContentIndex(tableContentIndex, alphabetLetter) }
                }
        }
    }

    private fun navigateToCompendiumIndexFromMonsterIndex(monsterIndex: String) {
        flowOf(state.value.items)
            .map { items ->
                items.indexOfFirst {
                    it is MonsterCompendiumItem.Item
                            && it.monster.index == monsterIndex
                }
            }.onEach { compendiumIndex ->
                if (compendiumIndex < 0) throw NavigateToCompendiumIndexError(monsterIndex)
            }
            .flowOn(dispatcher)
            .onEach { compendiumIndex ->
                sendAction(GoToCompendiumIndex(compendiumIndex))
            }
            .catch {  error ->
                if (error is NavigateToCompendiumIndexError) {
                    loadMonsters()
                } else {
                    analytics.logException(error)
                }
            }
            .launchIn(scope)
    }

    private fun observeEvents() {
        scope.launch {
            folderPreviewResultListener.result.collect { event ->
                when (event) {
                    is FolderPreviewResult.OnFolderPreviewPreviewVisibilityChanges -> showMonsterFolderPreview(
                        event.isShowing
                    )
                }
            }
        }

        monsterDetailEventListener.collectOnMonsterPageChanges { event ->
            navigateToCompendiumIndexFromMonsterIndex(event.monsterIndex)
        }.launchIn(scope)

        monsterDetailEventListener.collectOnMonsterCompendiumChanges {
            loadMonsters()
        }.launchIn(scope)

        syncEventListener.collectSyncFinishedEvents {
            loadMonsters()
        }.launchIn(scope)
    }

    private fun showMonsterFolderPreview(isShowing: Boolean) {
        setState { showMonsterFolderPreview(isShowing) }
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
            flowOf(state.value.items)
                .getCompendiumIndexFromTableContentIndex(tableContent, tableContentIndex)
                .flowOn(dispatcher)
                .collect { compendiumIndex ->
                    sendAction(GoToCompendiumIndex(compendiumIndex))
                }
        }
    }
}
