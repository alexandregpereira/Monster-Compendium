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

package br.alexandregpereira.hunter.ui.util

import androidx.compose.ui.graphics.Color

fun String.toColor(): Color {
    val colorHex = this.removePrefix("#")
    var colorARGB = colorHex.takeUnless { it.isBlank() }
        ?.toLongOrNull(16) ?: return Color.Transparent

    if (colorHex.length == 6) {
        colorARGB = colorARGB or 0x00000000ff000000
    } else if (colorHex.length != 8) {
        return Color.Transparent
    }

    return Color(
        alpha = (colorARGB.shr(24) and 0xFF).toInt(),
        red = (colorARGB.shr(16) and 0xFF).toInt(),
        green = (colorARGB.shr(8) and 0xFF).toInt(),
        blue = (colorARGB.shr(0) and 0xFF).toInt(),
    )
}
