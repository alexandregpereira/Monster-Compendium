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

package br.alexandregpereira.hunter.search.ui

import androidx.annotation.DrawableRes
import br.alexandregpereira.hunter.search.R

internal data class SearchViewState(
    val searchValue: String = "",
    val monsters: List<MonsterCardState> = emptyList()
) {

    companion object {
        val Initial = SearchViewState()
    }
}

data class MonsterCardState(
    val index: String,
    val name: String,
    val imageUrl: String,
    val type: MonsterTypeState,
    val backgroundColorLight: String,
    val backgroundColorDark: String,
    val challengeRating: Float,
    val contentDescription: String = ""
)

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

internal fun SearchViewState.changeSearchValue(value: String): SearchViewState {
    return this.copy(searchValue = value)
}

internal fun SearchViewState.changeMonsters(monsters: List<MonsterCardState>): SearchViewState {
    return this.copy(monsters = monsters)
}
