package br.alexandregpereira.hunter.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.ScreenWidthOrHeightSizeType.Compact
import br.alexandregpereira.hunter.ui.compose.ScreenWidthOrHeightSizeType.Expanded
import br.alexandregpereira.hunter.ui.compose.ScreenWidthOrHeightSizeType.Medium

val LocalScreenSize = compositionLocalOf<ScreenSizeInfo> { error("No Screen Size Info provided") }

data class ScreenSizeInfo(
    val heightInPixels: Int,
    val widthInPixels: Int,
    val heightInDp: Dp,
    val widthInDp: Dp,
) {
    private val heightSizeType: ScreenWidthOrHeightSizeType = when {
        heightInDp < 480.dp -> Compact
        heightInDp < 900.dp -> Medium
        else -> Expanded
    }

    private val widthSizeType: ScreenWidthOrHeightSizeType = when {
        widthInDp < 600.dp -> Compact
        widthInDp < 840.dp -> Medium
        else -> Expanded
    }

    val type: ScreenSizeType = when {
        widthSizeType == Expanded -> ScreenSizeType.LandscapeExpanded
        widthSizeType == Medium && heightSizeType == Compact -> ScreenSizeType.LandscapeCompact
        else -> ScreenSizeType.Portrait
    }
}

enum class ScreenSizeType {
    Portrait,
    LandscapeCompact,
    LandscapeExpanded,
}

private enum class ScreenWidthOrHeightSizeType {
    Compact,
    Medium,
    Expanded,
}

@Composable
expect fun getPlatformScreenSizeInfo(): ScreenSizeInfo
