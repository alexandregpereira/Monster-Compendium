package br.alexandregpereira.hunter.paywall

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import br.alexandregpereira.hunter.paywall.ui.LocalStrings
import br.alexandregpereira.hunter.paywall.ui.PaywallScreen
import br.alexandregpereira.hunter.ui.compose.LifecycleEventObserver
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun PaywallFeature() {
    val stateHolder = koinInject<PaywallStateHolder>()
    val state by stateHolder.state.collectAsState()

    val uriHandler = LocalUriHandler.current
    LaunchedEffect(stateHolder, uriHandler) {
        stateHolder.action.collectLatest { action ->
            when (action) {
                is PaywallViewAction.GoToUrl -> uriHandler.openUri(action.url)
            }
        }
    }

    LifecycleEventObserver(
        onStart = stateHolder::onStart,
        onResume = stateHolder::onResume,
    )

    CompositionLocalProvider(LocalStrings provides state.strings) {
        PaywallScreen(
            state = state,
            onClose = stateHolder::onClose,
            subscribe = stateHolder::onSubscribe,
            restore = stateHolder::onRestoreSubscription,
            onTryAgainLoadOffer = stateHolder::onTryAgainLoadOffer,
            onTryAgainPurchase = stateHolder::onTryAgainPurchase,
            onComeBackToOffer = stateHolder::onComeBackToOffer,
            onTerms = stateHolder::onTermsClick,
            onPrivacy = stateHolder::onPrivacyClick,
        )
    }
}
