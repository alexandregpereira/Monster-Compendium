package br.alexandregpereira.hunter.revenue.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.AppButtonType

@Composable
internal fun PaywallScrollableContent(
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier.verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Spacer(modifier = Modifier.height(48.dp))
    PaywallTitle()
    Spacer(modifier = Modifier.height(16.dp))
    PaywallDescription()
    Spacer(modifier = Modifier.height(48.dp))
    PaywallFeaturesComparisonTable()
    Spacer(modifier = Modifier.height(48.dp))
}
@Composable
internal fun PaywallFooter(
    modifier: Modifier = Modifier,
    subscribe: () -> Unit,
    restore: () -> Unit,
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp),
) {
    PaywallOffer(offer = "$4.99 / month")
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
        text = "Subscribe",
        modifier = Modifier,
        onClick = subscribe,
    )
    AppButton(
        text = "Restore subscription",
        type = AppButtonType.TERTIARY,
        size = AppButtonSize.SMALL,
        onClick = restore,
    )
}

@Composable
internal fun PaywallTitle() {
    Text(
        text = "Subscribe to support\nthe project",
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
        text = "With your support we will continue to\nmaintaining the project",
        fontSize = 14.sp,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
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
        text = "Cancel anytime",
        fontSize = 12.sp,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
        modifier = Modifier.align(Alignment.CenterHorizontally),
    )
}

@Composable
internal fun PaywallFeaturesComparisonTable() {
    val freeColumnWidth = 80.dp
    val proColumnWidth = 80.dp

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Features",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )

            // FREE column header
            Box(
                modifier = Modifier.width(freeColumnWidth),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "FREE",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                )
            }

            // PRO column header — white pill badge with a check icon
            Box(
                modifier = Modifier.width(proColumnWidth),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colors.onSurface)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Premium",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onPrimary,
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
        Spacer(Modifier.height(12.dp))

        // Row: Access to all features
        FeatureRow(
            feature = "Access to all features",
            freeIncluded = true,
            proIncluded = true,
            freeColumnWidth = freeColumnWidth,
            proColumnWidth = proColumnWidth,
        )

        Spacer(Modifier.height(12.dp))

        // Row: No Ads
        FeatureRow(
            feature = "No Ads",
            freeIncluded = false,
            proIncluded = true,
            freeColumnWidth = freeColumnWidth,
            proColumnWidth = proColumnWidth,
        )
    }
}

@Composable
private fun FeatureRow(
    feature: String,
    freeIncluded: Boolean,
    proIncluded: Boolean,
    freeColumnWidth: Dp,
    proColumnWidth: Dp,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = feature,
            fontSize = 14.sp,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.weight(1f),
        )

        // FREE indicator
        Box(
            modifier = Modifier.width(freeColumnWidth),
            contentAlignment = Alignment.Center,
        ) {
            if (freeIncluded) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp),
                )
            } else {
                // Dash — "not included"
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        // PRO indicator
        Box(
            modifier = Modifier.width(proColumnWidth),
            contentAlignment = Alignment.Center,
        ) {
            if (proIncluded) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(20.dp),
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
