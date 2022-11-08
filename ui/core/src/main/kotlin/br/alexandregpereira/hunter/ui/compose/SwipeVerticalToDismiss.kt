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

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SwipeVerticalToDismiss(
    swipeTriggerDistance: Dp = 200.dp,
    onClose: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val swipeVerticalState = rememberSwipeVerticalState()
    SwipeVertical(
        state = swipeVerticalState,
        onSwipeTriggered = onClose,
        swipeTriggerDistance = swipeTriggerDistance,
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    this.translationY = swipeVerticalState.offset
                }
        ) {
            content()
        }
    }
}
