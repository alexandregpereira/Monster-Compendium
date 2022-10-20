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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun SettingsTextField(
    text: String,
    modifier: Modifier = Modifier,
    description: String = "",
    onValueChange: (String) -> Unit = {}
) = Column(modifier) {
    AppTextField(
        text = text,
        onValueChange = onValueChange
    )

    description.takeIf { it.isNotBlank() }?.let {
        Text(
            text = it,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


@Preview
@Composable
private fun SettingsTextFieldPreview() = Window {
    Column {
        SettingsTextField(
            text = "",
        )
        SettingsTextField(
            text = "",
            description = "Something describing something",
            modifier = Modifier.padding(top = 16.dp)
        )
        SettingsTextField(
            text = "asda asdas adasasd as",
            description = "Something describing something",
            modifier = Modifier.padding(top = 16.dp)
        )
        SettingsTextField(
            text = "asda asdas adasasd as",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
