package br.alexandregpereira.hunter.ui.compose

import androidx.compose.runtime.Composable

@Composable
fun BackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
) {
    if (!enabled) return
    BackPlatformHandler(enabled = enabled, onBack = onBack)
}

@Composable
expect fun BackPlatformHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
)
