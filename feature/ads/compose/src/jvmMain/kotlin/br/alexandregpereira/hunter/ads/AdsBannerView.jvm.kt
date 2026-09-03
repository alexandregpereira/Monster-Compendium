package br.alexandregpereira.hunter.ads

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

@Composable
internal actual fun AdsBannerView(
    onAdLoaded: () -> Unit,
    onAdFailedToLoad: () -> Unit,
) {
    // There is no ad SDK on JVM/Desktop, so the promo banner takes the slot back.
    val currentOnAdFailedToLoad by rememberUpdatedState(onAdFailedToLoad)
    LaunchedEffect(Unit) { currentOnAdFailedToLoad() }
}
