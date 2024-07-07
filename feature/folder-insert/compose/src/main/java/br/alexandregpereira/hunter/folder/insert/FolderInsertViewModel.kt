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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class FolderInsertViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val stateHolder: FolderInsertStateHolder,
) : ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle.getState())
    val state: StateFlow<FolderInsertState> = _state

    init {
        stateHolder.state
            .map {
                it.saveState(savedStateHandle)
            }
            .onEach { _state.value = it }
            .launchIn(viewModelScope)
    }

    fun onFolderNameFieldChange(folderName: String) {
        stateHolder.onFolderNameFieldChange(folderName)
    }

    fun onRemoveMonster(monsterIndex: String) {
        stateHolder.onRemoveMonster(monsterIndex)
    }

    fun onSave() {
        stateHolder.onSave()
    }

    fun onClose() {
        stateHolder.onClose()
    }

    override fun onCleared() {
        super.onCleared()
        stateHolder.onCleared()
    }
}
