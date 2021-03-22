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

package br.alexandregpereira.hunter.monster.compendium.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.MonsterImage
import br.alexandregpereira.hunter.ui.compose.MonsterItemType
import br.alexandregpereira.hunter.ui.compose.animatePressed
import br.alexandregpereira.hunter.ui.compose.pressedGesture
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun MonsterCard(
    name: String,
    imageUrl: String,
    backgroundColor: String,
    contentDescription: String,
    challengeRating: Float,
    type: MonsterItemType,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(8.dp),
    onCLick: () -> Unit = {},
) {
    var pressed by remember { mutableStateOf(false) }
    val scale = animatePressed(pressed = pressed)

    Column(
        modifier
            .padding(paddingValues)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .pressedGesture(onTap = onCLick, onPressed = { pressed = it })
    ) {
        MonsterImage(
            imageUrl = imageUrl,
            backgroundColor = backgroundColor,
            contentDescription = contentDescription,
            challengeRating = challengeRating,
            type = type
        )

        Text(
            text = name,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Preview
@Composable
fun MonsterCardPreview() {
    HunterTheme {
        MonsterCard(
            "Monster of the Monsters",
            "https://raw.githubusercontent.com/alexandregpereira/dnd-monster-manual/main/images/aboleth.png",
            "#80e3efef",
            "any",
            22f,
            type = MonsterItemType.ABERRATION
        )
    }
}
