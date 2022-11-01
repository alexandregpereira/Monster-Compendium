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

package br.alexandregpereira.hunter.app

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.app.MainViewEvent.BottomNavigationItemClick
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailResult.OnVisibilityChanges
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailResultListener
import br.alexandregpereira.hunter.event.folder.detail.collectOnVisibilityChanges
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val monsterDetailEventListener: MonsterDetailEventListener,
    private val folderDetailResultListener: FolderDetailResultListener,
) : ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle.getState())
    val state: StateFlow<MainViewState> = _state

    init {
        observeMonsterDetailEvents()
        observeFolderDetailResults()
    }

    private fun observeMonsterDetailEvents() {
        monsterDetailEventListener.events.onEach { event ->
            when (event) {
                is MonsterDetailEvent.Show -> setState { copy(showMonsterDetail = true) }
                MonsterDetailEvent.Hide -> setState { copy(showMonsterDetail = false) }
            }
        }.launchIn(viewModelScope)
    }

    private fun observeFolderDetailResults() {
        folderDetailResultListener.collectOnVisibilityChanges { result ->
            setState { copy(showFolderDetail = result.isShowing) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MainViewEvent) {
        when (event) {
            is BottomNavigationItemClick -> setState { copy(bottomBarItemSelected = event.item) }
        }
    }

    private fun setState(block: MainViewState.() -> MainViewState) {
        _state.value = state.value.block().saveState(savedStateHandle)
    }
}
