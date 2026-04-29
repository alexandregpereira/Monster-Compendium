/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.monster.content

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MonsterContentManagerState(
    val monsterContents: ImmutableList<MonsterContentState> = persistentListOf(),
    val isOpen: Boolean = false,
    val isLoading: Boolean = false,
    val showGenericError: Boolean = false,
    val strings: MonsterContentManagerStrings = MonsterContentManagerStrings(),
) {
    val monsterContentsByAcronym = monsterContents.associateBy { it.acronym }
}

data class MonsterContentState(
    val acronym: String,
    val name: String,
    val originalName: String?,
    val totalMonsters: Int,
    val summary: String,
    val coverImageUrl: String?,
    val isAdded: Boolean,
    val isDefault: Boolean = false,
)

internal fun MonsterContentManagerState.hide(): MonsterContentManagerState {
    return copy(isOpen = false)
}
