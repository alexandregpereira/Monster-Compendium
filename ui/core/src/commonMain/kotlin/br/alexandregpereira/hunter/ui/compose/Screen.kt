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

import androidx.compose.animation.EnterExitState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun AppScreen(
    isOpen: Boolean,
    contentPaddingValues: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier,
    showCloseButton: Boolean = true,
    backgroundColor: Color = MaterialTheme.colors.surface,
    level: Int = 1,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    var enterExitState: EnterExitState? by remember { mutableStateOf(null) }
    val swipeVerticalState: SwipeVerticalState = rememberSwipeVerticalState(key = enterExitState)
    Closeable(
        isOpen = isOpen,
        onClosed = onClose,
        getScrollOffset = { swipeVerticalState.offset.toInt() }
    )

    SwipeVerticalToDismiss(
        visible = isOpen,
        swipeVerticalState = swipeVerticalState,
        onAnimationStateChange = { enterExitState = it },
        onClose = onClose
    ) {
        Window(backgroundColor = backgroundColor, level = level, modifier = modifier) {
            if (showCloseButton) {
                BoxClosable(
                    contentPaddingValues = contentPaddingValues,
                    onClick = onClose,
                    content = content,
                )
            } else {
                content()
            }
        }
    }
}

@Composable
fun AppFullScreen(
    isOpen: Boolean,
    contentPaddingValues: PaddingValues = PaddingValues(),
    backgroundColor: Color = MaterialTheme.colors.surface,
    level: Int = 1,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) = AppScreen(
    isOpen = isOpen,
    contentPaddingValues = contentPaddingValues,
    modifier = Modifier.fillMaxSize(),
    backgroundColor = backgroundColor,
    level = level,
    onClose = onClose,
    content = content
)

@Composable
fun BoxClosable(
    contentPaddingValues: PaddingValues = PaddingValues(),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Box(modifier) {
    content()
    BoxCloseButton(
        showIcon = true,
        onClick = onClick,
        modifier = Modifier
            .padding(top = contentPaddingValues.calculateTopPadding())
    )
}

@Composable
fun BoxCloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showIcon: Boolean = false,
) = Box(
    modifier = modifier.size(48.dp)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = false),
            onClick = onClick
        )
        .clip(CircleShape)
        .run {
            if (showIcon) {
                background(MaterialTheme.colors.surface.copy(alpha = .8f))
            } else this
        }
        .semantics { contentDescription = "Close" },
    contentAlignment = Alignment.Center
) {
    if (showIcon) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
        )
    }
}
