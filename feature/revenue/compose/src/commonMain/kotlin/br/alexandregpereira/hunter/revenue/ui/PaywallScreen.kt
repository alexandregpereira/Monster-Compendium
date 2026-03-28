package br.alexandregpereira.hunter.revenue.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.ui.compose.AppScreen
import br.alexandregpereira.hunter.ui.compose.LocalAppContentPadding
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun PaywallScreen(
    isOpen: Boolean,
    onClose: () -> Unit,
    onPurchaseCompleted: () -> Unit,
    onPurchaseError: (message: String) -> Unit,
) = AppScreen(
    isOpen = isOpen,
    contentPaddingValues = LocalAppContentPadding.current,
    swipeTriggerPercentage = 0.4f,
    onClose = onClose,
) {
    RevenueCatPaywall(
        shouldDisplayDismissButton = true,
        onDismiss = onClose,
        onPurchaseCompleted = onPurchaseCompleted,
        onPurchaseError = onPurchaseError,
    )
}
