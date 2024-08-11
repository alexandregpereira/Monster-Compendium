package br.alexandregpereira.hunter.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getPlatformScreenSizeInfo(): ScreenSizeInfo {
    val density = LocalDensity.current
    val config = LocalWindowInfo.current.containerSize

    return ScreenSizeInfo(
        heightInPixels = config.height,
        widthInPixels = config.width,
        heightInDp = with(density) { config.height.toDp() },
        widthInDp = with(density) { config.width.toDp() }
    )
}
