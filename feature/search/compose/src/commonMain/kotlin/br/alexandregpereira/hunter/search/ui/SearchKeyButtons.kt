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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize

@Composable
internal fun SearchKeyButtons(
    searchKeys: List<SearchKeyState>,
    initialScrollOffset: Int,
    shouldShow: () -> Boolean,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    onScrollChanges: (Int) -> Unit,
) {
    val scrollState = rememberScrollState(initial = initialScrollOffset)
    onScrollChanges(
        getScrollPosition = { scrollState.value },
        onScrollChanges = onScrollChanges,
    )
    AnimatedVisibility(
        visible = shouldShow(),
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Row(
            modifier = modifier.horizontalScroll(
                state = scrollState,
            ).padding(horizontal = 16.dp),
        ) {
            searchKeys.forEachIndexed { index, searchKey ->
                SearchKeyButton(searchKey = searchKey, onClick = { onClick(index) })
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
private fun onScrollChanges(
    getScrollPosition: () -> Int,
    onScrollChanges: (Int) -> Unit,
) {
    onScrollChanges(getScrollPosition())
}

@Composable
internal fun SearchKeyButton(
    searchKey: SearchKeyState,
    onClick: () -> Unit,
) = AppButton(
    text = searchKey.keyWithSymbols,
    size = AppButtonSize.VERY_SMALL,
    onClick = onClick,
)
