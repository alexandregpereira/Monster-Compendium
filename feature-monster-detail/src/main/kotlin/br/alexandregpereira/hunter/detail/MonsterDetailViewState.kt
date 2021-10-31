/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
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

import br.alexandregpereira.hunter.detail.ui.MonsterState

data class MonsterDetailViewState(
    val isLoading: Boolean = false,
    val initialMonsterIndex: Int = 0,
    val monsters: List<MonsterState> = emptyList(),
    val showOptions: Boolean = false,
    val options: List<MonsterDetailOptionState> = emptyList()
) {

    companion object {
        val Initial = MonsterDetailViewState()
    }
}

val MonsterDetailViewState.Loading: MonsterDetailViewState
    get() = this.copy(isLoading = true)

val MonsterDetailViewState.NotLoading: MonsterDetailViewState
    get() = this.copy(isLoading = false)

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
