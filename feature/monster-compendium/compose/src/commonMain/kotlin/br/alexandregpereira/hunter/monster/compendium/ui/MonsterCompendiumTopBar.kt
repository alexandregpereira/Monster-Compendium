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

package br.alexandregpereira.hunter.monster.compendium.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.AppDropdownButton
import br.alexandregpereira.hunter.ui.compose.animatePressed

/**
 * The sort button is always less translucent than the top bar.
 */
private enum class TopBarScrollState(
    val topBarAlpha: Float,
    val sortButtonAlpha: Float,
) {
    AtTop(topBarAlpha = 1f, sortButtonAlpha = 1f),
    ScrollingDown(topBarAlpha = 0.7f, sortButtonAlpha = 0.85f),
    ScrollingUp(topBarAlpha = 0.9f, sortButtonAlpha = 0.95f),
}

@Composable
internal fun MonsterCompendiumTopBar(
    contentDescription: String,
    sortTitle: String,
    sortLabel: String,
    sortOptions: List<String>,
    sortOptionsOpened: Boolean,
    listState: LazyGridState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onSearchClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onSortOptionsClose: () -> Unit = {},
    onSortOptionSelected: (index: Int) -> Unit = {},
) {
    val scrollState = rememberTopBarScrollState(listState)
    val alpha by animateFloatAsState(
        targetValue = scrollState.topBarAlpha,
        label = "TopBarAlpha",
    )
    val sortButtonAlpha by animateFloatAsState(
        targetValue = scrollState.sortButtonAlpha,
        label = "SortButtonAlpha",
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface.copy(alpha = alpha))
            .padding(top = contentPadding.calculateTopPadding())
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        AppDropdownButton(
            text = sortLabel,
            options = sortOptions,
            expanded = sortOptionsOpened,
            title = sortTitle,
            size = AppButtonSize.VERY_SMALL,
            backgroundAlpha = sortButtonAlpha,
            onClick = onSortClick,
            onDismiss = onSortOptionsClose,
            onOptionSelected = onSortOptionSelected,
            modifier = Modifier.padding(start = 8.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .animatePressed(onClick = onSearchClick, pressedScale = .8f),
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = contentDescription,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

@Composable
private fun rememberTopBarScrollState(listState: LazyGridState): TopBarScrollState {
    var scrollState by remember {
        val isAtTop = listState.firstVisibleItemIndex == 0 &&
                listState.firstVisibleItemScrollOffset == 0
        mutableStateOf(if (isAtTop) TopBarScrollState.AtTop else TopBarScrollState.ScrollingUp)
    }
    LaunchedEffect(listState) {
        var previous = listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { current ->
                val (index, offset) = current
                val (previousIndex, previousOffset) = previous
                scrollState = when {
                    index == 0 && offset == 0 -> TopBarScrollState.AtTop
                    index > previousIndex -> TopBarScrollState.ScrollingDown
                    index < previousIndex -> TopBarScrollState.ScrollingUp
                    offset > previousOffset -> TopBarScrollState.ScrollingDown
                    offset < previousOffset -> TopBarScrollState.ScrollingUp
                    else -> scrollState
                }
                previous = current
            }
    }
    return scrollState
}
