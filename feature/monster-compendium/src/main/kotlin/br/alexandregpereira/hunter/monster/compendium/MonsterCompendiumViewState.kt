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

package br.alexandregpereira.hunter.monster.compendium

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.ui.compendium.SectionState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState

data class MonsterCompendiumViewState(
    val isLoading: Boolean = true,
    val monstersBySection: Map<SectionState, List<MonsterCardState>> = emptyMap(),
    val alphabet: List<Char> = emptyList(),
    val alphabetIndex: Int = 0,
    val alphabetOpened: Boolean = false,
    val isShowingMonsterFolderPreview: Boolean = false,
)

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
    monstersBySection: Map<SectionState, List<MonsterCardState>>,
    alphabet: List<Char>,
    alphabetIndex: Int
) = this.copy(
    isLoading = false,
    monstersBySection = monstersBySection,
    alphabet = alphabet,
    alphabetIndex = alphabetIndex
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
