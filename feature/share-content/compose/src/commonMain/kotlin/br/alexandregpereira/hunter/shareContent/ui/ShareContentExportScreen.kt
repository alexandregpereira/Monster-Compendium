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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.ScreenHeader

@Composable
internal fun ShareContentExportScreen(
    contentToExportShort: String,
    exportCopyButtonText: String,
    exportCopyButtonEnabled: Boolean,
    onExport: () -> Unit,
    onExportToFile: () -> Unit,
) = Column {
    Spacer(modifier = Modifier.height(16.dp))

    ScreenHeader(title = strings.exportTitle)

    Spacer(modifier = Modifier.height(16.dp))

    AppTextField(
        text = contentToExportShort,
        label = strings.contentToImportLabel,
        enabled = false,
    )

    Spacer(modifier = Modifier.height(32.dp))

    AppButton(
        text = strings.shareButton,
        onClick = onExportToFile,
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ShareContentExportScreenPreview() = PreviewWindow {
    ShareContentExportScreen(
        contentToExportShort = "contentToExportShort",
        exportCopyButtonText = "Button Text",
        exportCopyButtonEnabled = true,
        onExport = {},
        onExportToFile = {},
    )
}
