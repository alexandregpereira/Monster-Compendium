package br.alexandregpereira.hunter.ui.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CircularLoading(
    isLoading: Boolean,
    content: @Composable () -> Unit
) {
    Crossfade(targetState = isLoading) {
        if (it) {
            CircularLoading()
        } else {
            content()
        }
    }
}

@Composable
fun CircularLoading() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    CircularProgressIndicator(
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier.size(40.dp)
    )
}
