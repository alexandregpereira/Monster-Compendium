package br.alexandregpereira.hunter.revenue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import br.alexandregpereira.hunter.revenue.ui.PaywallScreen
import org.koin.compose.koinInject

@Composable
fun PaywallFeature() {
    val stateHolder = koinInject<PaywallStateHolder>()
    val state by stateHolder.state.collectAsState()

    LaunchedEffect(Unit) {
        stateHolder.onStart()
    }

    PaywallScreen(
        isOpen = state.isOpen,
        onDismiss = stateHolder::onDismiss,
        onPurchaseCompleted = stateHolder::onPurchaseCompleted,
        onPurchaseError = stateHolder::onPurchaseError,
    )
}
