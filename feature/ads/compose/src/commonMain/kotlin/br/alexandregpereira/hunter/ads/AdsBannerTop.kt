package br.alexandregpereira.hunter.ads

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.LocalScreenSize
import org.koin.compose.koinInject

@Composable
fun AdsBannerTop(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val stateHolder = koinInject<AdsStateHolder>()
    val state by stateHolder.state.collectAsState()

    LaunchedEffect(stateHolder) {
        stateHolder.onStart()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        val isBannerVisible = state.isVisible
        AnimatedVisibility(
            visible = isBannerVisible,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.fillMaxWidth().weight(.1f),
        ) {
            AdsBannerView()
        }
        val screenSize = LocalScreenSize.current
        val newScreenSize = remember(isBannerVisible, screenSize) {
            val percentage = if (state.isVisible) 0.9f else 1f
            screenSize.copy(
                heightInDp = screenSize.heightInDp * percentage,
                heightInPixels = (screenSize.heightInPixels * percentage).toInt(),
            )
        }

        CompositionLocalProvider(
            LocalScreenSize provides newScreenSize,
        ) {
            Box(modifier = Modifier.weight(.9f)) {
                content()
            }
        }
    }
}

@Composable
internal expect fun AdsBannerView()
