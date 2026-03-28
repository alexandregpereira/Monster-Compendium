package br.alexandregpereira.hunter.revenue.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppScreen
import br.alexandregpereira.hunter.ui.compose.LocalAppContentPadding
import br.alexandregpereira.hunter.ui.compose.LocalScreenSize
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.ScreenSizeType

@Composable
internal fun PaywallScreen(
    isOpen: Boolean,
    screenSizeType: ScreenSizeType = LocalScreenSize.current.type,
    onClose: () -> Unit,
    subscribe: () -> Unit,
    restore: () -> Unit,
) = AppScreen(
    isOpen = isOpen,
    contentPaddingValues = LocalAppContentPadding.current,
    swipeTriggerPercentage = 0.4f,
    onClose = onClose,
) {
    when (screenSizeType) {
        ScreenSizeType.Portrait -> PaywallScreenContent(
            modifier = Modifier.padding(bottom = 16.dp),
            subscribe = subscribe,
            restore = restore,
        )
        ScreenSizeType.LandscapeCompact,
        ScreenSizeType.LandscapeExpanded -> PaywallLandscapeScreenContent(
            modifier = Modifier,
            subscribe = subscribe,
            restore = restore,
        )
    }
}

@Composable
internal fun PaywallScreenContent(
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
        )

        PaywallFooter(
            subscribe = subscribe,
            restore = restore,
        )
    }
}

@Composable
internal fun PaywallLandscapeScreenContent(
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
        )
        PaywallFooter(
            modifier = Modifier.weight(.4f),
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
    PaywallScreen(
        isOpen = true,
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
        isOpen = true,
        screenSizeType = ScreenSizeType.LandscapeExpanded,
        onClose = {},
        subscribe = {},
        restore = {},
    )
}
