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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.shareContent.state.ShareContentState
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.ScreenHeader

@Composable
internal fun ShareContentExportScreen(
    state: ShareContentState,
    onCopy: () -> Unit,
) {
    Spacer(modifier = Modifier.height(16.dp))

    ScreenHeader(title = state.strings.exportTitle)

    Spacer(modifier = Modifier.height(16.dp))

    AppTextField(
        text = state.contentToExportShort,
        label = state.strings.contentToImportLabel,
        enabled = false,
    )

    Spacer(modifier = Modifier.height(32.dp))

    AppButton(
        text = state.exportCopyButtonText,
        enabled = state.exportCopyButtonEnabled,
        onClick = onCopy,
    )
}
