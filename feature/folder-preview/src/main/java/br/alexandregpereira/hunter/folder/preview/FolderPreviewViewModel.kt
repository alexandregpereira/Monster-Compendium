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
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.folder.preview.domain.AddMonsterToFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.GetMonstersFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.RemoveMonsterFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewConsumerEvent.OnFolderPreviewPreviewVisibilityChanges
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewConsumerEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.AddMonster
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.HideFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.ShowFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventListener
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
    private val folderPreviewEventListener: FolderPreviewEventListener,
    private val folderPreviewConsumerEventDispatcher: FolderPreviewConsumerEventDispatcher,
    private val getMonstersFromFolderPreview: GetMonstersFromFolderPreviewUseCase,
    private val addMonsterToFolderPreview: AddMonsterToFolderPreviewUseCase,
    private val removeMonsterFromFolderPreview: RemoveMonsterFromFolderPreviewUseCase,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _state: MutableStateFlow<FolderPreviewViewState> = MutableStateFlow(
        savedStateHandle.getState()
    )
    val state: StateFlow<FolderPreviewViewState> = _state

    init {
        observeEvents()
        loadMonsters()
    }

    fun onItemClick(monsterIndex: String) = navigateToMonsterDetail(monsterIndex)

    fun onItemLongClick(monsterIndex: String) = removeMonster(monsterIndex)

    private fun observeEvents() {
        viewModelScope.launch {
            folderPreviewEventListener.events.collect { event ->
                when (event) {
                    is AddMonster -> addMonster(event.index)
                    HideFolderPreview -> hideFolderPreview()
                    is ShowFolderPreview -> showFolderPreview(event.force)
                }
            }
        }
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
                val showPreview = if (savedStateHandle.containsState()) {
                    state.value.showPreview
                } else {
                    monsters.isNotEmpty()
                }
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

    private fun showFolderPreview(force: Boolean) {
        val show = if (force) true else state.value.monsters.isNotEmpty()
        _state.value = _state.value.changeShowPreview(show = show, savedStateHandle)
        dispatchFolderPreviewVisibilityChangesEvent()
    }

    private fun dispatchFolderPreviewVisibilityChangesEvent() {
        folderPreviewConsumerEventDispatcher.dispatchEvent(
            OnFolderPreviewPreviewVisibilityChanges(isShowing = _state.value.showPreview)
        )
    }

    private fun navigateToMonsterDetail(monsterIndex: String) {
        monsterDetailEventDispatcher.dispatchEvent(
            Show(index = monsterIndex, indexes = state.value.monsters.map { it.index })
        )
    }
}
