package br.alexandregpereira.hunter.paywall.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.paywall.PaywallActionResultState
import br.alexandregpereira.hunter.paywall.PaywallFeatureState
import br.alexandregpereira.hunter.paywall.PaywallState
import br.alexandregpereira.hunter.ui.compose.AppScreen
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.LoadingScreenState
import br.alexandregpereira.hunter.ui.compose.LocalScreenSize
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.ScreenSizeType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun PaywallScreen(
    state: PaywallState,
    screenSizeType: ScreenSizeType = LocalScreenSize.current.type,
    onClose: () -> Unit,
    subscribe: () -> Unit,
    restore: () -> Unit,
    onTryAgainLoadOffer: () -> Unit = {},
    onTryAgainPurchase: () -> Unit = {},
    onComeBackToOffer: () -> Unit = {},
) = AppScreen(
    isOpen = state.isOpen,
    swipeTriggerPercentage = 0.4f,
    showCloseButton = false,
    onClose = onClose,
) {
    val isLoading = state.isLoading
    val errorState = state.actionResultState
    val loadingScreenState = remember(isLoading, errorState) {
        when {
            isLoading -> LoadingScreenState.LoadingScreen
            errorState != null -> LoadingScreenState.Error(errorState)
            else -> LoadingScreenState.Success(Unit)
        }
    }
    LoadingScreen<Unit, PaywallActionResultState>(
        state = loadingScreenState,
        fillMaxSize = false,
        errorContent = { errorState ->
            PaywallActionResultScreen(
                errorState = errorState,
                onTryAgainLoadOffer = onTryAgainLoadOffer,
                onTryAgainPurchase = onTryAgainPurchase,
                onComeBackToOffer = onComeBackToOffer,
                onCloseClick = onClose,
            )
        }
    ) {
        if (state.actionResultState == null) {
            when (screenSizeType) {
                ScreenSizeType.Portrait -> PaywallScreenContent(
                    features = state.features,
                    subscriptionOfferFormatted = state.subscriptionOfferFormatted,
                    subscribe = subscribe,
                    restore = restore,
                )

                ScreenSizeType.LandscapeCompact,
                ScreenSizeType.LandscapeExpanded -> PaywallLandscapeScreenContent(
                    features = state.features,
                    subscriptionOfferFormatted = state.subscriptionOfferFormatted,
                    subscribe = subscribe,
                    restore = restore,
                )
            }
        }
    }
}

@Composable
internal fun PaywallScreenContent(
    features: ImmutableList<PaywallFeatureState>,
    subscriptionOfferFormatted: String = "",
    modifier: Modifier = Modifier,
    subscribe: () -> Unit,
    restore: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PaywallScrollableContent(
            modifier = Modifier,
            features = features,
        )

        Spacer(modifier = Modifier.height(8.dp))

        PaywallFooter(
            subscriptionOfferFormatted = subscriptionOfferFormatted,
            subscribe = subscribe,
            restore = restore,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
}

@Composable
internal fun PaywallLandscapeScreenContent(
    features: ImmutableList<PaywallFeatureState>,
    subscriptionOfferFormatted: String,
    modifier: Modifier = Modifier,
    subscribe: () -> Unit,
    restore: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth().padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PaywallScrollableContent(
            modifier = Modifier.weight(.6f),
            features = features,
        )
        PaywallFooter(
            modifier = Modifier.weight(.4f),
            subscriptionOfferFormatted = subscriptionOfferFormatted,
            subscribe = subscribe,
            restore = restore,
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun PaywallScreenPreview() = PreviewWindow {
    var isLoading by remember { mutableStateOf(false) }
    PaywallScreen(
        state = PaywallState(
            isOpen = true,
            isLoading = isLoading,
            features = persistentListOf(
                PaywallFeatureState(
                    name = "Feature 1",
                    isPremium = true,
                ),
                PaywallFeatureState(
                    name = "Feature 2",
                    isPremium = false,
                ),
                PaywallFeatureState(
                    name = "Feature 3",
                    isPremium = false,
                ),
            ),
            subscriptionOfferFormatted = "$9.99 / month",
        ),
        screenSizeType = ScreenSizeType.Portrait,
        onClose = { isLoading = false },
        subscribe = { isLoading = true},
        restore = {},
    )
}

@Preview(
    showBackground = true,
    widthDp = 700,
    heightDp = 400,
)
@Composable
private fun PaywallLandscapeScreenPreview() = PreviewWindow {
    PaywallScreen(
        state = PaywallState(
            isOpen = true,
            features = persistentListOf(
                PaywallFeatureState(
                    name = "Feature 1",
                    isPremium = true,
                ),
                PaywallFeatureState(
                    name = "Feature 2",
                    isPremium = false,
                ),
                PaywallFeatureState(
                    name = "Feature 3",
                    isPremium = false,
                ),
            ),
            subscriptionOfferFormatted = "$9.99 / month",
        ),
        screenSizeType = ScreenSizeType.LandscapeExpanded,
        onClose = {},
        subscribe = {},
        restore = {},
    )
}
