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
import br.alexandregpereira.hunter.ui.theme.HunterTheme
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
) = HunterTheme {
    BottomSheet(opened = state.isOpen, contentPadding = contentPadding, onClose = onClose) {
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
            text = state.strings.save,
            modifier = Modifier.padding(16.dp),
            onClick = onSave
        )
    }
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
