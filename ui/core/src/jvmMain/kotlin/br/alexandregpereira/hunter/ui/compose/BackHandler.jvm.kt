package br.alexandregpereira.hunter.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalBackDispatcher = compositionLocalOf {
    BackDispatcher { println("LocalBackDispatcher not provided") }
}

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    if (!enabled) return
    LocalBackDispatcher.current.onBackPressed(onBack)
}

fun interface BackDispatcher {
    fun onBackPressed(block: () -> Unit)
}
