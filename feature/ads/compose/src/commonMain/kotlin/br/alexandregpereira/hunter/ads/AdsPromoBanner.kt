package br.alexandregpereira.hunter.ads

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.AppButtonType
import br.alexandregpereira.hunter.ui.compose.AppSurface

@Composable
internal fun AdsPromoBanner(
    strings: AdsStrings,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) = AppSurface(
    modifier = modifier,
    color = MaterialTheme.colors.surface,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = strings.promoBannerTitle,
                fontSize = 15.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = strings.promoBannerMessage,
                fontSize = 13.sp,
                lineHeight = 17.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        AppButton(
            text = strings.promoBannerButton,
            modifier = Modifier.width(112.dp),
            size = AppButtonSize.SMALL,
            type = AppButtonType.PRIMARY,
            onClick = onClick,
        )
    }
}
