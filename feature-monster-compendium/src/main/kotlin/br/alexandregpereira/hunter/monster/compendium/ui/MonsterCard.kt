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

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.theme.Shapes
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun MonsterCard(
    imageUrl: String,
    backgroundColor: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    CoilImage(
        data = imageUrl,
        contentDescription = contentDescription,
        fadeIn = true,
        modifier = modifier
            .size(width = 156.dp, height = 208.dp)
            .background(
                color = Color(backgroundColor.runCatching { parseColor(this) }.getOrNull() ?: 0),
                shape = Shapes.large
            )
    )
}

@Preview
@Composable
fun MonsterCardPreview() {
    HunterTheme {
        MonsterCard(
            "https://raw.githubusercontent.com/alexandregpereira/dnd-monster-manual/main/images/aboleth.png",
            "#80e3efef",
            "any"
        )
    }
}