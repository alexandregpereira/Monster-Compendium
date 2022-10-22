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
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewConsumerEvent.OnFolderPreviewPreviewVisibilityChanges
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewConsumerEventListener
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.AddMonster
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.ShowFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterPreviewsBySectionUseCase
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumEvents
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumViewState
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterRowState
import br.alexandregpereira.hunter.monster.compendium.ui.SectionState
import br.alexandregpereira.hunter.monster.compendium.ui.alphabetIndex
import br.alexandregpereira.hunter.monster.compendium.ui.alphabetOpened
import br.alexandregpereira.hunter.monster.compendium.ui.complete
import br.alexandregpereira.hunter.monster.compendium.ui.getState
import br.alexandregpereira.hunter.monster.compendium.ui.loading
import br.alexandregpereira.hunter.monster.compendium.ui.saveState
import br.alexandregpereira.hunter.monster.compendium.ui.showMonsterFolderPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
    private val folderPreviewConsumerEventListener: FolderPreviewConsumerEventListener,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    @LoadOnInitFlag loadOnInit: Boolean = true,
) : ViewModel(), MonsterCompendiumEvents {

    private val _state = MutableStateFlow(savedStateHandle.getState())
    val state: StateFlow<MonsterCompendiumViewState> = _state

    init {
        observeEvents()
        if (loadOnInit) loadMonsters()
    }

    fun loadMonsters() = viewModelScope.launch {
        getMonsterPreviewsBySectionUseCase()
            .zip(
                getLastCompendiumScrollItemPositionUseCase()
            ) { monstersBySection, scrollItemPosition ->
                val monstersBySectionState = monstersBySection.asState()
                val alphabet = monstersBySectionState.getAlphabet()
                state.value.complete(
                    monstersBySection = monstersBySectionState,
                    alphabet = alphabet,
                    alphabetIndex = getAlphabetIndex(
                        scrollItemPosition,
                        alphabet,
                        monstersBySectionState
                    ),
                    initialScrollItemPosition = scrollItemPosition
                )
            }
            .onStart {
                emit(state.value.loading(isLoading = true))
            }
            .flowOn(dispatcher)
            .catch {
                Log.e("MonsterViewModel", it.message ?: "")
                it.printStackTrace()
                emit(state.value.loading(isLoading = false))
            }
            .collect { state ->
                _state.value = state
            }
    }

    override fun onItemCLick(index: String) {
        monsterDetailEventDispatcher.dispatchEvent(Show(index))
    }

    override fun onItemLongCLick(index: String) {
        folderPreviewEventDispatcher.dispatchEvent(AddMonster(index))
        folderPreviewEventDispatcher.dispatchEvent(ShowFolderPreview())
    }

    override fun onFirstVisibleItemChange(position: Int) {
        saveCompendiumScrollItemPosition(position)
    }

    override fun onAlphabetOpened() = changeAlphabetOpenState(opened = true)

    override fun onAlphabetClosed() = changeAlphabetOpenState(opened = false)

    override fun onAlphabetIndexClicked(position: Int) {
        navigateToCompendiumIndex(position)
    }

    private fun navigateToCompendiumIndex(alphabetIndex: Int) {
        _state.value = state.value.alphabetOpened(alphabetOpened = false)
        viewModelScope.launch {
            flowOf(state.value.monstersBySection)
                .map { monstersBySection ->
                    val alphabet = monstersBySection.getAlphabet()
                    monstersBySection.mapToFirstLetters().indexOf(alphabet[alphabetIndex])
                }
                .flowOn(dispatcher)
                .collect { compendiumIndex ->
                    _state.value = state.value.copy(compendiumIndex = compendiumIndex)
                }
        }
    }

    private fun saveCompendiumScrollItemPosition(position: Int) {
        val monstersBySection = state.value.monstersBySection
        val alphabet = state.value.alphabet
        viewModelScope.launch {
            saveCompendiumScrollItemPositionUseCase(position)
                .map {
                    getAlphabetIndex(position, alphabet, monstersBySection)
                }
                .flowOn(dispatcher)
                .collect { alphabetIndex ->
                    _state.value = state.value.alphabetIndex(alphabetIndex)
                        .copy(initialScrollItemPosition = position)
                        .saveState(savedStateHandle)
                }
        }
    }

    private fun changeAlphabetOpenState(opened: Boolean) {
        _state.value = state.value.alphabetOpened(alphabetOpened = opened)
    }

    private fun Map<SectionState, List<MonsterRowState>>.mapToFirstLetters(): List<Char> {
        val monsterAlphabet = mutableListOf<Char>()
        forEach { entry ->
            monsterAlphabet.add(entry.key.title.first())
            monsterAlphabet.addAll(
                entry.value.map { entry.key.title.first() }
            )
        }
        return monsterAlphabet
    }

    private fun Map<SectionState, List<MonsterRowState>>.getAlphabet(): List<Char> {
        return mapToFirstLetters().toSortedSet().toList()
    }

    private fun getAlphabetIndex(
        scrollItemPosition: Int,
        alphabet: List<Char>,
        monstersBySection: Map<SectionState, List<MonsterRowState>>
    ): Int {
        if (monstersBySection.isEmpty()) return 0
        val monsterFirstLetters = monstersBySection.mapToFirstLetters()
        return alphabet.indexOf(monsterFirstLetters[scrollItemPosition])
    }

    private fun observeEvents() {
        viewModelScope.launch {
            folderPreviewConsumerEventListener.events.collect { event ->
                when (event) {
                    is OnFolderPreviewPreviewVisibilityChanges -> showMonsterFolderPreview(event.isShowing)
                }
            }
        }
    }

    private fun showMonsterFolderPreview(isShowing: Boolean) {
        _state.value = state.value.showMonsterFolderPreview(isShowing, savedStateHandle)
    }
}
