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

package br.alexandregpereira.hunter.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import br.alexandregpereira.hunter.search.ui.SearchScreen
import org.koin.compose.koinInject

@Composable
fun SearchScreenFeature(
    contentPadding: PaddingValues = PaddingValues(),
) {
    val stateHolder: SearchStateHolder = koinInject()
    val state by stateHolder.state.collectAsState()

    val initialFirstVisibleItemIndex = remember { state.firstVisibleItemIndex }
    val initialFirstVisibleItemScrollOffset = remember { state.firstVisibleItemScrollOffset }
    val initialSearchKeysScrollOffset = remember { state.searchKeysScrollOffset }

    SearchScreen(
        searchValue = state.searchValue,
        monsterRows = state.monsterRows,
        searchLabel = state.searchLabel,
        searchResults = state.searchResults,
        isSearching = state.isSearching,
        searchKeys = state.searchKeys,
        initialFirstVisibleItemIndex = initialFirstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = initialFirstVisibleItemScrollOffset,
        initialSearchKeysScrollOffset = initialSearchKeysScrollOffset,
        contentPaddingValues = contentPadding,
        onSearchValueChange = stateHolder::onSearchValueChange,
        onCardClick = stateHolder::onItemClick,
        onCardLongClick = stateHolder::onItemLongClick,
        onSearchKeyClick = stateHolder::onSearchKeyClick,
        onAddClick = stateHolder::onAddClick,
        onScrollChanges = stateHolder::onScrollChanges,
        onSearchKeysScrollChanges = stateHolder::onSearchKeysScrollChanges,
    )
}
