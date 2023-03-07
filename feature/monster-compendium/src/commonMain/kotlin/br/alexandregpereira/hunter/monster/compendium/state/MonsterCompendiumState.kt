/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.monster.compendium.state

import br.alexandregpereira.hunter.monster.compendium.domain.MonsterCompendiumError
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem

data class MonsterCompendiumState(
    val isLoading: Boolean = true,
    val items: List<MonsterCompendiumItem> = emptyList(),
    val alphabet: List<String> = emptyList(),
    val alphabetSelectedIndex: Int = -1,
    val popupOpened: Boolean = false,
    val tableContent: List<TableContentItem> = emptyList(),
    val tableContentIndex: Int = -1,
    val tableContentInitialIndex: Int = 0,
    val tableContentOpened: Boolean = false,
    val isShowingMonsterFolderPreview: Boolean = false,
    val errorState: MonsterCompendiumError? = null
)

fun MonsterCompendiumState.loading(isLoading: Boolean): MonsterCompendiumState {
    return this.copy(isLoading = isLoading)
}

fun MonsterCompendiumState.complete(
    items: List<MonsterCompendiumItem>,
    alphabet: List<String>,
    tableContent: List<TableContentItem>,
    alphabetSelectedIndex: Int,
    tableContentIndex: Int,
) = this.copy(
    isLoading = false,
    items = items,
    alphabet = alphabet,
    tableContent = tableContent,
    alphabetSelectedIndex = alphabetSelectedIndex,
    tableContentIndex = tableContentIndex,
)

fun MonsterCompendiumState.error(): MonsterCompendiumState {
    return this.copy(errorState = MonsterCompendiumError.UnknownError())
}

fun MonsterCompendiumState.tableContentIndex(
    tableContentIndex: Int,
    alphabetSelectedIndex: Int,
) = this.copy(
    tableContentIndex = tableContentIndex,
    alphabetSelectedIndex = alphabetSelectedIndex
)

fun MonsterCompendiumState.popupOpened(
    popupOpened: Boolean,
) = this.copy(
    popupOpened = popupOpened,
)

fun MonsterCompendiumState.tableContentOpened(
    tableContentOpened: Boolean,
) = this.copy(
    tableContentOpened = tableContentOpened,
)

fun MonsterCompendiumState.showMonsterFolderPreview(
    isShowing: Boolean
) = this.copy(
    isShowingMonsterFolderPreview = isShowing,
)
