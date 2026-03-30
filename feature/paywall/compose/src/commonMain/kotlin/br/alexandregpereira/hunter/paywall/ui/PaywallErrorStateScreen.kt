package br.alexandregpereira.hunter.paywall.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.paywall.PaywallErrorState
import br.alexandregpereira.hunter.paywall.PaywallState
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessageContent
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.ScreenSizeType

@Composable
internal fun PaywallErrorStateScreen(
    errorState: PaywallErrorState,
    onTryAgainLoadOffer: () -> Unit,
    onTryAgainPurchase: () -> Unit,
    onComeBackToOffer: () -> Unit,
) = Box(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
    when (errorState) {
        PaywallErrorState.GetCurrentOfferError -> EmptyScreenMessageContent(
            title = strings.offerLoadErrorTitle,
            description = strings.offerLoadErrorDescription,
            buttonText = strings.tryAgain,
            onButtonClick = onTryAgainLoadOffer,
        )

        PaywallErrorState.PurchaseError -> EmptyScreenMessageContent(
            title = strings.purchaseErrorTitle,
            description = strings.purchaseErrorDescription,
            buttonText = strings.tryAgain,
            secondaryButtonText = strings.comeBackToOffer,
            onButtonClick = onTryAgainPurchase,
            onSecondaryButtonClick = onComeBackToOffer,
        )

        PaywallErrorState.RestorePurchaseError -> EmptyScreenMessageContent(
            title = strings.restorePurchaseErrorTitle,
            description = strings.restorePurchaseErrorDescription,
            buttonText = strings.comeBackToOffer,
            onButtonClick = onComeBackToOffer,
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun PaywallScreenErrorPreview() = PreviewWindow {
    PaywallScreen(
        state = PaywallState(
            isOpen = true,
            errorState = PaywallErrorState.GetCurrentOfferError,
        ),
        screenSizeType = ScreenSizeType.Portrait,
        onClose = {},
        subscribe = {},
        restore = {},
    )
}

@Preview(
    showBackground = true,
)
@Composable
private fun PaywallScreenPurchaseErrorPreview() = PreviewWindow {
    PaywallScreen(
        state = PaywallState(
            isOpen = true,
            errorState = PaywallErrorState.PurchaseError,
        ),
        screenSizeType = ScreenSizeType.Portrait,
        onClose = {},
        subscribe = {},
        restore = {},
    )
}

@Preview(
    showBackground = true,
)
@Composable
private fun PaywallScreenRestorePurchaseErrorPreview() = PreviewWindow {
    PaywallScreen(
        state = PaywallState(
            isOpen = true,
            errorState = PaywallErrorState.RestorePurchaseError,
        ),
        screenSizeType = ScreenSizeType.Portrait,
        onClose = {},
        subscribe = {},
        restore = {},
    )
}
