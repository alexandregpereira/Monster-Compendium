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

package br.alexandregpereira.hunter.folder.preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEvent.*
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult.OnMonsterRemoved
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.folder.preview.domain.AddMonsterToFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.ClearFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.GetMonstersFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.RemoveMonsterFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.AddMonster
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.HideFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.ShowFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResult.OnFolderPreviewPreviewVisibilityChanges
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
internal class FolderPreviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val folderPreviewEventManager: FolderPreviewEventManager,
    private val getMonstersFromFolderPreview: GetMonstersFromFolderPreviewUseCase,
    private val addMonsterToFolderPreview: AddMonsterToFolderPreviewUseCase,
    private val removeMonsterFromFolderPreview: RemoveMonsterFromFolderPreviewUseCase,
    private val clearFolderPreviewUseCase: ClearFolderPreviewUseCase,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    private val monsterDetailEventListener: MonsterDetailEventListener,
    private val folderInsertEventDispatcher: FolderInsertEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _state: MutableStateFlow<FolderPreviewViewState> = MutableStateFlow(
        savedStateHandle.getState()
    )
    val state: StateFlow<FolderPreviewViewState> = _state

    init {
        observeEvents()
        if (!savedStateHandle.containsState()
            || (state.value.showPreview && state.value.monsters.isEmpty())
        ) {
            loadMonsters()
        }
    }

    fun onItemClick(monsterIndex: String) = navigateToMonsterDetail(monsterIndex)

    fun onItemLongClick(monsterIndex: String) = removeMonster(monsterIndex)

    fun onSave() {
        folderInsertEventDispatcher.dispatchEvent(
            event = Show(monsterIndexes = state.value.monsters.map { it.index })
        ).onEach { result ->
            when (result) {
                is FolderInsertResult.OnSaved -> clear()
                is OnMonsterRemoved -> removeMonster(result.monsterIndex)
            }
        }.launchIn(viewModelScope)
    }

    private fun observeEvents() {
        viewModelScope.launch {
            folderPreviewEventManager.events.collect { event ->
                when (event) {
                    is AddMonster -> addMonster(event.index)
                    HideFolderPreview -> hideFolderPreview()
                    is ShowFolderPreview -> loadMonsters()
                }
            }
        }

        viewModelScope.launch {
            monsterDetailEventListener.events.collect { event ->
                when (event) {
                    MonsterDetailEvent.Hide -> loadMonsters()
                    is Show -> hideFolderPreview()
                }
            }
        }
    }

    private fun clear() {
        hideFolderPreview()
        clearFolderPreviewUseCase()
            .flowOn(dispatcher)
            .launchIn(viewModelScope)
    }

    private fun addMonster(index: String) {
        addMonsterToFolderPreview(index)
            .flowOn(dispatcher)
            .onEach { monsters ->
                _state.value = state.value.changeMonsters(monsters)
            }
            .launchIn(viewModelScope)
    }

    private fun loadMonsters() {
        getMonstersFromFolderPreview()
            .flowOn(dispatcher)
            .onEach { monsters ->
                val showPreview = monsters.isNotEmpty()
                _state.value = state.value.changeMonsters(monsters = monsters)
                    .changeShowPreview(showPreview, savedStateHandle)
                dispatchFolderPreviewVisibilityChangesEvent()
            }
            .launchIn(viewModelScope)
    }

    private fun removeMonster(monsterIndex: String) {
        removeMonsterFromFolderPreview(monsterIndex)
            .flowOn(dispatcher)
            .onEach { monsters ->
                val showPreview = monsters.isNotEmpty()
                _state.value = state.value.changeMonsters(monsters = monsters)
                    .changeShowPreview(showPreview, savedStateHandle)
                if (showPreview.not()) {
                    dispatchFolderPreviewVisibilityChangesEvent()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun hideFolderPreview() {
        _state.value = _state.value.changeShowPreview(show = false, savedStateHandle)
        dispatchFolderPreviewVisibilityChangesEvent()
    }

    private fun dispatchFolderPreviewVisibilityChangesEvent() {
        folderPreviewEventManager.dispatchResult(
            OnFolderPreviewPreviewVisibilityChanges(isShowing = _state.value.showPreview)
        )
    }

    private fun navigateToMonsterDetail(monsterIndex: String) {
        monsterDetailEventDispatcher.dispatchEvent(
            Show(index = monsterIndex, indexes = state.value.monsters.map { it.index })
        )
    }
}
