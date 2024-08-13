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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun AppWindow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = HunterTheme {
    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        content()
    }
}

val cardShape = RoundedCornerShape(16.dp)

@Composable
fun Window(
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    backgroundColor: Color = MaterialTheme.colors.surface,
    level: Int = 1,
    content: @Composable () -> Unit
) {
    Surface(
        color = backgroundColor,
        content = content,
        elevation = elevation,
        modifier = modifier
            .padding((level * 4).dp)
            .clip(shape = cardShape),
    )
}

@Composable
fun PreviewWindow(
    darkTheme: Boolean = isSystemInDarkTheme(),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = HunterTheme(darkTheme = darkTheme) {
    Window(modifier = modifier, content = content)
}

fun Modifier.noIndicationClick(onClick: () -> Unit = {}): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

inline operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val layoutDirection = LayoutDirection.Rtl
    return PaddingValues(
        start = this.calculateStartPadding(layoutDirection) +
                other.calculateStartPadding(layoutDirection),
        top = this.calculateTopPadding() + other.calculateTopPadding(),
        end = this.calculateEndPadding(layoutDirection) +
                other.calculateEndPadding(layoutDirection),
        bottom = this.calculateBottomPadding() + other.calculateBottomPadding()
    )
}
