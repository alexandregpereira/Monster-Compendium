package br.alexandregpereira.hunter.paywall.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.paywall.PaywallActionResultState
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessageContent
import br.alexandregpereira.hunter.ui.compose.PreviewWindow

@Composable
internal fun PaywallActionResultScreen(
    errorState: PaywallActionResultState,
    onTryAgainLoadOffer: () -> Unit,
    onTryAgainPurchase: () -> Unit,
    onComeBackToOffer: () -> Unit,
    onCloseClick: () -> Unit = {},
) = Box(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
    when (errorState) {
        PaywallActionResultState.GetCurrentOfferError -> EmptyScreenMessageContent(
            title = strings.offerLoadErrorTitle,
            description = strings.offerLoadErrorDescription,
            buttonText = strings.tryAgain,
            onButtonClick = onTryAgainLoadOffer,
        )

        PaywallActionResultState.PurchaseError -> EmptyScreenMessageContent(
            title = strings.purchaseErrorTitle,
            description = strings.purchaseErrorDescription,
            buttonText = strings.tryAgain,
            secondaryButtonText = strings.comeBackToOffer,
            onButtonClick = onTryAgainPurchase,
            onSecondaryButtonClick = onComeBackToOffer,
        )

        PaywallActionResultState.RestorePurchaseError -> EmptyScreenMessageContent(
            title = strings.restorePurchaseErrorTitle,
            description = strings.restorePurchaseErrorDescription,
            buttonText = strings.comeBackToOffer,
            onButtonClick = onComeBackToOffer,
        )

        PaywallActionResultState.Success -> EmptyScreenMessageContent(
            icon = Icons.Filled.CheckCircle,
            title = strings.subscriptionSuccessTitle,
            description = strings.subscriptionSuccessDescription,
            buttonText = strings.buttonContinue,
            onButtonClick = onCloseClick,
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun PaywallSuccessPreview() = PreviewWindow {
    PaywallActionResultScreen(
        errorState = PaywallActionResultState.Success,
        onTryAgainLoadOffer = {},
        onTryAgainPurchase = {},
        onComeBackToOffer = {},
    )
}

@Preview(
    showBackground = true,
)
@Composable
private fun PaywallScreenErrorPreview() = PreviewWindow {
    PaywallActionResultScreen(
        errorState = PaywallActionResultState.GetCurrentOfferError,
        onTryAgainLoadOffer = {},
        onTryAgainPurchase = {},
        onComeBackToOffer = {},
    )
}

@Preview(
    showBackground = true,
)
@Composable
private fun PaywallScreenPurchaseErrorPreview() = PreviewWindow {
    PaywallActionResultScreen(
        errorState = PaywallActionResultState.PurchaseError,
        onTryAgainLoadOffer = {},
        onTryAgainPurchase = {},
        onComeBackToOffer = {},
    )
}

@Preview(
    showBackground = true,
)
@Composable
private fun PaywallScreenRestorePurchaseErrorPreview() = PreviewWindow {
    PaywallActionResultScreen(
        errorState = PaywallActionResultState.RestorePurchaseError,
        onTryAgainLoadOffer = {},
        onTryAgainPurchase = {},
        onComeBackToOffer = {},
    )
}
