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
import br.alexandregpereira.hunter.ui.compose.AppCircleButton
import br.alexandregpereira.hunter.ui.compose.ClearFocusWhenScrolling
import kotlin.math.absoluteValue

@Composable
internal fun SearchScreen(
    state: SearchViewState,
    contentPaddingValues: PaddingValues = PaddingValues(),
    onSearchValueChange: (TextFieldValue) -> Unit = {},
    onCardClick: (String) -> Unit = {},
    onCardLongClick: (String) -> Unit = {},
    onSearchKeyClick: (Int) -> Unit = {},
    onAddClick: () -> Unit = {},
) = Box(modifier = Modifier.fillMaxSize()) {
    val listState = rememberLazyGridState()
    val focusManager = LocalFocusManager.current

    ClearFocusWhenScrolling(listState)

    SearchGrid(
        monsterRows = state.monsterRows,
        totalResults = state.searchResults,
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
            text = state.searchValue,
            searchLabel = state.searchLabel,
            onValueChange = { newValue ->
                if (!isProgrammaticChange) {
                    onSearchValueChange(newValue)
                }
            },
            isSearching = state.isSearching,
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
            modifier = Modifier,
            searchKeys = state.searchKeys,
            onClick = {
                isProgrammaticChange = true
                onSearchKeyClick(it)
                focusRequester.requestFocus()
                isProgrammaticChange = false
            },
        )
    }

    AnimatedVisibility(
        visible = state.monsterRows.isNotEmpty(),
        modifier = Modifier.align(Alignment.BottomEnd),
        enter = fadeIn(spring()),
        exit = fadeOut(spring()),
    ) {
        AppCircleButton(
            modifier = Modifier.padding(16.dp),
            onClick = onAddClick
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
            )
        }
    }
}
