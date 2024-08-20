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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    opened: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(),
    maxWidth: Dp = maxBottomSheetWidth,
    widthFraction: Float = 1f,
    closeClickingOutside: Boolean = true,
    topSpaceHeight: Dp = 288.dp,
    onClose: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    var enterExitState: EnterExitState? by remember { mutableStateOf(null) }
    val swipeVerticalState: SwipeVerticalState = rememberSwipeVerticalState(key = enterExitState)

    if (closeClickingOutside) {
        Closeable(
            isOpen = opened,
            onClosed = onClose,
            getScrollOffset = { swipeVerticalState.offset.toInt() }
        )
    }

    SwipeVerticalToDismiss(
        swipeVerticalState = swipeVerticalState,
        visible = opened,
        onClose = onClose,
        onAnimationStateChange = { state ->
            enterExitState = state
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val scrollState = rememberScrollState()
            ClearFocusWhenScrolling(scrollState)
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .align(Alignment.BottomCenter)
                    .run {
                        if (closeClickingOutside) {
                            noIndicationClick(onClick = onClose)
                        } else this
                    },
                horizontalAlignment = Alignment.End,
            ) {
                if (closeClickingOutside) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(topSpaceHeight + contentPadding.calculateTopPadding())
                    )
                }
                val screenSize = LocalScreenSize.current
                Window(
                    modifier = Modifier.widthIn(
                        max = maxWidth.takeIf { screenSize.isLandscape } ?: maxBottomSheetWidth
                    ).fillMaxWidth(
                        fraction = widthFraction.takeIf { screenSize.isLandscape } ?: 1f
                    ).noIndicationClick(),
                    level = 0,
                ) {
                    Column(
                        modifier = modifier.padding(
                            start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                            end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
                        )
                    ) {
                        content()
                        Spacer(modifier = Modifier.height(contentPadding.calculateBottomPadding()))
                    }
                }
            }
        }
    }
}

val maxBottomSheetWidth get() = 720.dp
