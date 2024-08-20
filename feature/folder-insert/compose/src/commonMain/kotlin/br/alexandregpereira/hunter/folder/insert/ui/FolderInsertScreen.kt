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

package br.alexandregpereira.hunter.folder.insert.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.folder.insert.FolderInsertState
import br.alexandregpereira.hunter.folder.insert.MonsterPreviewState
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun FolderInsertScreen(
    state: FolderInsertState,
    contentPadding: PaddingValues = PaddingValues(),
    onFolderNameFieldChange: (String) -> Unit = {},
    onFolderSelected: (String) -> Unit = {},
    onLongClick: (String) -> Unit = {},
    onSave: () -> Unit = {},
    onClose: () -> Unit = {},
    onShare: () -> Unit = {},
) = BottomSheet(
    opened = state.isOpen,
    contentPadding = contentPadding,
    topSpaceHeight = 0.dp,
    onClose = onClose
) {
    ScreenHeader(
        title = state.strings.addToFolder,
        modifier = Modifier.padding(16.dp)
    )

    MonsterPreviewRow(
        monsters = state.monsterPreviews,
        onLongClick = onLongClick,
        modifier = Modifier.padding(top = 8.dp)
    )

    AppTextField(
        text = state.folderName,
        label = state.strings.folderNameLabel,
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
        onValueChange = onFolderNameFieldChange
    )

    MonsterFolderGrid(
        folders = state.folders,
        onFolderSelected = onFolderSelected,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    AppButton(
        text = state.strings.share,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp).padding(horizontal = 16.dp),
        isPrimary = false,
        onClick = onShare
    )

    AppButton(
        text = state.strings.save,
        modifier = Modifier.padding(bottom = 16.dp, top = 8.dp).padding(horizontal = 16.dp),
        onClick = onSave
    )
}

@Preview
@Composable
private fun FolderInsertScreenPreview() {
    FolderInsertScreen(
        state = FolderInsertState(
            isOpen = true,
            monsterPreviews = (1..10).map { i ->
                MonsterPreviewState(
                    index = "index$i",
                    backgroundColorLight = "#e2e2e2",
                    backgroundColorDark = "#e2e2e2",
                )
            },
            folders = (1..15).map { i ->
                "Folder$i" to 10
            }
        )
    )
}
