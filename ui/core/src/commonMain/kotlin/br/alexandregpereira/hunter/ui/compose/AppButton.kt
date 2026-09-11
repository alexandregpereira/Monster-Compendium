/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ln

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    size: AppButtonSize = AppButtonSize.MEDIUM,
    enabled: Boolean = true,
    type: AppButtonType,
    elevation: Int = 1,
    onClick: () -> Unit = {}
) {
    AppBasicButton(
        modifier = modifier.height(size.height.dp).fillMaxWidth(),
        enabled = enabled,
        type = type,
        elevation = elevation,
        onClick = onClick
    ) {
        val fontSizes = when (size) {
            AppButtonSize.VERY_SMALL -> 12.sp
            AppButtonSize.SMALL,
            AppButtonSize.MEDIUM -> 16.sp
        }
        val textDecoration = TextDecoration.Underline.takeIf {
            type == AppButtonType.TERTIARY
        }
        Text(
            text = text,
            fontWeight = FontWeight.Normal,
            color = LocalContentColor.current,
            fontSize = fontSizes,
            textAlign = TextAlign.Center,
            textDecoration = textDecoration,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    size: AppButtonSize = AppButtonSize.MEDIUM,
    enabled: Boolean = true,
    isPrimary: Boolean = true,
    elevation: Int = 1,
    onClick: () -> Unit = {}
) {
    AppButton(
        text = text,
        modifier = modifier,
        size = size,
        enabled = enabled,
        type = if (isPrimary) AppButtonType.PRIMARY else AppButtonType.SECONDARY,
        elevation = elevation,
        onClick = onClick
    )
}

@Composable
fun AppCircleButton(
    modifier: Modifier = Modifier,
    size: AppButtonSize = AppButtonSize.MEDIUM,
    enabled: Boolean = true,
    isPrimary: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    AppBasicButton(
        modifier = modifier.size(size.height.dp),
        enabled = enabled,
        shape = CircleShape,
        type = if (isPrimary) AppButtonType.PRIMARY else AppButtonType.SECONDARY,
        onClick = onClick
    ) {
        content()
    }
}

@Composable
fun AppDropdownButton(
    text: String,
    options: List<String>,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    title: String? = null,
    size: AppButtonSize = AppButtonSize.SMALL,
    type: AppButtonType = AppButtonType.SECONDARY,
    backgroundAlpha: Float = 1f,
    onClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
    onOptionSelected: (index: Int) -> Unit = {},
) = Box(modifier) {
    val isVerySmall = size == AppButtonSize.VERY_SMALL
    AppBasicButton(
        modifier = Modifier.height(size.height.dp),
        type = type,
        backgroundAlpha = backgroundAlpha,
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                start = if (isVerySmall) 12.dp else 16.dp,
                end = if (isVerySmall) 6.dp else 8.dp,
            ),
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Normal,
                color = LocalContentColor.current,
                fontSize = if (isVerySmall) 12.sp else 16.sp,
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = LocalContentColor.current,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .size(if (isVerySmall) 16.dp else 24.dp),
            )
        }
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        if (title != null) {
            Text(
                text = title,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        options.forEachIndexed { index, option ->
            DropdownMenuItem(onClick = { onOptionSelected(index) }) {
                Text(
                    text = option,
                    color = MaterialTheme.colors.onSurface,
                )
            }
        }
    }
}

@Composable
private fun AppBasicButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    type: AppButtonType = AppButtonType.PRIMARY,
    elevation: Int = 1,
    backgroundAlpha: Float = 1f,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val backgroundColorAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.5f
    )

    val backgroundColor = when (type) {
        AppButtonType.PRIMARY -> MaterialTheme.colors.primary
        AppButtonType.SECONDARY -> {
            val alpha = ((4.5f * ln(elevation.toFloat() + 1)) + 2f) / 100f
            MaterialTheme.colors.onSurface.copy(alpha = alpha)
                .compositeOver(MaterialTheme.colors.surface)
        }
        AppButtonType.TERTIARY -> MaterialTheme.colors.surface.copy(alpha = 0.1f)
    }
    val contentColor = when (type) {
        AppButtonType.PRIMARY -> MaterialTheme.colors.onPrimary
        AppButtonType.SECONDARY,
        AppButtonType.TERTIARY -> MaterialTheme.colors.onSurface
    }

    Box(
        modifier = modifier
            .animatePressed(
                enabled = enabled,
                onClick = onClick
            )
            .clip(shape)
            .background(color = backgroundColor.copy(alpha = backgroundColorAlpha * backgroundAlpha)),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor
        ) {
            content()
        }
    }
}

enum class AppButtonSize(val height: Int) {
    VERY_SMALL(32),
    SMALL(40),
    MEDIUM(48),
}

enum class AppButtonType {
    PRIMARY,
    SECONDARY,
    TERTIARY,
}

@Preview
@Composable
private fun AppButtonPreview() = PreviewWindow {
    Column {
        AppButton(text = "Primary")
        AppButton(
            text = "Text Disabled",
            enabled = false,
            modifier = Modifier.padding(top = 16.dp)
        )
        AppButton(
            text = "Secondary",
            type = AppButtonType.SECONDARY,
            modifier = Modifier.padding(top = 16.dp)
        )
        AppButton(
            text = "Tertiary",
            type = AppButtonType.TERTIARY,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
