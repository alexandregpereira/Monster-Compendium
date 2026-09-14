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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compendium.compendiumHeaderPaddingTop
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.AppDropdownButton

private val SortButtonPaddingBottom = 8.dp

/**
 * Fixed height, so the list content padding does not change when the button is hidden.
 */
internal val MonsterCompendiumSortButtonHeight: Dp =
    AppButtonSize.VERY_SMALL.height.dp + SortButtonPaddingBottom

/**
 * Fades out when the first section title text (the header) reaches the bottom of the button,
 * so it only hides once the title is going behind it.
 *
 * The first section title item starts right below the button (at [MonsterCompendiumSortButtonHeight]),
 * and its text starts [compendiumHeaderPaddingTop] below the item top. The button bottom is
 * [SortButtonPaddingBottom] above the item top, so the text touches it after scrolling the sum of both.
 */
@Composable
internal fun MonsterCompendiumSortButton(
    sortTitle: String,
    sortLabel: String,
    sortOptions: List<String>,
    sortOptionsOpened: Boolean,
    listState: LazyGridState,
    modifier: Modifier = Modifier,
    onSortClick: () -> Unit = {},
    onSortOptionsClose: () -> Unit = {},
    onSortOptionSelected: (index: Int) -> Unit = {},
) {
    val hideScrollOffset = with(LocalDensity.current) {
        (SortButtonPaddingBottom + compendiumHeaderPaddingTop).roundToPx()
    }
    val isVisible by remember(listState, hideScrollOffset) {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 &&
                listState.firstVisibleItemScrollOffset < hideScrollOffset
        }
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
    ) {
        AppDropdownButton(
            text = sortLabel,
            options = sortOptions,
            expanded = sortOptionsOpened,
            title = sortTitle,
            leadingIcon = Icons.AutoMirrored.Filled.Sort,
            leadingIconContentDescription = sortTitle,
            size = AppButtonSize.VERY_SMALL,
            onClick = onSortClick,
            onDismiss = onSortOptionsClose,
            onOptionSelected = onSortOptionSelected,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                bottom = SortButtonPaddingBottom,
            ),
        )
    }
}
