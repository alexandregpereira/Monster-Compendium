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

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
        tint = remember(backgroundColor) { backgroundColor.getTintColor() },
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
