package br.alexandregpereira.hunter.revenue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import br.alexandregpereira.hunter.revenue.ui.LocalStrings
import br.alexandregpereira.hunter.revenue.ui.PaywallScreen
import org.koin.compose.koinInject

@Composable
fun PaywallFeature() {
    val stateHolder = koinInject<PaywallStateHolder>()
    val state by stateHolder.state.collectAsState()

    LaunchedEffect(Unit) {
        stateHolder.onStart()
    }

    CompositionLocalProvider(LocalStrings provides state.strings) {
        PaywallScreen(
            state = state,
            onClose = stateHolder::onClose,
            subscribe = stateHolder::onSubscribe,
            restore = stateHolder::onRestoreSubscription,
            onTryAgainLoadOffer = stateHolder::onTryAgainLoadOffer,
        )
    }
}
