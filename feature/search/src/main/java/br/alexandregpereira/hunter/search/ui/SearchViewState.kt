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
