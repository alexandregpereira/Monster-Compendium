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

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.shareContent.state.ShareContentImportState
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.ScreenHeader

@Composable
internal fun ShareContentImportBottomSheet(
    isOpen: Boolean,
    state: ShareContentImportState,
    onImport: () -> Unit,
    onFilePickClick: () -> Unit,
    onClose: () -> Unit,
) = BottomSheet(
    contentPadding = PaddingValues(
        end = 16.dp,
        start = 16.dp,
        bottom = 16.dp,
    ),
    topSpaceHeight = 0.dp,
    opened = isOpen,
    onClose = onClose,
    modifier = Modifier.animateContentSize()
) {
    CompositionLocalProvider(LocalImportStrings provides state.strings) {
        ShareContentImportScreen(
            state = state,
            onImport = onImport,
            onFilePickClick = onFilePickClick,
        )
    }
}

@Composable
internal fun ShareContentImportScreen(
    state: ShareContentImportState,
    onImport: () -> Unit,
    onFilePickClick: () -> Unit,
) = Column {
    Spacer(modifier = Modifier.height(16.dp))

    ScreenHeader(title = state.strings.importTitle)

    Spacer(modifier = Modifier.height(16.dp))

    LoadingScreen(
        isLoading = state.isLoading,
        fillMaxSize = false,
    ) {
        Column {
            if (state.importExtractedState != null) {
                ShareContentExtracted(
                    state = state.importExtractedState,
                    strings = importStrings.extractedStrings,
                )

                Spacer(modifier = Modifier.height(32.dp))

                AppButton(
                    text = importStrings.importButton,
                    onClick = onImport,
                )
            } else {
                ShareContentImportFilePickerScreen(
                    importErrorMessage = state.importErrorMessage,
                )

                Spacer(modifier = Modifier.height(32.dp))

                AppButton(
                    text = importStrings.pickCompendiumFile,
                    onClick = onFilePickClick,
                )
            }
        }
    }
}

@Composable
private fun ShareContentImportFilePickerScreen(
    importErrorMessage: String?,
) = Column {
    importErrorMessage?.takeIf { it.isNotBlank() }?.let { errorMessage ->
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = errorMessage,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
    }
}
