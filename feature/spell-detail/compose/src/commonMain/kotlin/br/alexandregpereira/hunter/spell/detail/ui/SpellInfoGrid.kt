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
