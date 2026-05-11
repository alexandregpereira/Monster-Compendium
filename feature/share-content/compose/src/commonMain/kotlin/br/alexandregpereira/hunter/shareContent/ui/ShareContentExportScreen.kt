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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.shareContent.state.ShareContentExportState
import br.alexandregpereira.hunter.shareContent.state.ShareContentExtractedState
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.LoadingScreenState
import br.alexandregpereira.hunter.ui.compose.ScreenHeader

@Composable
internal fun ShareContentExportBottomSheet(
    state: ShareContentExportState,
    onClose: () -> Unit,
    onExportToFile: () -> Unit,
    onEditContentTitle: (String) -> Unit,
    onEditContentDescription: (String) -> Unit,
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
            exportButtonEnabled = state.exportButtonEnabled,
            onExportToFile = onExportToFile,
            onEditContentTitle = onEditContentTitle,
            onEditContentDescription = onEditContentDescription,
        )
    }
}

@Composable
internal fun ShareContentExportScreen(
    isLoading: Boolean,
    exportError: Boolean,
    exportExtractedState: ShareContentExtractedState?,
    exportButtonEnabled: Boolean,
    onExportToFile: () -> Unit,
    onEditContentTitle: (String) -> Unit,
    onEditContentDescription: (String) -> Unit,
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
            Text(
                text = exportStrings.exportErrorTitle,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
        }
    ) { extractedState ->
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ShareContentExtracted(
                state = extractedState,
                strings = exportStrings.extractedStrings,
                onContentTitleChange = onEditContentTitle,
                onContentDescriptionChange = onEditContentDescription,
            )

            Spacer(modifier = Modifier.height(8.dp))

            AppButton(
                text = exportStrings.shareButton,
                enabled = exportButtonEnabled,
                onClick = onExportToFile,
            )
        }
    }
}
