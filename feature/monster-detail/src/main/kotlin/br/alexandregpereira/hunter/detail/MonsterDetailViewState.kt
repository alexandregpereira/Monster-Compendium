/*
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
