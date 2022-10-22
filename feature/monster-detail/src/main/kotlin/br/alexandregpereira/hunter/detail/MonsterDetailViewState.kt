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

package br.alexandregpereira.hunter.detail

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.detail.ui.MonsterState

data class MonsterDetailViewState(
    val initialMonsterIndex: Int = 0,
    val monsters: List<MonsterState> = emptyList(),
    val showOptions: Boolean = false,
    val options: List<MonsterDetailOptionState> = emptyList(),
    val showDetail: Boolean = false,
) {

    val isLoading: Boolean
        get() = monsters.isEmpty()
}

fun SavedStateHandle.getState(): MonsterDetailViewState {
    return MonsterDetailViewState(
        showDetail = this["showDetail"] ?: false
    )
}

fun MonsterDetailViewState.saveState(savedStateHandle: SavedStateHandle): MonsterDetailViewState {
    savedStateHandle["showDetail"] = showDetail
    return this
}

val MonsterDetailViewState.ShowOptions: MonsterDetailViewState
    get() = this.copy(showOptions = true)

val MonsterDetailViewState.HideOptions: MonsterDetailViewState
    get() = this.copy(showOptions = false)

fun MonsterDetailViewState.complete(
    initialMonsterIndex: Int,
    monsters: List<MonsterState>,
    options: List<MonsterDetailOptionState>
): MonsterDetailViewState = this.copy(
    initialMonsterIndex = initialMonsterIndex,
    monsters = monsters,
    options = options
)
