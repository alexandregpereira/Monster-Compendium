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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun SwipeVerticalToDismiss(
    visible: Boolean,
    modifier: Modifier = Modifier,
    swipeVerticalState: SwipeVerticalState? = null,
    onClose: () -> Unit = {},
    onAnimationStateChange: (EnterExitState) -> Unit = {},
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { fullHeight -> fullHeight },
        exit = slideOutVertically { fullHeight -> fullHeight },
        modifier = modifier
    ) {
        val animationState = this.transition.currentState
        LaunchedEffect(animationState) {
            onAnimationStateChange(animationState)
        }
        val safeSwipeVerticalState = swipeVerticalState ?: rememberSwipeVerticalState(
            key = animationState
        )
        SwipeVertical(
            state = safeSwipeVerticalState,
            onSwipeTriggered = onClose,
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        this.translationY = safeSwipeVerticalState.offset
                    }
            ) {
                content()
            }
        }
    }
}
