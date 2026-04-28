package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.AppCard
import br.alexandregpereira.hunter.ui.compose.animatePressed
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun PremiumCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) = HunterTheme(darkTheme = isSystemInDarkTheme().not()){
    AppCard(
        modifier = modifier.animatePressed(onClick = onClick),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .alpha(.9f)
                    .clip(shape = MaterialTheme.shapes.large)
                    .background(color = MaterialTheme.colors.primary)
                    .size(56.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = null,
                )
            }
            Column(
                modifier = Modifier.weight(.6f),
            ) {
                Text(
                    text = "Compendium Premium",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                Text(
                    text = "Remove ads",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                )
            }
            AppButton(
                text = "Upgrade",
                size = AppButtonSize.VERY_SMALL,
                modifier = Modifier.weight(.4f),
                onClick = onClick,
            )
        }
    }
}

@Preview
@Composable
private fun PremiumCardPreview() = HunterTheme {
    PremiumCard()
}
