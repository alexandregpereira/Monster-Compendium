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

package br.alexandregpereira.hunter.ui.util

import androidx.compose.ui.graphics.Color

fun String.toColor(): Color {
    val colorHEX = this
    if (colorHEX[0] != '#') return Color.Transparent
    var colorARGB = colorHEX.substring(1).toLong(16)
    if (colorHEX.length == 7) {
        colorARGB = colorARGB or 0x00000000ff000000
    } else if (colorHEX.length != 9) {
        return Color.Transparent
    }
    return Color(
        alpha = (colorARGB.shr(24) and 0xFF).toInt(),
        red = (colorARGB.shr(16) and 0xFF).toInt(),
        green = (colorARGB.shr(8) and 0xFF).toInt(),
        blue = (colorARGB.shr(0) and 0xFF).toInt(),
    )
}
