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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.resources.Res
import br.alexandregpereira.hunter.ui.resources.ic_aberration
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.theme.Shapes
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MonsterCard(
    name: String,
    url: String,
    icon: DrawableResource,
    backgroundColor: String,
    challengeRating: String,
    isHorizontal: Boolean = false,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit = {},
    onLongCLick: () -> Unit = {},
) {
    val shape = Shapes.large
    val alpha = 0.7f
    val borderColor = MaterialTheme.colors.surface
    Box(
        modifier.animatePressed(
            onClick = onCLick,
            onLongClick = onLongCLick
        ).monsterAspectRatio(isHorizontal).clip(shape)
            .border(4.dp, borderColor, shape = shape),
    ) {
        MonsterImage(
            url = url,
            icon = icon,
            backgroundColor = backgroundColor,
            challengeRating = challengeRating,
            borderColor = borderColor,
        )

        val colorStops = arrayOf(
            0.0f to Color.Transparent,
            0.3f to borderColor.copy(alpha = alpha),
            .8f to borderColor
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = colorStops
                    )
                )
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp).align(Alignment.BottomStart)
            )
        }
    }
}

@Preview
@Composable
private fun MonsterCardPreview() {
    HunterTheme {
        MonsterCard(
            name = "Monster of the Monsters",
            url = "asdasdas",
            backgroundColor = "#ffe3ee",
            challengeRating = "18",
            icon = Res.drawable.ic_aberration
        )
    }
}
