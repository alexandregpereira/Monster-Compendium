/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.folder.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.folder.preview.domain.AddMonsterToFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.GetMonstersFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.GetMonstersFromFolderPreviewUseCase.Companion.TEMPORARY_FOLDER_NAME
import br.alexandregpereira.hunter.folder.preview.domain.RemoveMonsterFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.model.MonsterFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewConsumerEvent.OnFolderPreviewPreviewVisibilityChanges
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewConsumerEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.AddMonster
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.HideFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.ShowFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FolderPreviewViewModel @Inject constructor(
    private val folderPreviewEventListener: FolderPreviewEventListener,
    private val folderPreviewConsumerEventDispatcher: FolderPreviewConsumerEventDispatcher,
    private val getMonstersFromFolderPreview: GetMonstersFromFolderPreviewUseCase,
    private val addMonsterToFolderPreview: AddMonsterToFolderPreviewUseCase,
    private val removeMonsterFromFolderPreview: RemoveMonsterFromFolderPreviewUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _state: MutableStateFlow<FolderPreviewViewState> = MutableStateFlow(
        FolderPreviewViewState.INITIAL
    )
    val state: StateFlow<FolderPreviewViewState> = _state

    private val _action: MutableSharedFlow<FolderPreviewAction> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val action: SharedFlow<FolderPreviewAction> = _action

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
                    ShowFolderPreview -> showFolderPreview()
                }
            }
        }
    }

    private fun addMonster(index: String) {
        addMonsterToFolderPreview(index)
            .onEach { monsters ->
                changeMonsters(monsters)
            }
            .flowOn(dispatcher)
            .launchIn(viewModelScope)
    }

    private fun loadMonsters() {
        getMonstersFromFolderPreview()
            .flowOn(dispatcher)
            .onEach { monsters ->
                changeMonsters(monsters)
            }
            .launchIn(viewModelScope)
    }

    private fun removeMonster(monsterIndex: String) {
        removeMonsterFromFolderPreview(monsterIndex)
            .onEach { monsters ->
                changeMonsters(monsters)
            }
            .flowOn(dispatcher)
            .launchIn(viewModelScope)
    }

    private fun hideFolderPreview() {
        _state.value = _state.value.changeShowPreview(show = false)
        dispatchFolderPreviewVisibilityChangesEvent()
    }

    private fun showFolderPreview() {
        _state.value = _state.value.changeShowPreview(show = state.value.monsters.isNotEmpty())
        dispatchFolderPreviewVisibilityChangesEvent()
    }

    private fun dispatchFolderPreviewVisibilityChangesEvent() {
        folderPreviewConsumerEventDispatcher.dispatchEvent(
            OnFolderPreviewPreviewVisibilityChanges(isShowing = _state.value.showPreview)
        )
    }

    private fun navigateToMonsterDetail(monsterIndex: String) {
        _action.tryEmit(
            FolderPreviewAction.NavigateToDetail(
                folderName = TEMPORARY_FOLDER_NAME,
                monsterIndex = monsterIndex
            )
        )
    }

    private fun changeMonsters(monsters: List<MonsterFolderPreview>) {
        _state.value = _state.value.changeMonsters(monsters)
        dispatchFolderPreviewVisibilityChangesEvent()
    }
}
