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

package br.alexandregpereira.hunter.folder.list.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.folder.list.FolderListState

@Composable
internal fun FolderListScreen(
    state: FolderListState,
    contentPadding: PaddingValues = PaddingValues(),
    onCLick: (String) -> Unit = {},
    onLongCLick: (String) -> Unit = {},
    onItemSelectionClose: () -> Unit = {},
    onItemSelectionDeleteClick: () -> Unit = {},
    onItemSelectionAddToPreviewClick: () -> Unit = {},
) {
    Box(Modifier.fillMaxSize()) {
        FolderCardGrid(
            folders = state.folders,
            title = state.strings.title,
            contentPadding = contentPadding,
            onCLick = onCLick,
            onLongCLick = onLongCLick
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
