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

package br.alexandregpereira.hunter.monster.compendium.ui

import androidx.annotation.DrawableRes
import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.monster.compendium.R

data class MonsterCompendiumViewState(
    val isLoading: Boolean = true,
    val monstersBySection: Map<SectionState, List<MonsterRowState>> = emptyMap(),
    val alphabet: List<Char> = emptyList(),
    val alphabetIndex: Int = 0,
    val alphabetOpened: Boolean = false,
    val initialScrollItemPosition: Int = 0,
    val isShowingMonsterFolderPreview: Boolean = false,
    val compendiumIndex: Int = -1
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

internal fun SavedStateHandle.getState(): MonsterCompendiumViewState {
    return MonsterCompendiumViewState(
        isShowingMonsterFolderPreview = this["isShowingMonsterFolderPreview"] ?: false
    )
}

internal fun MonsterCompendiumViewState.saveState(
    savedStateHandle: SavedStateHandle
): MonsterCompendiumViewState {
    savedStateHandle["isShowingMonsterFolderPreview"] = this.isShowingMonsterFolderPreview
    return this
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
    savedStateHandle: SavedStateHandle
) = this.copy(
    isShowingMonsterFolderPreview = isShowing,
).saveState(savedStateHandle)

infix fun MonsterCardState.and(that: MonsterCardState?) = MonsterRowState(
    leftMonsterCardState = this,
    rightMonsterCardState = that
)
