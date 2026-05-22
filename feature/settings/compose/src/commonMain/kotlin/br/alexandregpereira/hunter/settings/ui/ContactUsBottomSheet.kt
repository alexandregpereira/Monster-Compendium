/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.settings.ContactUsInfoState
import br.alexandregpereira.hunter.settings.SettingsEnStrings
import br.alexandregpereira.hunter.settings.SettingsStrings
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.PreviewWindow

@Composable
internal fun ContactUsBottomSheet(
    opened: Boolean,
    strings: SettingsStrings,
    contactUsInfo: ContactUsInfoState = ContactUsInfoState(),
    contentPadding: PaddingValues = PaddingValues(),
    onSendEmailClick: () -> Unit = {},
    onClose: () -> Unit = {},
) = BottomSheet(
    opened = opened,
    onClose = onClose,
    contentPadding = contentPadding,
) {
    ContactUs(
        strings = strings,
        contactUsInfo = contactUsInfo,
        onSendEmailClick = onSendEmailClick,
    )
}

@Composable
private fun ContactUs(
    strings: SettingsStrings,
    modifier: Modifier = Modifier,
    contactUsInfo: ContactUsInfoState = ContactUsInfoState(),
    onSendEmailClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = strings.contactUsTitle,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = strings.contactUsDescription,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
        SelectionContainer {
            Text(
                text = strings.contact,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = strings.contactAppInfo,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.06f),
                        shape = MaterialTheme.shapes.small,
                    )
                    .padding(12.dp)
            ) {
                AppInfoRow(label = "Version", value = contactUsInfo.appVersion)
                AppInfoRow(label = "Platform", value = contactUsInfo.platform)
                if (contactUsInfo.deviceId.isNotBlank()) {
                    AppInfoRow(label = "Device ID", value = contactUsInfo.deviceId)
                }
            }
        }
        AppButton(
            text = strings.contactUsButton,
            onClick = onSendEmailClick,
            modifier = Modifier.padding(top = 32.dp),
        )
    }
}

@Composable
private fun AppInfoRow(label: String, value: String) {
    Text(
        text = "$label: $value",
        fontFamily = FontFamily.Monospace,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
    )
}

@Preview
@Composable
private fun ContactUsPreview() = PreviewWindow {
    ContactUs(
        strings = SettingsEnStrings(),
        contactUsInfo = ContactUsInfoState(
            appVersion = "1.0.0",
            platform = "Android",
            deviceId = "abc123",
        ),
    )
}
