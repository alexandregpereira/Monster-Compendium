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

package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.settings.SettingsEnStrings
import br.alexandregpereira.hunter.settings.SettingsStrings
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.Window
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun AdvancedSettings(
    imageBaseUrl: String,
    alternativeSourceBaseUrl: String,
    modifier: Modifier = Modifier,
    saveButtonEnabled: Boolean = false,
    strings: SettingsStrings,
    onImageBaseUrlChange: (String) -> Unit = {},
    onAlternativeSourceBaseUrlChange: (String) -> Unit = {},
    onSaveButtonClick: () -> Unit = {}
) = Column(
    modifier
        .padding(16.dp)
) {

    Text(
        text = strings.additionalContent,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    Text(
        text = strings.monsterImagesJson,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        modifier = Modifier
    )

    SettingsTextField(
        text = imageBaseUrl,
        onValueChange = onImageBaseUrlChange,
        modifier = Modifier.padding(top = 4.dp)
    )

    Text(
        text = strings.alternativeSourcesJson,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        modifier = Modifier.padding(top = 16.dp)
    )

    SettingsTextField(
        text = alternativeSourceBaseUrl,
        onValueChange = onAlternativeSourceBaseUrlChange,
        modifier = Modifier.padding(top = 4.dp)
    )

    AppButton(
        text = strings.sync,
        enabled = saveButtonEnabled,
        onClick = onSaveButtonClick,
        modifier = Modifier.padding(top = 40.dp)
    )
}

@Preview
@Composable
private fun AdvancedSettingsPreview() = Window {
    AdvancedSettings(
        imageBaseUrl = "",
        alternativeSourceBaseUrl = "",
        strings = SettingsEnStrings(),
    )
}
