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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow

/**
 * Reports the range of items on screen every time it changes, which is how far the content has been
 * scrolled. Both ends are needed to measure it: scrolling down moves the last visible item, while
 * scrolling back up moves the first one.
 *
 * Supports [LazyGridState], [LazyStaggeredGridState] and [LazyListState]. Nothing is reported for
 * other states.
 */
@Composable
fun OnVisibleItemsChange(
    listState: ScrollableState,
    onChange: (firstVisibleItemIndex: Int, lastVisibleItemIndex: Int, itemsSize: Int) -> Unit,
) {
    val getVisibleItems = remember(listState) { listState.visibleItemsProvider() }
    val currentOnChange by rememberUpdatedState(onChange)
    LaunchedEffect(getVisibleItems) {
        if (getVisibleItems == null) return@LaunchedEffect
        snapshotFlow { getVisibleItems() }.collect { visibleItems ->
            currentOnChange(visibleItems.first, visibleItems.last, visibleItems.itemsSize)
        }
    }
}

private data class VisibleItems(val first: Int, val last: Int, val itemsSize: Int)

/**
 * Returns the range of visible item indexes and the total item count, or null when the state is not
 * a supported lazy layout state. The indexes are [NO_VISIBLE_ITEM] while nothing is laid out yet.
 *
 * The visible items of a staggered grid are not ordered by index, so the bounds are taken instead
 * of the first and the last ones.
 */
private fun ScrollableState.visibleItemsProvider(): (() -> VisibleItems)? {
    return when (val state = this) {
        is LazyGridState -> ({ state.layoutInfo.run { visibleItemsInfo.map { it.index }.toVisibleItems(totalItemsCount) } })
        is LazyStaggeredGridState -> ({ state.layoutInfo.run { visibleItemsInfo.map { it.index }.toVisibleItems(totalItemsCount) } })
        is LazyListState -> ({ state.layoutInfo.run { visibleItemsInfo.map { it.index }.toVisibleItems(totalItemsCount) } })
        else -> null
    }
}

private fun List<Int>.toVisibleItems(itemsSize: Int): VisibleItems = VisibleItems(
    first = minOrNull() ?: NO_VISIBLE_ITEM,
    last = maxOrNull() ?: NO_VISIBLE_ITEM,
    itemsSize = itemsSize,
)

private const val NO_VISIBLE_ITEM = -1
