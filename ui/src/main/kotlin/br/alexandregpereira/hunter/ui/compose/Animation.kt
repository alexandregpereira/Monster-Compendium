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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.animatePressed(
    pressedScale: Float = 0.96f,
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null,
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val haptic = LocalHapticFeedback.current

    animatePressed(
        interactionSource = interactionSource,
        pressedScale = pressedScale
    ).combinedClickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick,
        onLongClick = {
            onLongClick?.run {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                invoke()
            }
        }
    )
}

fun Modifier.animatePressed(
    interactionSource: InteractionSource,
    pressedScale: Float = 0.96f
): Modifier = composed {
    val animationSpec: AnimationSpec<Float> = spring(stiffness = 600f)
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = animationSpec
    )

    this.graphicsLayer(
        scaleX = scale,
        scaleY = scale
    )
}
