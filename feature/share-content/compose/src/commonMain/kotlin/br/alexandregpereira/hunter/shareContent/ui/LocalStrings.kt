package br.alexandregpereira.hunter.shareContent.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import br.alexandregpereira.hunter.shareContent.state.ShareContentExportStrings
import br.alexandregpereira.hunter.shareContent.state.ShareContentImportStrings

internal val LocalImportStrings = compositionLocalOf { ShareContentImportStrings() }
internal val LocalExportStrings = compositionLocalOf { ShareContentExportStrings() }

internal val importStrings: ShareContentImportStrings
    @Composable
    @ReadOnlyComposable
    get() = LocalImportStrings.current

internal val exportStrings: ShareContentExportStrings
    @Composable
    @ReadOnlyComposable
    get() = LocalExportStrings.current
