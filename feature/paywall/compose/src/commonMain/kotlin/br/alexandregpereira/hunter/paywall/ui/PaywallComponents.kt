package br.alexandregpereira.hunter.paywall.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.paywall.PaywallFeatureState
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.AppButtonType
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun PaywallScrollableContent(
    features: ImmutableList<PaywallFeatureState>,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier.verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Spacer(modifier = Modifier.height(24.dp))
    PaywallTitle()
    Spacer(modifier = Modifier.height(8.dp))
    PaywallDescription()
    Spacer(modifier = Modifier.height(24.dp))
    PaywallFeaturesComparisonTable(features)
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
internal fun PaywallFooter(
    subscriptionOfferFormatted: String,
    modifier: Modifier = Modifier,
    subscribe: () -> Unit,
    restore: () -> Unit,
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp),
) {
    PaywallOffer(offer = subscriptionOfferFormatted)
    PaywallButtons(
        subscribe = subscribe,
        restore = restore,
    )
}

@Composable
internal fun PaywallButtons(
    subscribe: () -> Unit,
    restore: () -> Unit,
) = Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
) {
    AppButton(
        text = strings.subscribeButton,
        modifier = Modifier,
        onClick = subscribe,
    )
    AppButton(
        text = strings.restoreButton,
        type = AppButtonType.TERTIARY,
        size = AppButtonSize.SMALL,
        onClick = restore,
    )
}

@Composable
internal fun PaywallTitle() {
    Text(
        text = strings.title,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 36.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.onSurface,
    )
}

@Composable
internal fun PaywallDescription() {
    Text(
        text = strings.description,
        fontSize = 16.sp,
        fontWeight = FontWeight.Light,
        color = MaterialTheme.colors.onSurface,
        textAlign = TextAlign.Center,
        lineHeight = 20.sp,
    )
}

@Composable
internal fun PaywallOffer(
    offer: String,
) = Column(
    verticalArrangement = Arrangement.spacedBy(4.dp),
) {
    Text(
        text = offer,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.align(Alignment.CenterHorizontally),
    )

    Text(
        text = strings.cancelAnytime,
        fontSize = 12.sp,
        fontStyle = FontStyle.Italic,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
        modifier = Modifier.align(Alignment.CenterHorizontally),
    )
}

@Composable
internal fun PaywallFeaturesComparisonTable(
    features: ImmutableList<PaywallFeatureState>,
) {
    val columnWidth = 80.dp

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = strings.featuresColumnHeader,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )

            FeatureColumnHeader(
                text = strings.freeColumnHeader,
                columnWidth = columnWidth,
                textColor = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            )

            FeatureColumnHeader(
                text = strings.premiumColumnHeader,
                columnWidth = columnWidth,
            )
        }

        Spacer(Modifier.height(8.dp))
        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))

        features.forEach { feature ->
            Spacer(Modifier.height(12.dp))

            FeatureRow(
                feature = feature.name,
                isPremium = feature.isPremium,
                columnWidth = columnWidth,
            )
        }
    }
}

@Composable
private fun FeatureColumnHeader(
    text: String,
    columnWidth: Dp,
    textColor: Color = MaterialTheme.colors.onSurface,
) = Box(
    modifier = Modifier.width(columnWidth),
    contentAlignment = Alignment.Center,
) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
    )
}

@Composable
private fun FeatureRow(
    feature: String,
    isPremium: Boolean,
    columnWidth: Dp,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = feature,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f),
        )

        FeatureCell(
            included = !isPremium,
            columnWidth = columnWidth,
        )

        FeatureCell(
            included = true,
            columnWidth = columnWidth,
            highlightIcon = true,
        )
    }
}

@Composable
private fun FeatureCell(
    included: Boolean,
    columnWidth: Dp,
    highlightIcon: Boolean = false,
) = Box(
    modifier = Modifier.width(columnWidth),
    contentAlignment = Alignment.Center,
) {
    val tintColor = if (highlightIcon) {
        MaterialTheme.colors.onSurface
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
    }
    if (included) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = tintColor,
            modifier = Modifier.size(20.dp),
        )
    } else {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = null,
            tint = tintColor,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PaywallScrollableContentPreview() = HunterTheme {
    PaywallScrollableContent(
        features = persistentListOf(
            PaywallFeatureState(
                name = "Access to all features",
                isPremium = true,
            ),
            PaywallFeatureState(
                name = "No Ads",
                isPremium = false,
            ),
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PaywallFeaturesComparisonTablePreview() = HunterTheme {
    PaywallFeaturesComparisonTable(
        features = persistentListOf(
            PaywallFeatureState(
                name = "Access to all features",
                isPremium = true,
            ),
            PaywallFeatureState(
                name = "No Ads",
                isPremium = false,
            ),
        )
    )
}
