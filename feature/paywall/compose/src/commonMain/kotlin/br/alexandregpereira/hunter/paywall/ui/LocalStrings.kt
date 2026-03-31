package br.alexandregpereira.hunter.paywall.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import br.alexandregpereira.hunter.paywall.PaywallStrings

internal val LocalStrings = compositionLocalOf {
    PaywallStrings()
}

internal val strings: PaywallStrings
    @Composable
    get() = LocalStrings.current
