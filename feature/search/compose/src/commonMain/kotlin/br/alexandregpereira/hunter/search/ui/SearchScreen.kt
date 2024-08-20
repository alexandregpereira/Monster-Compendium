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

package br.alexandregpereira.hunter.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compose.AppCircleButton
import br.alexandregpereira.hunter.ui.compose.ClearFocusWhenScrolling
import kotlin.math.absoluteValue

@Composable
internal fun SearchScreen(
    searchValue: TextFieldValue,
    monsterRows: List<MonsterCardState>,
    searchLabel: String,
    searchResults: String,
    isSearching: Boolean,
    searchKeys: List<SearchKeyState>,
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0,
    initialSearchKeysScrollOffset: Int = 0,
    contentPaddingValues: PaddingValues = PaddingValues(),
    onSearchValueChange: (TextFieldValue) -> Unit = {},
    onCardClick: (String) -> Unit = {},
    onCardLongClick: (String) -> Unit = {},
    onSearchKeyClick: (Int) -> Unit = {},
    onAddClick: () -> Unit = {},
    onScrollChanges: (Int, Int) -> Unit = { _, _ -> },
    onSearchKeysScrollChanges: (Int) -> Unit = {},
) = Box(modifier = Modifier.fillMaxSize()) {
    val listState = rememberLazyGridState(
        initialFirstVisibleItemIndex = initialFirstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = initialFirstVisibleItemScrollOffset,
    )
    val focusManager = LocalFocusManager.current

    ClearFocusWhenScrolling(listState)
    
    OnScrollChanges(
        getFirstVisibleItemValues = {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        },
        onScrollChanges = onScrollChanges
    )

    SearchGrid(
        monsterRows = monsterRows,
        totalResults = searchResults,
        listState = listState,
        contentPadding = PaddingValues(
            top = contentPaddingValues.calculateTopPadding() + 96.dp + 8.dp,
            bottom = contentPaddingValues.calculateBottomPadding()
        ),
        onCardClick = {
            focusManager.clearFocus()
            onCardClick(it)
        },
        onCardLongClick = {
            focusManager.clearFocus()
            onCardLongClick(it)
        }
    )

    Column {
        var isProgrammaticChange by remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }
        SearchBar(
            text = searchValue,
            searchLabel = searchLabel,
            onValueChange = { newValue ->
                if (!isProgrammaticChange) {
                    onSearchValueChange(newValue)
                }
            },
            isSearching = isSearching,
            modifier = Modifier
                .focusRequester(focusRequester)
                .background(color = MaterialTheme.colors.surface)
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp + contentPaddingValues.calculateTopPadding())
        )

        val density = LocalDensity.current
        val scrollTriggerInPixels = with(density) { 56.dp.toPx() }
        Spacer(modifier = Modifier.height(8.dp))
        SearchKeyButtons(
            shouldShow = {
                val offset: Int = listState.layoutInfo.visibleItemsInfo
                    .firstOrNull()?.offset?.y?.absoluteValue ?: 0
                offset < scrollTriggerInPixels
            },
            initialScrollOffset = initialSearchKeysScrollOffset,
            modifier = Modifier,
            searchKeys = searchKeys,
            onScrollChanges = onSearchKeysScrollChanges,
            onClick = {
                isProgrammaticChange = true
                onSearchKeyClick(it)
                focusRequester.requestFocus()
                isProgrammaticChange = false
            },
        )
    }

    AnimatedVisibility(
        visible = monsterRows.isNotEmpty(),
        modifier = Modifier.align(Alignment.BottomEnd),
        enter = fadeIn(spring()),
        exit = fadeOut(spring()),
    ) {
        AppCircleButton(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
            onClick = onAddClick
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun OnScrollChanges(
    getFirstVisibleItemValues: () -> Pair<Int, Int>,
    onScrollChanges: (Int, Int) -> Unit
) {
    val (firstVisibleItemIndex, firstVisibleItemScrollOffset) = getFirstVisibleItemValues()
    onScrollChanges(
        firstVisibleItemIndex,
        firstVisibleItemScrollOffset
    )
}
