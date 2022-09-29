/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.search.R
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun SearchBar(
    text: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.search_search_label)) },
        maxLines = 1,
        shape = CircleShape,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colors.onSurface
        )
    )
}

@Preview
@Composable
private fun SearchBarPreview() = HunterTheme {
    Column(Modifier.padding(16.dp)) {
        SearchBar("")
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(
            text = "Test"
        )
    }
}
