/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Indication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun animatePressed(
    pressed: Boolean,
    pressedScale: Float = 0.96f
): Float {
    val animationSpec: AnimationSpec<Float> = spring(stiffness = 600f)
    return animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = animationSpec
    ).value
}

fun Modifier.pressedGesture(
    enabled: Boolean = true,
    rippleEffectEnabled: Boolean = false,
    onTap: () -> Unit,
    onPressed: PointerInputScope.(Boolean) -> Unit
): Modifier = composed {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val indication: Indication = rememberRipple(color = MaterialTheme.colors.primary)
    val pressedInteraction = remember { mutableStateOf<PressInteraction.Press?>(null) }

    val modifier = this.run {
        if (enabled) {
            pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onTap()
                    },
                    onPress = {
                        if (rippleEffectEnabled) {
                            pressedInteraction.value?.let { oldValue ->
                                val interaction = PressInteraction.Cancel(oldValue)
                                interactionSource.emit(interaction)
                                pressedInteraction.value = null
                            }
                            val interaction = PressInteraction.Press(it)
                            interactionSource.emit(interaction)
                            pressedInteraction.value = interaction
                        }
                        onPressed(true)

                        tryAwaitRelease()
                        if (rippleEffectEnabled) {
                            pressedInteraction.value?.let { pressRelease ->
                                interactionSource.emit(PressInteraction.Release(pressRelease))
                                pressedInteraction.value = null
                            }
                        }
                        onPressed(false)
                    }
                )
            }
        } else {
            this
        }
    }

    DisposableEffect(interactionSource) {
        onDispose {
            pressedInteraction.value?.let { oldValue ->
                val interaction = PressInteraction.Cancel(oldValue)
                interactionSource.tryEmit(interaction)
                pressedInteraction.value = null
            }
        }
    }

    modifier.indication(interactionSource, indication)
}