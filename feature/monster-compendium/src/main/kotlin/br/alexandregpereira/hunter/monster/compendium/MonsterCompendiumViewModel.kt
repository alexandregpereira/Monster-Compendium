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

package br.alexandregpereira.hunter.monster.compendium

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.event.monster.detail.collectOnMonsterPageChanges
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.AddMonster
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.ShowFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResult.OnFolderPreviewPreviewVisibilityChanges
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResultListener
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumViewAction.GoToCompendiumIndex
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterPreviewsBySectionUseCase
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumEvents
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState.Title
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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

@HiltViewModel
class MonsterCompendiumViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMonsterPreviewsBySectionUseCase: GetMonsterPreviewsBySectionUseCase,
    private val getLastCompendiumScrollItemPositionUseCase: GetLastCompendiumScrollItemPositionUseCase,
    private val saveCompendiumScrollItemPositionUseCase: SaveCompendiumScrollItemPositionUseCase,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val folderPreviewResultListener: FolderPreviewResultListener,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    private val monsterDetailEventListener: MonsterDetailEventListener,
    private val dispatcher: CoroutineDispatcher,
    @LoadOnInitFlag loadOnInit: Boolean = true,
) : ViewModel(), MonsterCompendiumEvents {

    var initialScrollItemPosition: Int = 0
        private set

    private val _state = MutableStateFlow(savedStateHandle.getState())
    val state: StateFlow<MonsterCompendiumViewState> = _state

    private val _action = MutableSharedFlow<MonsterCompendiumViewAction>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val action: SharedFlow<MonsterCompendiumViewAction> = _action

    init {
        observeEvents()
        if (loadOnInit) loadMonsters()
    }

    fun loadMonsters() = viewModelScope.launch {
        getMonsterPreviewsBySectionUseCase()
            .zip(
                getLastCompendiumScrollItemPositionUseCase()
            ) { items, scrollItemPosition ->
                val itemsState = items.asState()
                val alphabet = itemsState.getAlphabet()
                state.value.complete(
                    items = itemsState,
                    alphabet = alphabet,
                    alphabetIndex = getAlphabetIndex(
                        scrollItemPosition,
                        alphabet,
                        itemsState
                    ),
                ) to scrollItemPosition
            }
            .onStart {
                emit(state.value.loading(isLoading = true) to initialScrollItemPosition)
            }
            .flowOn(dispatcher)
            .catch { error ->
                Log.e("MonsterViewModel", error.message ?: "")
                error.printStackTrace()
                emit(state.value.error() to initialScrollItemPosition)
            }
            .collect { (state, scrollItemPosition) ->
                initialScrollItemPosition = scrollItemPosition
                _state.value = state
            }
    }

    override fun onItemCLick(index: String) {
        monsterDetailEventDispatcher.dispatchEvent(Show(index))
    }

    override fun onItemLongCLick(index: String) {
        folderPreviewEventDispatcher.dispatchEvent(AddMonster(index))
        folderPreviewEventDispatcher.dispatchEvent(ShowFolderPreview)
    }

    override fun onFirstVisibleItemChange(position: Int) {
        saveCompendiumScrollItemPosition(position)
    }

    override fun onAlphabetOpened() = changeAlphabetOpenState(opened = true)

    override fun onAlphabetClosed() = changeAlphabetOpenState(opened = false)

    override fun onAlphabetIndexClicked(position: Int) {
        navigateToCompendiumIndexFromAlphabetIndex(position)
    }

    override fun onErrorButtonClick() {
        loadMonsters()
    }

    private fun navigateToCompendiumIndexFromAlphabetIndex(alphabetIndex: Int) {
        _state.value = state.value.alphabetOpened(alphabetOpened = false)
        viewModelScope.launch {
            flowOf(state.value.items)
                .map { items ->
                    val letter = items.getAlphabet()[alphabetIndex]
                    items.indexOfFirst { it is Title && it.value == letter.toString() }
                }
                .flowOn(dispatcher)
                .collect { compendiumIndex ->
                    sendAction(GoToCompendiumIndex(compendiumIndex))
                }
        }
    }

    private fun saveCompendiumScrollItemPosition(position: Int) {
        val items = state.value.items
        val alphabet = state.value.alphabet
        viewModelScope.launch {
            saveCompendiumScrollItemPositionUseCase(position)
                .map {
                    getAlphabetIndex(position, alphabet, items)
                }
                .flowOn(dispatcher)
                .collect { alphabetIndex ->
                    initialScrollItemPosition = position
                    _state.value = state.value.alphabetIndex(alphabetIndex)
                        .saveState(savedStateHandle)
                }
        }
    }

    private fun changeAlphabetOpenState(opened: Boolean) {
        _state.value = state.value.alphabetOpened(alphabetOpened = opened)
    }

    private fun List<CompendiumItemState>.mapToFirstLetters(): List<Char> {
        var lastLetter: Char? = null
        return map { item ->
            when (item) {
                is Title -> {
                    item.value.first().also { lastLetter = it }
                }
                is CompendiumItemState.Item -> lastLetter
                    ?: throw IllegalArgumentException("Letter not initialized")
            }
        }
    }

    private fun List<CompendiumItemState>.getAlphabet(): List<Char> {
        return mapToFirstLetters().toSortedSet().toList()
    }

    private fun getAlphabetIndex(
        scrollItemPosition: Int,
        alphabet: List<Char>,
        items: List<CompendiumItemState>
    ): Int {
        if (items.isEmpty()) return 0
        val monsterFirstLetters = items.mapToFirstLetters()
        return alphabet.indexOf(monsterFirstLetters[scrollItemPosition])
    }

    private fun navigateToCompendiumIndexFromMonsterIndex(monsterIndex: String) {
        flowOf(state.value.items)
            .map { items ->
                items.indexOfFirst {
                    it is CompendiumItemState.Item
                            && it.getMonsterCardState().index == monsterIndex
                }
            }
            .flowOn(dispatcher)
            .onEach { compendiumIndex ->
                sendAction(GoToCompendiumIndex(compendiumIndex))
            }
            .launchIn(viewModelScope)
    }

    private fun CompendiumItemState.Item.getMonsterCardState() = this.value as MonsterCardState

    private fun observeEvents() {
        viewModelScope.launch {
            folderPreviewResultListener.result.collect { event ->
                when (event) {
                    is OnFolderPreviewPreviewVisibilityChanges -> showMonsterFolderPreview(event.isShowing)
                }
            }
        }

        monsterDetailEventListener.collectOnMonsterPageChanges { event ->
            navigateToCompendiumIndexFromMonsterIndex(event.monsterIndex)
        }.launchIn(viewModelScope)
    }

    private fun showMonsterFolderPreview(isShowing: Boolean) {
        _state.value = state.value.showMonsterFolderPreview(isShowing, savedStateHandle)
    }

    private fun sendAction(action: MonsterCompendiumViewAction) {
        _action.tryEmit(action)
    }
}
