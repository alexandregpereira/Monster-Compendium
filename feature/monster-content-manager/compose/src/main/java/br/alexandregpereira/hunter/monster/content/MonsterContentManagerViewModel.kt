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

package br.alexandregpereira.hunter.monster.content

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.state.StateHolder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

internal class MonsterContentManagerViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val stateHolder: MonsterContentManagerStateHolder,
) : ViewModel(), StateHolder<MonsterContentManagerState> by stateHolder{

    init {
        stateHolder.state
            .map {
                it.saveState(savedStateHandle)
            }
            .launchIn(viewModelScope)
    }

    fun onAddContentClick(acronym: String) {
        stateHolder.onAddContentClick(acronym)
    }

    fun onRemoveContentClick(acronym: String) {
        stateHolder.onRemoveContentClick(acronym)
    }

    fun onPreviewClick(acronym: String, name: String) {
        stateHolder.onPreviewClick(acronym, name)
    }

    fun onClose() {
        stateHolder.onClose()
    }

    override fun onCleared() {
        super.onCleared()
        stateHolder.onCleared()
    }
}
