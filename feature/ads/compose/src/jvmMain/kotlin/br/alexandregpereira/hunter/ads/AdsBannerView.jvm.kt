package br.alexandregpereira.hunter.ads

import androidx.compose.runtime.Composable

@Composable
internal actual fun AdsBannerView(
    onAdLoaded: () -> Unit,
    onAdImpression: () -> Unit,
    onAdFailedToLoad: (errorCode: Int?, errorMessage: String?) -> Unit,
) {
    // There is no ad SDK on JVM/Desktop. The ads consent is never granted there, so the promo
    // banner always owns the slot and this composable is never reached.
}
