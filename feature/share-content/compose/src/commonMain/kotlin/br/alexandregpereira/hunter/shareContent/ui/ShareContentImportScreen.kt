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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.file.rememberCompendiumFilePickerLauncher
import br.alexandregpereira.hunter.shareContent.state.ShareContentImportExtractedState
import br.alexandregpereira.hunter.shareContent.state.ShareContentImportState
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.ScreenHeader

@Composable
internal fun ShareContentImportScreen(
    state: ShareContentImportState,
    onImport: () -> Unit,
    onFilePicked: (fileName: String, bytes: ByteArray) -> Unit,
) = Column {
    Spacer(modifier = Modifier.height(16.dp))

    ScreenHeader(title = state.strings.importTitle)

    Spacer(modifier = Modifier.height(16.dp))

    val launcher = rememberCompendiumFilePickerLauncher { file ->
        onFilePicked(file.name,file.content)
    }

    LoadingScreen(
        isLoading = state.isLoading,
        fillMaxSize = false,
    ) {
        Column {
            state.importExtractedState?.let {
                ShareContentImportExtractedScreen(state = it, contentToImport = state.contentToImport)
            } ?: ShareContentImportFilePickerScreen(
                importErrorMessage = state.importErrorMessage,
                onFilePicked = onFilePicked,
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = strings.importButton,
                onClick = onImport,
            )
        }
    }
}

@Composable
private fun ShareContentImportFilePickerScreen(
    importErrorMessage: String?,
    onFilePicked: (fileName: String, bytes: ByteArray) -> Unit,
) = Column {
    val launcher = rememberCompendiumFilePickerLauncher { file ->
        onFilePicked(file.name,file.content)
    }
    AppButton(
        text = strings.importButton,
        isPrimary = false,
        size = AppButtonSize.SMALL,
        onClick = { launcher.launch() },
    )

    Spacer(modifier = Modifier.height(16.dp))

    importErrorMessage?.takeIf { it.isNotBlank() }?.let { errorMessage ->
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = errorMessage,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun ShareContentImportExtractedScreen(
    contentToImport: String,
    state: ShareContentImportExtractedState,
) = Column {
    Text(
        text = contentToImport,
    )
    Text(
        text = state.monsterQuantity,
    )
    Text(
        text = state.monsterLoreQuantity,
    )
    Text(
        text = state.spellQuantity,
    )
    Text(
        text = state.contentSize,
    )
}
