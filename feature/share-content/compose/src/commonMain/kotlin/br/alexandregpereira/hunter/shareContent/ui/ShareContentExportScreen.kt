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

package br.alexandregpereira.hunter.shareContent.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.shareContent.state.ShareContentExportState
import br.alexandregpereira.hunter.shareContent.state.ShareContentExtractedEntryState
import br.alexandregpereira.hunter.shareContent.state.ShareContentExtractedState
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessageContent
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.LoadingScreenState
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ShareContentExportBottomSheet(
    state: ShareContentExportState,
    onClose: () -> Unit,
    onExportToFile: () -> Unit,
) = BottomSheet(
    contentPadding = PaddingValues(
        end = 16.dp,
        start = 16.dp,
        bottom = 16.dp,
    ),
    topSpaceHeight = 0.dp,
    opened = state.isOpen,
    onClose = onClose,
) {
    CompositionLocalProvider(LocalExportStrings provides state.strings) {
        ShareContentExportScreen(
            isLoading = state.isLoading,
            exportError = state.exportError,
            exportExtractedState = state.exportExtractedState,
            onExportToFile = onExportToFile,
        )
    }
}

@Composable
internal fun ShareContentExportScreen(
    isLoading: Boolean,
    exportError: Boolean,
    exportExtractedState: ShareContentExtractedState?,
    onExportToFile: () -> Unit,
) = Column(
    modifier = Modifier.padding(top = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    ScreenHeader(title = exportStrings.exportTitle)

    val loadingState = remember(isLoading, exportError, exportExtractedState) {
        when {
            isLoading -> LoadingScreenState.LoadingScreen
            exportError -> LoadingScreenState.Error(Unit)
            exportExtractedState != null -> LoadingScreenState.Success(exportExtractedState)
            else -> LoadingScreenState.LoadingScreen
        }
    }

    LoadingScreen<ShareContentExtractedState, Unit>(
        state = loadingState,
        fillMaxSize = false,
        errorContent = {
            EmptyScreenMessageContent(title = exportStrings.exportErrorTitle)
        }
    ) { extractedState ->
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ShareContentExtracted(
                state = extractedState,
            )

            Spacer(modifier = Modifier.height(8.dp))

            AppButton(
                text = exportStrings.shareButton,
                onClick = onExportToFile,
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ShareContentExportBottomSheetPreview() = PreviewWindow {
    val state = ShareContentExportState(
        isOpen = true,
        exportExtractedState = ShareContentExtractedState(
            fileName = "File Name",
            isFileNameEditable = false,
            contentSize = "400 MB",
            contentEntries = persistentListOf(
                ShareContentExtractedEntryState(
                    quantity = "50 Monsters",
                    content = "Monster 1, Monster 2, Monster 3, Monster 4",
                    contentWarning = "This content will override the monster content",
                ),
                ShareContentExtractedEntryState(
                    quantity = "50 Lore entries",
                    content = "Monster 1, Monster 2, Monster 3",
                ),
                ShareContentExtractedEntryState(
                    quantity = "20 Spells",
                    enabled = false,
                    content = "Spell 1, Spell 2, Spell 3, Spell 5, Spell 65",
                    contentWarning = "This content will override the monster content",
                ),
                ShareContentExtractedEntryState(
                    quantity = "10 Local images",
                    content = "Image 1, Image 2, Image 3",
                ),
            ),
        )
    )
    ShareContentExportBottomSheet(
        state = state,
        onClose = {},
        onExportToFile = {},
    )
}
