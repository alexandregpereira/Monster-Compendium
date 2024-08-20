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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.Window
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun SpellDescription(
    description: String,
    modifier: Modifier = Modifier,
    higherLevel: String? = null
) = Column(modifier) {
    Text(
        text = description,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
    )
    higherLevel?.let {
        Text(
            buildAnnotatedString {

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                    )
                ) {
                    append("${strings.atHigherLevels} ")
                }

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Light
                    )
                ) {
                    append(it)
                }

            },
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview
@Composable
private fun SpellDescriptionPreview() = Window {
    SpellDescription(
        description = "asdadsas ads asda sd asd asdasdasd asd asd asd asd asd asd as dassd asdasdasa" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                "asd asdasdasd asdasdasdasdasdasasdasdasdasdasdasdasdasdasd",
        higherLevel = "asdadsas ads asda sd asd asdasdasd asd asd asd asd asd asd as dassd asdasdasa" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd" +
                " dasdasdasdas ads asdasdasdas dasasdasdasdasdasdasdas dasdasd asd asdasdasdasd"
    )
}
