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

import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.monster.compendium.domain.MonsterCompendiumError
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import kotlin.native.ObjCName

@ObjCName(name = "MonsterCompendiumState", exact = true)
data class MonsterCompendiumState(
    val isLoading: Boolean = true,
    val items: List<MonsterCompendiumItemState> = emptyList(),
    val alphabet: List<String> = emptyList(),
    val alphabetSelectedIndex: Int = -1,
    val popupOpened: Boolean = false,
    val tableContent: List<TableContentItem> = emptyList(),
    val tableContentIndex: Int = -1,
    val tableContentInitialIndex: Int = 0,
    val tableContentOpened: Boolean = false,
    val errorState: MonsterCompendiumError? = null,
    val strings: MonsterCompendiumStrings = MonsterCompendiumStrings(),
)

@ObjCName(name = "MonsterCompendiumItemState", exact = true)
sealed class MonsterCompendiumItemState {

    val key: String
        get() = when (this) {
            is Title -> id
            is Item -> monster.index
        }

    data class Title(
        val id: String,
        val value: String,
        val isHeader: Boolean
    ) : MonsterCompendiumItemState()

    data class Item(
        val monster: MonsterPreviewState
    ) : MonsterCompendiumItemState()
}

data class MonsterPreviewState(
    val index: String = "",
    val name: String = "",
    val type: MonsterType = MonsterType.ABERRATION,
    val challengeRating: String = "",
    val imageUrl: String = "",
    val backgroundColorLight: String = "",
    val backgroundColorDark: String = "",
    val isImageHorizontal: Boolean = false,
    val imageContentScale: MonsterImageContentScale = MonsterImageContentScale.Fit,
)

fun MonsterCompendiumState.loading(isLoading: Boolean): MonsterCompendiumState {
    return this.copy(isLoading = isLoading, errorState = null)
}

fun MonsterCompendiumState.complete(
    items: List<MonsterCompendiumItemState>,
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

fun MonsterCompendiumState.error(error: Throwable): MonsterCompendiumState {
    val compendiumError =  if (error is MonsterCompendiumError) {
        error
    } else {
        MonsterCompendiumError.UnknownError(error)
    }

    return this.copy(errorState = compendiumError, isLoading = false)
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
