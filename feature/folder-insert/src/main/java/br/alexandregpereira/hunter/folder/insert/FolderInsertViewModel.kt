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

package br.alexandregpereira.hunter.folder.insert

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.folder.AddMonstersToFolderUseCase
import br.alexandregpereira.hunter.domain.folder.GetFolderMonsterPreviewsByIdsUseCase
import br.alexandregpereira.hunter.domain.folder.GetMonsterFoldersUseCase
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEvent.Show
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult.OnMonsterRemoved
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult.OnSaved
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

@HiltViewModel
internal class FolderInsertViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMonsterFolders: GetMonsterFoldersUseCase,
    private val getFolderMonsterPreviewsByIds: GetFolderMonsterPreviewsByIdsUseCase,
    private val addMonstersToFolder: AddMonstersToFolderUseCase,
    private val folderInsertEventManager: FolderInsertEventManager,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle.getState())
    val state: StateFlow<FolderInsertViewState> = _state

    init {
        observeEvents()
        if (state.value.isOpen && state.value.monsterPreviews.isEmpty()) {
            load(state.value.monsterIndexes)
        }
    }

    fun onFolderNameFieldChange(folderName: String) {
        setState {
            copy(
                folderName = folderName,
                folderIndexSelected = folders.map { it.first }
                    .indexOfFirst { it.equals(folderName, ignoreCase = true) }
            )
        }
    }

    fun onRemoveMonster(monsterIndex: String) {
        loadMonsterPreviews(
            monsterIndexes = state.value.monsterIndexes.toMutableList().also {
                it.remove(monsterIndex)
            }
        )
        folderInsertEventManager.dispatchResult(OnMonsterRemoved(monsterIndex))
    }

    fun onSave() {
        if (state.value.folderName.isBlank()) return

        onClose()
        val indexes = state.value.monsterIndexes
        addMonstersToFolder(
            folderName = state.value.folderName.trim(),
            indexes = indexes
        ).flowOn(dispatcher)
            .onCompletion {
                folderInsertEventManager.dispatchResult(OnSaved(indexes))
            }
            .launchIn(viewModelScope)
    }

    fun onClose() {
        setState { copy(isOpen = false) }
    }

    private fun load(monsterIndexes: List<String>) {
        setState {
            copy(
                monsterIndexes = monsterIndexes,
                folderName = ""
            )
        }

        loadMonsterFolders()
        loadMonsterPreviews(monsterIndexes)
    }

    private fun loadMonsterFolders() {
        getMonsterFolders()
            .map { folders ->
                folders.map { it.name to it.monsters.size }
            }
            .flowOn(dispatcher)
            .onEach { folders ->
                setState { copy(folders = folders) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadMonsterPreviews(monsterIndexes: List<String>) {
        getFolderMonsterPreviewsByIds(monsterIndexes)
            .map { monsters ->
                monsters.asState()
            }
            .flowOn(dispatcher)
            .onEach { monsters ->
                setState {
                    copy(
                        monsterPreviews = monsters,
                        monsterIndexes = monsterIndexes,
                        isOpen = monsters.isNotEmpty()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeEvents() {
        folderInsertEventManager.events.onEach { event ->
            when (event) {
                is Show -> load(event.monsterIndexes)
            }
        }.launchIn(viewModelScope)
    }

    private fun setState(block: FolderInsertViewState.() -> FolderInsertViewState) {
        _state.value = state.value.block().saveState(savedStateHandle)
    }
}
