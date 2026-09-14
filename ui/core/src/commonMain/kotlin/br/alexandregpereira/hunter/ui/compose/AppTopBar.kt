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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * The bottom content is always less translucent than the top bar.
 */
private enum class AppTopBarScrollState(
    val topBarAlpha: Float,
    val bottomContentAlpha: Float,
) {
    AtTop(topBarAlpha = 1f, bottomContentAlpha = 1f),
    ScrollingDown(topBarAlpha = 0.7f, bottomContentAlpha = 0.85f),
    ScrollingUp(topBarAlpha = 0.9f, bottomContentAlpha = 0.95f),
}

/**
 * @param listState when provided, the top bar becomes translucent while the list is scrolled.
 * Supports [LazyGridState], [LazyStaggeredGridState], [LazyListState] and [ScrollState].
 * The background is drawn before [modifier], so a top padding set by the caller (e.g. the status
 * bar) is also covered by it.
 * @param titleContent replaces the [title] and [subtitle] when provided.
 * @param bottomContent receives the alpha its background should use to follow the top bar
 * translucency.
 */
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    listState: ScrollableState? = null,
    onBackClick: () -> Unit = {},
    navigationIcon: (@Composable () -> Unit)? = {
        AppTopBarIconButton(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick,
        )
    },
    titleContent: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    moreOptions: List<String> = emptyList(),
    moreOptionsContentDescription: String = "More options",
    backgroundColor: Color = MaterialTheme.colors.surface,
    onMoreOptionClick: (index: Int) -> Unit = {},
    bottomContent: (@Composable (backgroundAlpha: Float) -> Unit)? = null,
) {
    val scrollState = rememberAppTopBarScrollState(listState)
    val alpha by animateFloatAsState(
        targetValue = scrollState.topBarAlpha,
        label = "TopBarAlpha",
    )
    val bottomContentAlpha by animateFloatAsState(
        targetValue = scrollState.bottomContentAlpha,
        label = "TopBarBottomContentAlpha",
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(backgroundColor.copy(alpha = backgroundColor.alpha * alpha))
            }
            .then(modifier),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 56.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            navigationIcon?.invoke()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
            ) {
                if (titleContent != null) {
                    titleContent()
                } else {
                    AppTopBarTitle(title = title, subtitle = subtitle)
                }
            }
            actions()
            if (moreOptions.isNotEmpty()) {
                AppTopBarMoreOptions(
                    options = moreOptions,
                    contentDescription = moreOptionsContentDescription,
                    onOptionClick = onMoreOptionClick,
                )
            }
        }
        if (bottomContent != null) {
            AppTopBarBottomContent(
                getAlpha = { bottomContentAlpha },
                content = bottomContent,
            )
        }
    }
}

@Composable
fun AppTopBarIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .animatePressed(onClick = onClick, pressedScale = .8f),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun AppTopBarTitle(
    title: String?,
    subtitle: String?,
) {
    if (title != null) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
    if (subtitle != null) {
        Text(
            text = subtitle,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * Reads the alpha in its own scope, so the whole top bar is not recomposed while it animates.
 */
@Composable
private fun AppTopBarBottomContent(
    getAlpha: () -> Float,
    content: @Composable (backgroundAlpha: Float) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
    ) {
        content(getAlpha())
    }
}

@Composable
private fun AppTopBarMoreOptions(
    options: List<String>,
    contentDescription: String,
    onOptionClick: (index: Int) -> Unit,
) = Box {
    var expanded by remember { mutableStateOf(false) }
    AppTopBarIconButton(
        imageVector = Icons.Filled.MoreVert,
        contentDescription = contentDescription,
        onClick = { expanded = true },
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        options.forEachIndexed { index, option ->
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onOptionClick(index)
                }
            ) {
                Text(
                    text = option,
                    color = MaterialTheme.colors.onSurface,
                )
            }
        }
    }
}

@Composable
private fun rememberAppTopBarScrollState(listState: ScrollableState?): AppTopBarScrollState {
    val getScrollPosition = remember(listState) { listState?.scrollPositionProvider() }
    var scrollState by remember(getScrollPosition) {
        val isAtTop = getScrollPosition == null || getScrollPosition() == (0 to 0)
        mutableStateOf(if (isAtTop) AppTopBarScrollState.AtTop else AppTopBarScrollState.ScrollingUp)
    }
    LaunchedEffect(getScrollPosition) {
        if (getScrollPosition == null) return@LaunchedEffect
        var previous = getScrollPosition()
        snapshotFlow { getScrollPosition() }
            .collect { current ->
                val (index, offset) = current
                val (previousIndex, previousOffset) = previous
                scrollState = when {
                    index == 0 && offset == 0 -> AppTopBarScrollState.AtTop
                    index > previousIndex -> AppTopBarScrollState.ScrollingDown
                    index < previousIndex -> AppTopBarScrollState.ScrollingUp
                    offset > previousOffset -> AppTopBarScrollState.ScrollingDown
                    offset < previousOffset -> AppTopBarScrollState.ScrollingUp
                    else -> scrollState
                }
                previous = current
            }
    }
    return scrollState
}

/**
 * Returns the first visible item index and its scroll offset, or null when the state is not a
 * supported lazy layout state.
 */
private fun ScrollableState.scrollPositionProvider(): (() -> Pair<Int, Int>)? {
    return when (val state = this) {
        is LazyGridState -> ({ state.firstVisibleItemIndex to state.firstVisibleItemScrollOffset })
        is LazyStaggeredGridState -> ({ state.firstVisibleItemIndex to state.firstVisibleItemScrollOffset })
        is LazyListState -> ({ state.firstVisibleItemIndex to state.firstVisibleItemScrollOffset })
        is ScrollState -> ({ 0 to state.value })
        else -> null
    }
}

@Preview
@Composable
private fun AppTopBarPreview() = PreviewWindow {
    Column {
        AppTopBar(
            title = "Creatures",
            actions = {
                AppTopBarIconButton(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    onClick = {},
                )
            },
            bottomContent = { backgroundAlpha ->
                AppDropdownButton(
                    text = "Alphabetical",
                    options = listOf("Alphabetical"),
                    expanded = false,
                    size = AppButtonSize.VERY_SMALL,
                    backgroundAlpha = backgroundAlpha,
                )
            },
        )
        AppTopBar(
            title = "Title",
            subtitle = "Subtitle",
            moreOptions = listOf("Option 1", "Option 2"),
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}
