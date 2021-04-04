package br.alexandregpereira.hunter.ui.util

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun String.toColor(): Color {
    return Color(this.toColorInt())
}
