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
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.OverscrollFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class MildBounceOverscrollFactory : OverscrollFactory {
    override fun createOverscrollEffect(): OverscrollEffect = MildBounceOverscrollEffect()
    override fun equals(other: Any?): Boolean = other is MildBounceOverscrollFactory
    override fun hashCode(): Int = 0
}

private class MildBounceOverscrollEffect : OverscrollEffect {
    private var offsetY by mutableStateOf(0f)
    private var bounceJob: Job? = null
    private var edgeHitHandled = false
    private var nodeScope: CoroutineScope? = null

    override val isInProgress: Boolean get() = offsetY != 0f

    override fun applyToScroll(
        delta: Offset,
        source: NestedScrollSource,
        performScroll: (Offset) -> Offset
    ): Offset {
        val consumed = performScroll(delta)
        val overscrollY = delta.y - consumed.y
        when {
            source == NestedScrollSource.UserInput && overscrollY != 0f -> {
                bounceJob?.cancel()
                offsetY += overscrollY * Resistance
                // Report what was actually consumed: the scroll's portion + the overscroll
                // effect's portion. Returning delta would over-report on the x axis if a
                // horizontal component wasn't consumed.
                return consumed + Offset(0f, overscrollY)
            }
            // SideEffect is the source during a fling animation. When performScroll doesn't
            // consume all the delta the fling has hit an edge. Start the spring immediately
            // here rather than waiting for performFling to return (which on iOS can take ~1s).
            source == NestedScrollSource.SideEffect && overscrollY != 0f && !edgeHitHandled -> {
                edgeHitHandled = true
                val startOffset = offsetY + overscrollY * Resistance
                offsetY = startOffset
                bounceJob?.cancel()
                bounceJob = nodeScope?.launch { springBackToZero(startOffset) }
            }
        }
        return delta
    }

    override suspend fun applyToFling(
        velocity: Velocity,
        performFling: suspend (Velocity) -> Velocity
    ) {
        // Cancel before resetting the flag to narrow the window where a second concurrent
        // fling could reset edgeHitHandled while SideEffect callbacks from the first are live.
        bounceJob?.cancel()
        edgeHitHandled = false
        performFling(velocity)
        // Edge spring is already handled in applyToScroll(SideEffect). Only spring back here
        // for drag-based overscroll (UserInput) that wasn't followed by an edge-hitting fling.
        if (!edgeHitHandled && offsetY != 0f) {
            springBackToZero(offsetY)
        }
    }

    private suspend fun springBackToZero(from: Float) {
        Animatable(from).animateTo(0f, SpringSpec) {
            offsetY = value
        }
    }

    override val node: DelegatableNode = object : Modifier.Node(), DrawModifierNode {
        override fun onAttach() {
            nodeScope = coroutineScope
        }

        override fun onDetach() {
            bounceJob?.cancel()
            bounceJob = null
            offsetY = 0f
            nodeScope = null
        }

        override fun ContentDrawScope.draw() {
            drawContext.canvas.save()
            drawContext.canvas.translate(0f, offsetY)
            drawContent()
            drawContext.canvas.restore()
        }
    }
}

private const val Resistance = 0.35f
private val SpringSpec = spring<Float>(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessMediumLow,
)
