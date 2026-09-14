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
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.search.SearchTip
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compose.AppCircleButton
import br.alexandregpereira.hunter.ui.compose.AppTopBar
import br.alexandregpereira.hunter.ui.compose.ClearFocusWhenScrolling
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessageContent
import kotlin.math.absoluteValue

@Composable
internal fun SearchScreen(
    title: String,
    searchValue: TextFieldValue,
    monsterRows: List<MonsterCardState>,
    searchResults: String,
    isSearching: Boolean,
    searchKeys: List<SearchKeyState>,
    searchTipsTitle: String,
    searchTips: List<SearchTip>,
    searchNoResultsTitle: String,
    searchNoResultsDescription: String,
    contentState: SearchContentState,
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
    onClose: () -> Unit = {},
) = Box(modifier = Modifier.fillMaxSize()) {
    val listState = rememberLazyGridState(
        initialFirstVisibleItemIndex = initialFirstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = initialFirstVisibleItemScrollOffset,
    )
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current
    var topBarHeight by remember { mutableStateOf(0.dp) }
    // Leaves room for the search key buttons shown below the top bar.
    val contentTopPadding = topBarHeight + 32.dp

    ClearFocusWhenScrolling(listState)

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) -> onScrollChanges(index, offset) }
    }

    Crossfade(
        targetState = contentState,
        animationSpec = spring(),
    ) { state ->
        when (state) {
            SearchContentState.Tips -> SearchTips(
                title = searchTipsTitle,
                tips = searchTips,
                contentPaddingValues = PaddingValues(
                    top = contentTopPadding,
                    bottom = contentPaddingValues.calculateBottomPadding(),
                ),
            )
            SearchContentState.Empty -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = contentTopPadding,
                        bottom = contentPaddingValues.calculateBottomPadding(),
                    )
            ) {
                EmptyScreenMessageContent(
                    title = searchNoResultsTitle,
                    description = searchNoResultsDescription,
                    modifier = Modifier.padding(16.dp),
                )
            }
            SearchContentState.Results -> SearchGrid(
                monsterRows = monsterRows,
                totalResults = searchResults,
                listState = listState,
                contentPadding = PaddingValues(
                    top = contentTopPadding,
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
        }
    }

    Column {
        val focusRequester = remember { FocusRequester() }
        AppTopBar(
            title = title,
            listState = listState,
            onCloseClick = {
                focusManager.clearFocus()
                onClose()
            },
            modifier = Modifier
                .onSizeChanged { size ->
                    topBarHeight = with(density) { size.height.toDp() }
                }
                .padding(top = contentPaddingValues.calculateTopPadding()),
            bottomContent = {
                SearchBar(
                    text = searchValue,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                        )
                    },
                    onValueChange = { newValue ->
                        if (newValue.text != searchValue.text) {
                            onSearchValueChange(newValue)
                        }
                    },
                    isSearching = isSearching,
                    modifier = Modifier.focusRequester(focusRequester),
                )
            },
        )

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
                onSearchKeyClick(it)
                focusRequester.requestFocus()
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
