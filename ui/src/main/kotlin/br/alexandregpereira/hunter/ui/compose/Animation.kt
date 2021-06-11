/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
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