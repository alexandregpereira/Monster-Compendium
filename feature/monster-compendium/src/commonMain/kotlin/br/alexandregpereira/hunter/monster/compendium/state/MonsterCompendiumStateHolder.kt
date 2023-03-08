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
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.event.monster.detail.collectOnMonsterPageChanges
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResult
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResultListener
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterCompendiumUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Title
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAction.GoToCompendiumIndex
import br.alexandregpereira.hunter.state.ActionListener
import br.alexandregpereira.hunter.state.DefaultActionDispatcher
import br.alexandregpereira.hunter.state.DefaultStateHolder
import br.alexandregpereira.hunter.state.ScopeManager
import br.alexandregpereira.hunter.state.StateHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val dispatcher: CoroutineDispatcher,
    initialState: MonsterCompendiumState = MonsterCompendiumState(),
    loadOnInit: Boolean = true,
) : ScopeManager(), StateHolder<MonsterCompendiumState>, ActionListener<MonsterCompendiumAction> {

    var initialScrollItemPosition: Int = 0
        private set

    private val stateHolder = DefaultStateHolder(initialState)
    private val actionDispatcher = DefaultActionDispatcher<MonsterCompendiumAction>()

    override val state: StateFlow<MonsterCompendiumState> = stateHolder.state
    override val action: SharedFlow<MonsterCompendiumAction> = actionDispatcher.action

    init {
        observeEvents()
        if (loadOnInit) loadMonsters()
    }

    fun loadMonsters() = scope.launch {
        getMonsterCompendiumUseCase()
            .zip(
                getLastCompendiumScrollItemPositionUseCase()
            ) { compendium, scrollItemPosition ->
                val items = compendium.items
                val alphabet = compendium.alphabet
                val tableContent = compendium.tableContent
                state.value.complete(
                    items = items,
                    alphabet = alphabet,
                    tableContent = tableContent,
                    tableContentIndex = getTableContentIndexFromCompendiumItemIndex(
                        scrollItemPosition,
                        items,
                        tableContent
                    ),
                    alphabetSelectedIndex = getAlphabetIndexFromCompendiumItemIndex(
                        scrollItemPosition,
                        items,
                        alphabet
                    )
                ) to scrollItemPosition
            }
            .onStart {
                emit(state.value.loading(isLoading = true) to initialScrollItemPosition)
            }
            .flowOn(dispatcher)
            .catch { error ->
                error.printStackTrace()
                emit(state.value.error() to initialScrollItemPosition)
            }
            .collect { (state, scrollItemPosition) ->
                initialScrollItemPosition = scrollItemPosition
                setState { state }
            }
    }

    fun onItemCLick(index: String) {
        monsterDetailEventDispatcher.dispatchEvent(MonsterDetailEvent.OnVisibilityChanges.Show(index))
    }

    fun onItemLongCLick(index: String) {
        folderPreviewEventDispatcher.dispatchEvent(FolderPreviewEvent.AddMonster(index))
        folderPreviewEventDispatcher.dispatchEvent(FolderPreviewEvent.ShowFolderPreview)
    }

    fun onFirstVisibleItemChange(position: Int) {
        saveCompendiumScrollItemPosition(position)
    }

    fun onPopupOpened() {
        setState {
            popupOpened(popupOpened = true)
                .tableContentOpened(false)
        }
    }

    fun onPopupClosed() {
        setState { popupOpened(popupOpened = false) }
    }

    fun onAlphabetIndexClicked(position: Int) {
        navigateToTableContentFromAlphabetIndex(position)
    }

    fun onTableContentIndexClicked(position: Int) {
        navigateToCompendiumIndexFromTableContentIndex(position)
    }

    fun onTableContentClosed() {
        setState { tableContentOpened(false) }
    }

    fun onErrorButtonClick() {
        loadMonsters()
    }

    private fun saveCompendiumScrollItemPosition(position: Int) {
        val items = state.value.items
        val alphabet = state.value.alphabet
        val tableContent = state.value.tableContent
        scope.launch {
            saveCompendiumScrollItemPositionUseCase(position)
                .map {
                    getTableContentIndexFromCompendiumItemIndex(position, items, tableContent) to
                            getAlphabetIndexFromCompendiumItemIndex(position, items, alphabet)
                }
                .flowOn(dispatcher)
                .collect { (tableContentIndex, alphabetLetter) ->
                    initialScrollItemPosition = position
                    setState { tableContentIndex(tableContentIndex, alphabetLetter) }
                }
        }
    }

    private fun List<MonsterCompendiumItem>.mapToFirstLetters(): List<String> {
        var lastLetter: Char? = null
        return map { item ->
            when (item) {
                is Title -> {
                    item.value.first().also { lastLetter = it }.toString()
                }
                is MonsterCompendiumItem.Item -> lastLetter?.toString()
                    ?: throw IllegalArgumentException("Letter not initialized")
            }
        }
    }

    private fun getAlphabetIndexFromCompendiumItemIndex(
        itemIndex: Int,
        items: List<MonsterCompendiumItem>,
        alphabet: List<String>
    ): Int {
        if (items.isEmpty()) return -1
        val monsterFirstLetters = items.mapToFirstLetters()
        return alphabet.indexOf(monsterFirstLetters[itemIndex])
    }

    private fun navigateToCompendiumIndexFromMonsterIndex(monsterIndex: String) {
        flowOf(state.value.items)
            .map { items ->
                items.indexOfFirst {
                    it is MonsterCompendiumItem.Item
                            && it.monster.index == monsterIndex
                }
            }
            .flowOn(dispatcher)
            .onEach { compendiumIndex ->
                sendAction(GoToCompendiumIndex(compendiumIndex))
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
    }

    private fun showMonsterFolderPreview(isShowing: Boolean) {
        setState { showMonsterFolderPreview(isShowing) }
    }

    private fun sendAction(action: MonsterCompendiumAction) {
        actionDispatcher.sendAction(action)
    }

    private fun navigateToTableContentFromAlphabetIndex(alphabetIndex: Int) {
        val alphabet = state.value.alphabet
        val currentAlphabetIndex = state.value.alphabetSelectedIndex
        val currentTableContentIndex = state.value.tableContentIndex
        flowOf(state.value.tableContent)
            .map { tableContent ->
                val letter = alphabet[alphabetIndex]
                if (currentAlphabetIndex == alphabetIndex) {
                    currentTableContentIndex
                } else {
                    tableContent.indexOfFirst { it.text == letter }
                }
            }
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
                .map { items ->
                    val content = tableContent[tableContentIndex]
                    items.indexOfFirst { item ->
                        val value = when (item) {
                            is Title -> item.id
                            is MonsterCompendiumItem.Item -> item.monster.index
                        }
                        content.id == value
                    }
                }
                .flowOn(dispatcher)
                .collect { compendiumIndex ->
                    sendAction(GoToCompendiumIndex(compendiumIndex))
                }
        }
    }

    private fun getTableContentIndexFromCompendiumItemIndex(
        itemIndex: Int,
        items: List<MonsterCompendiumItem>,
        tableContent: List<TableContentItem>
    ): Int {
        val compendiumItem = items.getOrNull(itemIndex) ?: return -1
        return tableContent.indexOfFirst { content ->
            val value = when (compendiumItem) {
                is Title -> compendiumItem.id
                is MonsterCompendiumItem.Item -> compendiumItem.monster.index
            }
            content.id == value
        }
    }

    private fun setState(block: MonsterCompendiumState.() -> MonsterCompendiumState) {
        stateHolder.setState(block)
    }
}
