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

import androidx.annotation.DrawableRes
import br.alexandregpereira.hunter.monster.compendium.R

data class MonsterCompendiumViewState(
    val isLoading: Boolean = true,
    val monstersBySection: Map<SectionState, List<MonsterRowState>> = emptyMap(),
    val alphabet: List<Char> = emptyList(),
    val alphabetIndex: Int = 0,
    val alphabetOpened: Boolean = false,
    val initialScrollItemPosition: Int = 0,
    val isShowingMonsterFolderPreview: Boolean = false,
) {

    companion object {
        val Initial = MonsterCompendiumViewState()
    }
}

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

data class MonsterCardState(
    val index: String,
    val name: String,
    val imageState: MonsterImageState,
)

data class MonsterImageState(
    val url: String,
    val type: MonsterTypeState,
    val backgroundColor: ColorState,
    val challengeRating: Float,
    val isHorizontal: Boolean = false,
    val contentDescription: String = ""
)

data class ColorState(
    val light: String,
    val dark: String
) {

    fun getColor(isDarkTheme: Boolean): String = if (isDarkTheme) dark else light
}

enum class MonsterTypeState(@DrawableRes val iconRes: Int) {
    ABERRATION(R.drawable.ic_aberration),
    BEAST(R.drawable.ic_beast),
    CELESTIAL(R.drawable.ic_celestial),
    CONSTRUCT(R.drawable.ic_construct),
    DRAGON(R.drawable.ic_dragon),
    ELEMENTAL(R.drawable.ic_elemental),
    FEY(R.drawable.ic_fey),
    FIEND(R.drawable.ic_fiend),
    GIANT(R.drawable.ic_giant),
    HUMANOID(R.drawable.ic_humanoid),
    MONSTROSITY(R.drawable.ic_monstrosity),
    OOZE(R.drawable.ic_ooze),
    PLANT(R.drawable.ic_plant),
    UNDEAD(R.drawable.ic_undead)
}

fun MonsterCompendiumViewState.loading(isLoading: Boolean): MonsterCompendiumViewState {
    return this.copy(isLoading = isLoading)
}

fun MonsterCompendiumViewState.complete(
    monstersBySection: Map<SectionState, List<MonsterRowState>>,
    alphabet: List<Char>,
    alphabetIndex: Int,
    initialScrollItemPosition: Int
) = this.copy(
    isLoading = false,
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

fun MonsterCompendiumViewState.showMonsterFolderPreview(
    isShowing: Boolean,
) = this.copy(
    isShowingMonsterFolderPreview = isShowing,
)

infix fun MonsterCardState.and(that: MonsterCardState?) = MonsterRowState(
    leftMonsterCardState = this,
    rightMonsterCardState = that
)
