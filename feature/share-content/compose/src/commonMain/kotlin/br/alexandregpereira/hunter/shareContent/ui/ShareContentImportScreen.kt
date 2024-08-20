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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.shareContent.state.ShareContentState
import br.alexandregpereira.hunter.shareContent.ui.resources.IconContentPaste
import br.alexandregpereira.hunter.shareContent.ui.resources.Res
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun ShareContentImportScreen(
    state: ShareContentState,
    onImport: () -> Unit,
    onPaste: (String) -> Unit,
) {
    Spacer(modifier = Modifier.height(16.dp))

    ScreenHeader(title = state.strings.importTitle)

    Spacer(modifier = Modifier.height(16.dp))

    AppTextField(
        text = state.contentToImportShort,
        label = state.strings.contentToImportLabel,
        onValueChange = onPaste,
        enabled = false,
        showClearIcon = true,
    )

    Spacer(modifier = Modifier.height(8.dp))

    val clipboardManager = LocalClipboardManager.current
    Row(
        verticalAlignment = CenterVertically,
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable {
            onPaste(clipboardManager.getText()?.text.orEmpty())
        }
    ) {
        Icon(
            painterResource(Res.drawable.IconContentPaste),
            contentDescription = state.strings.pasteContent,
            modifier = Modifier.padding(vertical = 8.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = state.strings.pasteContent,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }

    state.importErrorMessage.takeIf { it.isNotBlank() }?.let { errorMessage ->
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = errorMessage,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    AppButton(
        text = state.strings.importButton,
        onClick = onImport,
    )
}
