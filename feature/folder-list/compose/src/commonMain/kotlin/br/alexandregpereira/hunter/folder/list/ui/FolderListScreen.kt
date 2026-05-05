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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.folder.list.FolderListState
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessage
import br.alexandregpereira.hunter.ui.compose.SectionTitle

@Composable
internal fun FolderListScreen(
    state: FolderListState,
    contentPadding: PaddingValues = PaddingValues(),
    onCLick: (String) -> Unit = {},
    onLongCLick: (String) -> Unit = {},
    onItemSelectionClose: () -> Unit = {},
    onItemSelectionDeleteClick: () -> Unit = {},
    onItemSelectionAddToPreviewClick: () -> Unit = {},
    onScrollChanges: (Int, Int) -> Unit = { _, _ -> },
) {
    Box(Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = state.folders.isEmpty(),
            label = "FolderList",
        ) { isFoldersEmpty ->
            if (isFoldersEmpty) {
                FolderListEmptyScreen(
                    pageTitle = state.strings.title,
                    emptyScreenTitle = state.strings.emptyScreenTitle,
                    emptyScreenDescription = state.strings.emptyScreenDescription,
                )
            } else {
                FolderCardGrid(
                    folders = state.folders,
                    title = state.strings.title,
                    initialFirstVisibleItemIndex = remember { state.firstVisibleItemIndex },
                    initialFirstVisibleItemScrollOffset = remember { state.firstVisibleItemScrollOffset },
                    contentPadding = contentPadding,
                    onCLick = onCLick,
                    onLongCLick = onLongCLick,
                    onScrollChanges = onScrollChanges,
                )
            }
        }

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
    pageTitle: String,
    emptyScreenTitle: String,
    emptyScreenDescription: String,
) = Column(
    modifier = Modifier.padding(16.dp),
) {
    SectionTitle(title = pageTitle, isHeader = true)

    EmptyScreenMessage(
        title = emptyScreenTitle,
        description = emptyScreenDescription,
    )
}
