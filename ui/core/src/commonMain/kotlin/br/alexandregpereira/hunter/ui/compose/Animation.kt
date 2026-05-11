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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.platform.LocalHapticFeedback
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun Modifier.animatePressed(
    pressedScale: Float = 0.9f,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null,
): Modifier = composed {
    val haptic = LocalHapticFeedback.current
    val onLongClickFinal: (() -> Unit)? = if (onLongClick == null) null else ({
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        onLongClick.invoke()
    })
    combinedClickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ScaleIndication(pressedScale),
        enabled = enabled,
        onClick = onClick,
        onLongClick = onLongClickFinal,
    )
}

class ScaleIndication(private val pressedScale: Float = 0.96f) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        ScaleIndicationNode(pressedScale, interactionSource)

    override fun equals(other: Any?) =
        other is ScaleIndication && other.pressedScale == pressedScale

    override fun hashCode() = pressedScale.hashCode()
}

private class ScaleIndicationNode(
    private val pressedScale: Float,
    private val interactionSource: InteractionSource,
) : Modifier.Node(), DrawModifierNode {

    private val animatableScale = Animatable(1f)

    override fun onAttach() {
        coroutineScope.launch {
            var animationJob: Job? = null
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        animationJob?.cancel()
                        animationJob = launch {
                            animatableScale.animateTo(
                                targetValue = pressedScale,
                                animationSpec = spring(
                                    stiffness = 600f,
                                    dampingRatio = Spring.DampingRatioNoBouncy
                                )
                            )
                        }
                    }

                    is PressInteraction.Release, is PressInteraction.Cancel -> {
                        animationJob?.cancel()
                        animationJob = launch {
                            animatableScale.animateTo(
                                targetValue = pressedScale,
                                animationSpec = spring(
                                    stiffness = 600f,
                                    dampingRatio = Spring.DampingRatioNoBouncy
                                )
                            )
                            animatableScale.animateTo(
                                targetValue = 1f,
                                animationSpec = spring(
                                    stiffness = 400f,
                                    dampingRatio = Spring.DampingRatioMediumBouncy
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        scale(animatableScale.value) {
            this@draw.drawContent()
        }
    }
}
