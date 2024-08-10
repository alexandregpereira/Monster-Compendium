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

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.util.toColor
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.pow

@Composable
fun MonsterTypeIcon(
    icon: DrawableResource,
    iconSize: Dp,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    size: Dp = 48.dp,
    tint: Color = Color.Black,
    backgroundColor: Color = MaterialTheme.colors.surface,
) = CornerCircle(
    modifier = modifier,
    color = backgroundColor,
    size = size,
    direction = Direction.RIGHT,
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        tint = tint,
        modifier = Modifier
            .size(iconSize)
    )
}

fun String.getTintColor(): Color = this.toColor().getTintColor()

fun Color.getTintColor(): Color {
    val color = this
    val luminance = colorToLuminance(color.red, color.green, color.blue)
    return if (luminance > 0.5) Color.Black else Color.White
}

private fun colorToLuminance(red: Float, green: Float, blue: Float): Double {
    // Adjust colors based on luminance
    val rLum = if (red <= 0.03928f) red / 12.92f else ((red + 0.055f) / 1.055f).pow(2.4f)
    val gLum = if (green <= 0.03928f) green / 12.92f else ((green + 0.055f) / 1.055f).pow(2.4f)
    val bLum = if (blue <= 0.03928f) blue / 12.92f else ((blue + 0.055f) / 1.055f).pow(2.4f)

    // Apply the luminance formula
    return (0.2126 * rLum + 0.7152 * gLum + 0.0722 * bLum).toDouble()
}
