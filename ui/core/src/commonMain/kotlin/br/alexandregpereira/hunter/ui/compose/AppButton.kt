/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.ln

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    size: AppButtonSize = AppButtonSize.MEDIUM,
    enabled: Boolean = true,
    isPrimary: Boolean = true,
    onClick: () -> Unit = {}
) {
    AppBasicButton(
        modifier = modifier.height(size.height.dp).fillMaxWidth(),
        enabled = enabled,
        isPrimary = isPrimary,
        onClick = onClick
    ) {
        val fontSizes = when (size) {
            AppButtonSize.VERY_SMALL -> 12.sp
            AppButtonSize.SMALL,
            AppButtonSize.MEDIUM -> 16.sp
        }
        Text(
            text = text,
            fontWeight = FontWeight.Normal,
            color = LocalContentColor.current,
            fontSize = fontSizes,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
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
        isPrimary = isPrimary,
        onClick = onClick
    ) {
        content()
    }
}

@Composable
private fun AppBasicButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    isPrimary: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val backgroundColorAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.5f
    )

    val backgroundColor = if (isPrimary) {
        MaterialTheme.colors.primary
    } else {
        val alpha = ((4.5f * ln(1f + 1)) + 2f) / 100f
        MaterialTheme.colors.onSurface.copy(alpha = alpha)
            .compositeOver(MaterialTheme.colors.surface)
    }
    val contentColor = if (isPrimary) {
        MaterialTheme.colors.onPrimary
    } else {
        MaterialTheme.colors.onSurface
    }

    Box(
        modifier = modifier
            .animatePressed(
                enabled = enabled,
                onClick = onClick
            )
            .clip(shape)
            .background(color = backgroundColor.copy(alpha = backgroundColorAlpha)),
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

@Preview
@Composable
private fun AppButtonPreview() = Window {
    Column {
        AppButton(text = "Text")
        AppButton(
            text = "Text Disabled",
            enabled = false,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
