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

package br.alexandregpereira.hunter.folder.list.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.folder.list.FolderListState
import br.alexandregpereira.hunter.ui.compose.AppTopBar
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessage

@Composable
internal fun FolderListScreen(
    state: FolderListState,
    contentPadding: PaddingValues = PaddingValues(),
    onCloseClick: () -> Unit = {},
    onCLick: (String) -> Unit = {},
    onLongCLick: (String) -> Unit = {},
    onItemSelectionClose: () -> Unit = {},
    onItemSelectionDeleteClick: () -> Unit = {},
    onItemSelectionAddToPreviewClick: () -> Unit = {},
    onScrollChanges: (Int, Int) -> Unit = { _, _ -> },
) {
    val density = LocalDensity.current
    val listState = rememberLazyGridState(
        initialFirstVisibleItemIndex = state.firstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = state.firstVisibleItemScrollOffset,
    )
    var topBarHeight by remember { mutableStateOf(0.dp) }
    Box(Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = state.folders.isEmpty(),
            label = "FolderList",
        ) { isFoldersEmpty ->
            if (isFoldersEmpty) {
                FolderListEmptyScreen(
                    emptyScreenTitle = state.strings.emptyScreenTitle,
                    emptyScreenDescription = state.strings.emptyScreenDescription,
                    modifier = Modifier.padding(top = topBarHeight),
                )
            } else {
                FolderCardGrid(
                    folders = state.folders,
                    listState = listState,
                    contentPadding = PaddingValues(
                        top = topBarHeight,
                        bottom = contentPadding.calculateBottomPadding(),
                    ),
                    onCLick = onCLick,
                    onLongCLick = onLongCLick,
                    onScrollChanges = onScrollChanges,
                )
            }
        }

        AppTopBar(
            title = state.strings.title,
            listState = listState,
            onCloseClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .onSizeChanged { size ->
                    topBarHeight = with(density) { size.height.toDp() }
                }
                .padding(top = contentPadding.calculateTopPadding()),
        )

        ItemSelection(
            itemSelectionText = state.strings.itemSelected(state.itemSelectionCount),
            deleteText = state.strings.delete,
            addToPreviewText = state.strings.addToPreview,
            contentBottomPadding = contentPadding.calculateBottomPadding(),
            onClose = onItemSelectionClose,
            onDeleteClick = onItemSelectionDeleteClick,
            onAddToPreviewClick = onItemSelectionAddToPreviewClick,
            isOpen = state.isItemSelectionOpen,
        )
    }
}

@Composable
private fun FolderListEmptyScreen(
    emptyScreenTitle: String,
    emptyScreenDescription: String,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier.padding(16.dp),
) {
    EmptyScreenMessage(
        title = emptyScreenTitle,
        description = emptyScreenDescription,
    )
}
