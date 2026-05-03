package br.alexandregpereira.hunter.shareContent.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import br.alexandregpereira.hunter.shareContent.state.ShareContentStrings

internal val LocalStrings = compositionLocalOf { ShareContentStrings() }

internal val strings: ShareContentStrings
    @Composable
    @ReadOnlyComposable
    get() = LocalStrings.current
