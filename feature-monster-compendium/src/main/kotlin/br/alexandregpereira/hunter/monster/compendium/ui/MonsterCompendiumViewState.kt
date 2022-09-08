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

package br.alexandregpereira.hunter.monster.compendium.ui

data class MonsterCompendiumViewState(
    val isLoading: Boolean = false,
    val monstersBySection: Map<SectionState, List<MonsterRowState>> = emptyMap(),
    val alphabet: List<Char> = emptyList(),
    val alphabetIndex: Int = 0,
    val alphabetOpened: Boolean = false,
    val initialScrollItemPosition: Int = 0,
) {

    companion object {
        val Initial = MonsterCompendiumViewState()
    }
}

val MonsterCompendiumViewState.Loading: MonsterCompendiumViewState
    get() = this.copy(isLoading = true)

fun MonsterCompendiumViewState.complete(
    monstersBySection: Map<SectionState, List<MonsterRowState>>,
    alphabet: List<Char>,
    alphabetIndex: Int,
    initialScrollItemPosition: Int
) = this.copy(
    monstersBySection = monstersBySection,
    alphabet = alphabet,
    alphabetIndex = alphabetIndex,
    initialScrollItemPosition = initialScrollItemPosition
)

fun MonsterCompendiumViewState.alphabetIndex(
    alphabetIndex: Int,
) = this.copy(
    alphabetIndex = alphabetIndex,
)

fun MonsterCompendiumViewState.alphabetOpened(
    alphabetOpened: Boolean,
) = this.copy(
    alphabetOpened = alphabetOpened,
)

data class SectionState(
    val title: String,
    val id: String = title,
    val parentTitle: String? = null,
    val isHeader: Boolean = false,
)

data class MonsterRowState(
    val leftMonsterCardState: MonsterCardState,
    val rightMonsterCardState: MonsterCardState?,
)

infix fun MonsterCardState.and(that: MonsterCardState?) = MonsterRowState(
    leftMonsterCardState = this,
    rightMonsterCardState = that
)
