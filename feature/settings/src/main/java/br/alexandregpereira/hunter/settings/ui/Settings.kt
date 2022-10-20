/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.settings.R
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun Settings(
    imageBaseUrl: String,
    alternativeSourceBaseUrl: String,
    modifier: Modifier = Modifier,
    saveButtonEnabled: Boolean = false,
    onImageBaseUrlChange: (String) -> Unit = {},
    onAlternativeSourceBaseUrlChange: (String) -> Unit = {},
    onSaveButtonClick: () -> Unit = {}
) = Column(modifier.padding(16.dp)) {

    Text(
        text = stringResource(R.string.settings_additional_content),
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    Text(
        text = stringResource(R.string.settings_monster_images_json),
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
        text = stringResource(R.string.settings_alternative_sources_json),
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
        text = stringResource(R.string.settings_sync),
        enabled = saveButtonEnabled,
        onClick = onSaveButtonClick,
        modifier = Modifier.padding(top = 40.dp)
    )

    Text(
        text = stringResource(R.string.settings_sync_description),
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
    )
}

@Preview
@Composable
private fun SettingsPreview() = Window {
    Settings(
        imageBaseUrl = "",
        alternativeSourceBaseUrl = ""
    )
}
