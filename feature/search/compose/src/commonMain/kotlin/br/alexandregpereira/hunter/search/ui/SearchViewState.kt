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

import androidx.compose.ui.text.input.TextFieldValue
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState

internal data class SearchViewState(
    val searchValue: TextFieldValue = TextFieldValue(),
    val totalResults: Int = 0,
    val monsterRows: List<MonsterCardState> = emptyList(),
    val searchLabel: String = "",
    val searchResults: String = "",
    val isSearching: Boolean = false,
    val searchKeys: List<SearchKeyState> = emptyList(),
    val cursorAtTheEnd: Boolean = false,
)

internal data class SearchKeyState(
    private val key: String,
    private val symbol: String
) {
    val keyWithSymbols: String = if (symbol == "!") key else "$key$symbol"

    override fun toString(): String {
        return keyWithSymbols
    }
}
