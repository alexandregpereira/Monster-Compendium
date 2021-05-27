/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun AbilityDescriptionBlock(
    title: String,
    abilityDescriptions: List<AbilityDescription>,
    modifier: Modifier = Modifier,
    content: @Composable (index: Int) -> Unit = {}
) = Block(title = title, modifier = modifier) {

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
) = Column(modifier = modifier) {

    Text(
        text = name,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 16.sp,
    )

    Text(
        text = description,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        modifier = Modifier.padding(top = 4.dp)
    )

    content()
}

@Preview
@Composable
fun AbilityDescriptionPreview() = Window {
    AbilityDescription(
        name = "Name",
        description = "Description very long to test the description field"
    )
}
