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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
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
    contentScale: AppImageContentScale,
    isHorizontal: Boolean = false,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit = {},
    onLongCLick: () -> Unit = {},
) = ImageCard(
    name = name,
    isHorizontal = isHorizontal,
    modifier = modifier,
    onCLick = onCLick,
    onLongCLick = onLongCLick
) {
    MonsterImage(
        url = url,
        icon = icon,
        backgroundColor = backgroundColor,
        challengeRating = challengeRating,
        contentScale = contentScale,
    )
}

@Composable
fun ImageCard(
    name: String,
    isHorizontal: Boolean = false,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 18.sp,
    onCLick: () -> Unit = {},
    onLongCLick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val shape = imageCardShape
    val alpha = 0.7f
    val borderColor = MaterialTheme.colors.surface
    AppCard(
        backgroundColor = Color.Transparent,
        contentColor = MaterialTheme.colors.onSurface,
        elevation = imageCardElevation,
        shape = shape,
        modifier = modifier.animatePressed(
            onClick = onCLick,
            onLongClick = onLongCLick
        ).monsterAspectRatio(isHorizontal)
    ) {
        Box(Modifier.fillMaxSize()) {
            content()

            val colorStops = arrayOf(
                .0f to Color.Transparent,
                .8f to borderColor.copy(alpha = alpha),
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
                    fontSize = fontSize,
                    modifier = Modifier.padding(horizontal = 12.dp)
                        .padding(bottom = 12.dp, top = 24.dp)
                        .align(Alignment.BottomStart)
                )
            }
        }
    }
}

val imageCardShape = Shapes.large
val imageCardElevation = 2.dp

@Preview
@Composable
private fun MonsterCardPreview() {
    HunterTheme {
        MonsterCard(
            name = "Monster of the Monsters",
            url = "asdasdas",
            backgroundColor = "#ffe3ee",
            challengeRating = "18",
            icon = Res.drawable.ic_aberration,
            contentScale = AppImageContentScale.Crop,
        )
    }
}
