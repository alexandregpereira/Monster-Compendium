package br.alexandregpereira.hunter.revenue.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import br.alexandregpereira.hunter.revenue.PaywallFeatureState
import br.alexandregpereira.hunter.revenue.PaywallOfferState
import br.alexandregpereira.hunter.revenue.PaywallState
import br.alexandregpereira.hunter.ui.compose.AppScreen
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessageContent
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.LocalAppContentPadding
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
) = AppScreen(
    isOpen = state.isOpen,
    contentPaddingValues = LocalAppContentPadding.current,
    swipeTriggerPercentage = 0.4f,
    onClose = onClose,
) {
    LoadingScreen(isLoading = state.isLoading, fillMaxSize = false) {
        when (state.offer) {
            is PaywallOfferState.Success -> {
                when (screenSizeType) {
                    ScreenSizeType.Portrait -> PaywallScreenContent(
                        modifier = Modifier.padding(bottom = 16.dp),
                        features = state.features,
                        offer = state.offer,
                        subscribe = subscribe,
                        restore = restore,
                    )
                    ScreenSizeType.LandscapeCompact,
                    ScreenSizeType.LandscapeExpanded -> PaywallLandscapeScreenContent(
                        modifier = Modifier,
                        features = state.features,
                        offer = state.offer,
                        subscribe = subscribe,
                        restore = restore,
                    )
                }
            }
            is PaywallOfferState.UnknownError -> {
                EmptyScreenMessageContent(
                    title = strings.offerLoadErrorTitle,
                    description = strings.offerLoadErrorDescription,
                    buttonText = strings.offerLoadErrorTryAgain,
                    onButtonClick = onTryAgainLoadOffer,
                    modifier = Modifier.padding(horizontal = 32.dp),
                )
            }
        }
    }
}

@Composable
internal fun PaywallScreenContent(
    features: ImmutableList<PaywallFeatureState>,
    offer: PaywallOfferState.Success,
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

        PaywallFooter(
            offer = offer,
            subscribe = subscribe,
            restore = restore,
        )
    }
}

@Composable
internal fun PaywallLandscapeScreenContent(
    features: ImmutableList<PaywallFeatureState>,
    offer: PaywallOfferState.Success,
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
            offer = offer,
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
            offer = PaywallOfferState.Success(
                subscriptionOfferFormatted = "$9.99 / month",
            ),
        ),
        screenSizeType = ScreenSizeType.Portrait,
        onClose = { isLoading = false },
        subscribe = { isLoading = true},
        restore = {},
    )
}

@Preview(
    showBackground = true,
)
@Composable
private fun PaywallScreenErrorPreview() = PreviewWindow {
    PaywallScreen(
        state = PaywallState(
            isOpen = true,
            features = persistentListOf(),
            offer = PaywallOfferState.UnknownError,
        ),
        screenSizeType = ScreenSizeType.Portrait,
        onClose = {},
        subscribe = {},
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
            offer = PaywallOfferState.Success(
                subscriptionOfferFormatted = "$9.99 / month",
            ),
        ),
        screenSizeType = ScreenSizeType.LandscapeExpanded,
        onClose = {},
        subscribe = {},
        restore = {},
    )
}
