package br.alexandregpereira.hunter.revenue.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.ui.compose.LocalAppContentPadding
import br.alexandregpereira.hunter.ui.compose.VerticalDismiss
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun PaywallScreen(
    isOpen: Boolean,
    subscribe: () -> Unit,
) = VerticalDismiss(
    visible = isOpen,
    modifier = Modifier.padding(LocalAppContentPadding.current),
) {
    Window(
        modifier = Modifier.fillMaxSize(),
    ) {
        PaywallScreenContent(
            subscribe = subscribe,
        )
    }
}
