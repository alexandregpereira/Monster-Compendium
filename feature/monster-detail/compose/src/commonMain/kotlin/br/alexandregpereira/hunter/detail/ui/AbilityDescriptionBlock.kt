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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.monster.detail.AbilityDescriptionState
import br.alexandregpereira.hunter.ui.compose.Window
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun AbilityDescriptionBlock(
    title: String,
    abilityDescriptions: List<AbilityDescriptionState>,
    modifier: Modifier = Modifier,
    content: @Composable (index: Int) -> Unit = {}
) = Block(title = title, contentHorizontalPadding = 0.dp, modifier = modifier) {

    abilityDescriptions.forEachIndexed { index, abilityDescription ->
        AbilityDescription(
            abilityDescription.name,
            abilityDescription.description,
            modifier = Modifier.padding(top = if (index != 0) 24.dp else 0.dp),
            content = {
                content(index)
            }
        )
    }
}

@Composable
fun AbilityDescription(
    name: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) = SelectionContainer(modifier = modifier) {

    Column {
        Text(
            text = name,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = description,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = 4.dp)
                .padding(horizontal = 16.dp)
        )

        DisableSelection {
            content()
        }
    }
}

@Preview
@Composable
private fun AbilityDescriptionPreview() = Window {
    AbilityDescription(
        name = "Name",
        description = "Description very long to test the description field"
    )
}
