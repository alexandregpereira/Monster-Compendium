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

package br.alexandregpereira.hunter.spell.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.Window
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun SpellInfoGrid(
    castingTime: String,
    range: String,
    components: String,
    duration: String,
    savingThrowType: String?,
    modifier: Modifier = Modifier
) = Column(modifier = modifier) {
    val topPadding = 8.dp

    SpellTextInfo(
        title = strings.castingTime,
        description = castingTime
    )

    SpellTextInfo(
        title = strings.range,
        description = range,
        modifier = Modifier.padding(top = topPadding)
    )

    SpellTextInfo(
        title = strings.components,
        description = components,
        modifier = Modifier.padding(top = topPadding)
    )

    SpellTextInfo(
        title = strings.duration,
        description = duration,
        modifier = Modifier.padding(top = topPadding)
    )

    savingThrowType?.let {
        SpellTextInfo(
            title = strings.saveType,
            description = it,
            modifier = Modifier.padding(top = topPadding)
        )
    }
}

@Composable
private fun SpellTextInfo(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) = Text(
    buildAnnotatedString {

        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
            )
        ) {
            append("$title ")
        }

        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Normal
            )
        ) {
            append(description)
        }

    },
    fontSize = 14.sp,
    modifier = modifier
)

@Preview
@Composable
private fun SpellGridPreview() = Window {
    SpellInfoGrid(
        castingTime = "1 Action",
        components = "Anything",
        duration = "Any",
        range = "30 Feet",
        savingThrowType = "Strength",
    )
}
