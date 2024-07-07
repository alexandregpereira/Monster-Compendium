/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.Window

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
