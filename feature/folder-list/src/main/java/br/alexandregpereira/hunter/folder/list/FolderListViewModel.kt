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

package br.alexandregpereira.hunter.folder.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.folder.GetMonsterFoldersUseCase
import br.alexandregpereira.hunter.domain.folder.RemoveMonsterFoldersUseCase
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEvent.Show
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEventDispatcher
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult.OnSaved
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResultListener
import br.alexandregpereira.hunter.event.folder.list.FolderListResult.OnItemSelectionVisibilityChanges
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class FolderListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getMonsterFolders: GetMonsterFoldersUseCase,
    private val removeMonsterFolders: RemoveMonsterFoldersUseCase,
    private val folderInsertResultListener: FolderInsertResultListener,
    private val folderDetailEventDispatcher: FolderDetailEventDispatcher,
    private val folderListEventManager: FolderListEventManager,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state: MutableStateFlow<FolderListViewState> = MutableStateFlow(
        savedStateHandle.getState()
    )
    val state: StateFlow<FolderListViewState> = _state

    init {
        observeFolderInsertResults()
        loadMonsterFolders()
    }

    fun onItemClick(folderName: String) {
        if (state.value.itemSelectionEnabled) {
            onItemSelect(folderName)
            return
        }
        folderDetailEventDispatcher.dispatchEvent(Show(folderName = folderName))
    }

    fun onItemSelectionClose() {
        setState {
            copy(
                isItemSelectionOpen = false
            ).changeSelectedFolders(folders)
        }
        folderListEventManager.dispatchResult(OnItemSelectionVisibilityChanges(isShowing = false))
    }

    fun onItemSelectionDeleteClick() {
        onItemSelectionClose()
        flowOf(state.value.itemSelection)
            .map { it.toList() }
            .map { folderNames ->
                removeMonsterFolders(folderNames).collect()
            }
            .flowOn(dispatcher)
            .onEach { loadMonsterFolders() }
            .launchIn(viewModelScope)
    }

    fun onItemSelect(folderName: String) {
        setState {
            val (newItemSelection, added) = itemSelection.toMutableSet().let {
                if (isItemSelectionOpen.not()) it.clear()

                val added = it.add(folderName)
                if (added.not() && it.size > 1) {
                    it.remove(folderName)
                }
                it to added
            }
            val isItemSelectionOpen = added || newItemSelection != setOf(folderName)
            copy(
                itemSelection = newItemSelection,
                isItemSelectionOpen = isItemSelectionOpen,
            ).changeSelectedFolders(folders)
        }
        folderListEventManager.dispatchResult(
            OnItemSelectionVisibilityChanges(isShowing = state.value.isItemSelectionOpen)
        )
    }

    private fun loadMonsterFolders() {
        getMonsterFolders()
            .map { it.asState() }
            .flowOn(dispatcher)
            .onEach { folders ->
                setState { changeSelectedFolders(folders) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeFolderInsertResults() {
        folderInsertResultListener.result
            .filter { result ->
                result is OnSaved
            }
            .onEach {
                loadMonsterFolders()
            }
            .launchIn(viewModelScope)
    }

    private fun setState(block: FolderListViewState.() -> FolderListViewState) {
        _state.value = state.value.block().saveState(savedStateHandle)
    }
}
