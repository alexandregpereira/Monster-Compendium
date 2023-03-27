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
import br.alexandregpereira.hunter.monster.compendium.ui.TableContentItemState
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState
import br.alexandregpereira.hunter.ui.compose.LoadingScreenState
import br.alexandregpereira.hunter.ui.compose.LoadingScreenState.LoadingScreen

data class MonsterCompendiumViewState(
    val loadingState: LoadingScreenState = LoadingScreen,
    val items: List<CompendiumItemState> = emptyList(),
    val alphabet: List<String> = emptyList(),
    val alphabetSelectedIndex: Int = -1,
    val popupOpened: Boolean = false,
    val tableContent: List<TableContentItemState> = emptyList(),
    val tableContentIndex: Int = -1,
    val tableContentInitialIndex: Int = 0,
    val tableContentOpened: Boolean = false,
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
