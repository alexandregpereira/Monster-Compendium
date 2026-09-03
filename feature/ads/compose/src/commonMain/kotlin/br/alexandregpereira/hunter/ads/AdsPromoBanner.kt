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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
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
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            AutoSizeText(
                text = strings.promoBannerTitle,
                maxFontSize = 15.sp,
                minFontSize = 11.sp,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
            )
            AutoSizeText(
                text = strings.promoBannerMessage,
                maxFontSize = 13.sp,
                minFontSize = 9.sp,
                maxLines = 2,
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

/**
 * The banner slot height is a fraction of the screen height, so the text does not always fit in
 * [maxLines], specially when the device is using a bigger font scale. The font size is decreased
 * until the text fits or until [minFontSize] is reached.
 */
@Composable
private fun AutoSizeText(
    text: String,
    maxFontSize: TextUnit,
    minFontSize: TextUnit,
    maxLines: Int,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null,
) {
    var fontSize by remember(text, maxFontSize) { mutableStateOf(maxFontSize) }
    var isTextMeasured by remember(text, maxFontSize) { mutableStateOf(false) }

    Text(
        text = text,
        modifier = modifier.drawWithContent {
            if (isTextMeasured) drawContent()
        },
        fontSize = fontSize,
        lineHeight = fontSize * LINE_HEIGHT_RATIO,
        fontWeight = fontWeight,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.hasVisualOverflow && fontSize > minFontSize) {
                fontSize = (fontSize.value - FONT_SIZE_STEP)
                    .coerceAtLeast(minFontSize.value).sp
            } else {
                isTextMeasured = true
            }
        },
    )
}

private const val LINE_HEIGHT_RATIO = 1.3f
private const val FONT_SIZE_STEP = 0.5f
