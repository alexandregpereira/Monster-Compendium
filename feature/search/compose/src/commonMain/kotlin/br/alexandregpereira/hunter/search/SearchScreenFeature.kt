/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
