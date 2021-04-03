package br.alexandregpereira.hunter.ui.util

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color

fun String?.toColor(): Color {
    return Color(
        this
            .runCatching { parseColor(this) }
            .getOrNull() ?: 0
    )
}
