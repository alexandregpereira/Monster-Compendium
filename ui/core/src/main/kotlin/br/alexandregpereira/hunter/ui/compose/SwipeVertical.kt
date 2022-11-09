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

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * Based from the library [Accompanist Swipe Refresh](https://github.com/google/accompanist/tree/main/swiperefresh).
 */
@Composable
fun SwipeVertical(
    state: SwipeVerticalState,
    onSwipeTriggered: () -> Unit,
    modifier: Modifier = Modifier,
    swipeEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val updatedOnSwipeTriggered = rememberUpdatedState(onSwipeTriggered)

    // Our LaunchedEffect, which animates the content to its resting position
    LaunchedEffect(state.isSwipeInProgress) {
        if (!state.isSwipeInProgress && state.isSwipeTriggered().not()) {
            // If there's not a swipe in progress, rest the indicator at 0f
            state.animateOffsetTo(0f)
        }
    }

    // Our nested scroll connection, which updates our state.
    val nestedScrollConnection = remember(state, coroutineScope) {
        SwipeVerticalNestedScrollConnection(state, coroutineScope) {
            // On swipe triggered, re-dispatch to the update onSwipeTriggered block
            updatedOnSwipeTriggered.value.invoke()
        }
    }.apply {
        this.enabled = swipeEnabled
    }

    Box(
        modifier
            .nestedScroll(connection = nestedScrollConnection)
            .onGloballyPositioned {
                state.height = it.size.height.toFloat()
            }
    ) {
        content()
    }
}

@Composable
fun rememberSwipeVerticalState(
    swipeTriggerPercentage: Float = 0.1f
): SwipeVerticalState {
    return remember {
        SwipeVerticalState(swipeTriggerPercentage)
    }
}

class SwipeVerticalState(
    private val swipeTriggerPercentage: Float
) {
    private val _offset = Animatable(0f)
    private val mutatorMutex = MutatorMutex()

    private val swipeTrigger: Float
        get() = height * swipeTriggerPercentage

    var height: Float by mutableStateOf(0f)
        internal set

    /**
     * Whether a swipe/drag is currently in progress.
     */
    var isSwipeInProgress: Boolean by mutableStateOf(false)
        internal set

    /**
     * The current offset in pixels.
     */
    val offset: Float get() = _offset.value

    internal suspend fun animateOffsetTo(offset: Float) {
        mutatorMutex.mutate {
            _offset.animateTo(offset)
        }
    }

    /**
     * Dispatch scroll delta in pixels from touch events.
     */
    internal suspend fun dispatchScrollDelta(delta: Float) {
        mutatorMutex.mutate(MutatePriority.UserInput) {
            _offset.snapTo(_offset.value + delta)
        }
    }

    fun isSwipeTriggered(): Boolean = offset >= swipeTrigger
}

private class SwipeVerticalNestedScrollConnection(
    private val state: SwipeVerticalState,
    private val coroutineScope: CoroutineScope,
    private val onSwipeTriggered: () -> Unit,
) : NestedScrollConnection {
    var enabled: Boolean = false

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset = when {
        // If swiping isn't enabled, return zero
        !enabled -> Offset.Zero
        // If the user is swiping up, handle it
        source == NestedScrollSource.Drag && available.y < 0 -> onScroll(available)
        else -> Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset = when {
        // If swiping isn't enabled, return zero
        !enabled -> Offset.Zero
        // If the user is swiping down and there's y remaining, handle it
        source == NestedScrollSource.Drag && available.y > 0 -> onScroll(available)
        else -> Offset.Zero
    }

    private fun onScroll(available: Offset): Offset {
        if (available.y > 0) {
            state.isSwipeInProgress = true
        } else if (state.offset.roundToInt() == 0) {
            state.isSwipeInProgress = false
        }

        val newOffset = (available.y * DragMultiplier + state.offset).coerceAtLeast(0f)
        val dragConsumed = newOffset - state.offset

        return if (dragConsumed.absoluteValue >= 0.5f) {
            coroutineScope.launch {
                state.dispatchScrollDelta(dragConsumed)
            }
            // Return the consumed Y
            Offset(x = 0f, y = dragConsumed / DragMultiplier)
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        // If we're dragging and scrolled past the trigger point, trigger.
        if (state.isSwipeTriggered()) {
            onSwipeTriggered()
        }

        // Reset the drag in progress state
        state.isSwipeInProgress = false

        // Don't consume any velocity, to allow the scrolling layout to fling
        return Velocity.Zero
    }
}

private const val DragMultiplier = 1f
