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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.animatePressed

private const val TopBarAlphaAtTop = 1f
private const val TopBarAlphaScrollingUp = 0.9f
private const val TopBarAlphaScrollingDown = 0.7f

@Composable
internal fun MonsterCompendiumTopBar(
    contentDescription: String,
    listState: LazyGridState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: () -> Unit = {},
) {
    val alpha by animateFloatAsState(
        targetValue = rememberTopBarAlpha(listState),
        label = "TopBarAlpha",
    )
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface.copy(alpha = alpha))
            .padding(top = contentPadding.calculateTopPadding())
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .animatePressed(onClick = onClick, pressedScale = .8f),
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
private fun rememberTopBarAlpha(listState: LazyGridState): Float {
    var alpha by remember {
        val isAtTop = listState.firstVisibleItemIndex == 0 &&
                listState.firstVisibleItemScrollOffset == 0
        mutableFloatStateOf(if (isAtTop) TopBarAlphaAtTop else TopBarAlphaScrollingUp)
    }
    LaunchedEffect(listState) {
        var previous = listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { current ->
                val (index, offset) = current
                val (previousIndex, previousOffset) = previous
                alpha = when {
                    index == 0 && offset == 0 -> TopBarAlphaAtTop
                    index > previousIndex -> TopBarAlphaScrollingDown
                    index < previousIndex -> TopBarAlphaScrollingUp
                    offset > previousOffset -> TopBarAlphaScrollingDown
                    offset < previousOffset -> TopBarAlphaScrollingUp
                    else -> alpha
                }
                previous = current
            }
    }
    return alpha
}
