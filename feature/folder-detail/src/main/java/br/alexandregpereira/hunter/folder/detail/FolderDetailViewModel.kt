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

package br.alexandregpereira.hunter.folder.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.folder.GetMonstersByFolderUseCase
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEvent
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailResult.OnVisibilityChanges
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult.OnSaved
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResultListener
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@HiltViewModel
internal class FolderDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMonstersByFolder: GetMonstersByFolderUseCase,
    private val folderDetailEventManager: FolderDetailEventManager,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val folderInsertResultListener: FolderInsertResultListener,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state: MutableStateFlow<FolderDetailViewState> = MutableStateFlow(
        savedStateHandle.getState()
    )
    val state: StateFlow<FolderDetailViewState> = _state

    init {
        observeEvents()
        observeFolderInsertResults()
        if (state.value.isOpen && state.value.monsters.isEmpty()) {
            loadMonsters(state.value.folderName)
        }
    }

    fun onItemClick(index: String) {
        monsterDetailEventDispatcher.dispatchEvent(
            Show(
                index = index,
                indexes = state.value.monsters.map { it.index }
            )
        )
    }

    fun onItemLongClick(index: String) {
        folderPreviewEventDispatcher.dispatchEvent(FolderPreviewEvent.AddMonster(index))
        folderPreviewEventDispatcher.dispatchEvent(FolderPreviewEvent.ShowFolderPreview)
    }

    fun onClose() {
        setState { copy(isOpen = false) }
        folderDetailEventManager.dispatchResult(OnVisibilityChanges(isShowing = false))
    }

    private fun loadMonsters(folderName: String) {
        getMonstersByFolder(folderName)
            .map { it.asState() }
            .flowOn(dispatcher)
            .onEach { monsters ->
                setState {
                    copy(monsters = monsters, folderName = folderName, isOpen = true)
                }
                folderDetailEventManager.dispatchResult(OnVisibilityChanges(isShowing = true))
            }
            .launchIn(viewModelScope)
    }

    private fun observeEvents() {
        folderDetailEventManager.events.onEach { event ->
            when (event) {
                is FolderDetailEvent.Show -> loadMonsters(event.folderName)
            }
        }.launchIn(viewModelScope)
    }

    private fun observeFolderInsertResults() {
        folderInsertResultListener.result
            .filter { result ->
                result is OnSaved && state.value.isOpen
            }
            .onEach {
                loadMonsters(state.value.folderName)
            }
            .launchIn(viewModelScope)
    }

    private fun setState(block: FolderDetailViewState.() -> FolderDetailViewState) {
        _state.value = state.value.block().saveState(savedStateHandle)
    }
}
