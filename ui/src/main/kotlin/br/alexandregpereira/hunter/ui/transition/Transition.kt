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

package br.alexandregpereira.hunter.ui.transition

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerDefaults
import com.google.accompanist.pager.PagerState
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun <Data> AlphaTransition(
    dataList: List<Data>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    enableGesture: Boolean = true,
    content: @Composable (data: Data, alpha: Float) -> Unit
) = Transition(dataList, pagerState, modifier, enableGesture) { data, fraction, isTarget ->
    content(
        data,
        alpha = lerp(
            start = if (isTarget) 0f else 1f,
            stop = if (isTarget) 1f else 0f,
            fraction = fraction
        )
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun <Data> HorizontalSlideTransition(
    dataList: List<Data>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    enableGesture: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable (data: Data) -> Unit
) = Transition(dataList, pagerState, modifier, enableGesture) { data, fraction, isTarget ->
    Layout(
        content = {
            content(data)
        }
    ) { measurables, constraints ->
        val currentPlaceable = measurables.first().measure(constraints)

        val width = currentPlaceable.width
        val height = currentPlaceable.height

        val scrollDirection = pagerState.getScrollDirection()

        val value = lerp(
            start = if (isTarget) {
                when (scrollDirection) {
                    ScrollDirection.LEFT -> width + 24.dp.roundToPx()
                    ScrollDirection.RIGHT -> -width
                    ScrollDirection.IDLE -> 0
                }
            } else 0,
            stop = if (isTarget.not()) {
                when (scrollDirection) {
                    ScrollDirection.LEFT -> -width
                    ScrollDirection.RIGHT -> width + 24.dp.roundToPx()
                    ScrollDirection.IDLE -> 0
                }
            } else 0,
            fraction = fraction
        )

        layout(width, height) {
            currentPlaceable.placeRelative(x = value, y = 0)
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun <Data> Transition(
    dataList: List<Data>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    enableGesture: Boolean = true,
    content: @Composable (data: Data, fraction: Float, isTarget: Boolean) -> Unit
) {
    val transitionData = getTransitionData(dataList, pagerState)

    val boxModifier = if (enableGesture) {
        modifier.scrollable(
            orientation = Orientation.Horizontal,
            reverseDirection = true,
            flingBehavior = PagerDefaults.flingBehavior(pagerState),
            state = pagerState
        )
    } else modifier
    Box(boxModifier) {
        content(
            transitionData.data,
            transitionData.fraction,
            isTarget = false
        )
        if (transitionData.data != transitionData.nextData) {
            content(
                transitionData.nextData,
                transitionData.fraction,
                isTarget = true
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
fun <Data> getTransitionData(
    dataList: List<Data>,
    pagerState: PagerState
): TransitionData<Data> {
    val (currentIndex, nextIndex) = pagerState.getCurrentAndNextIndex()
    val currentPageOffsetDecimal = pagerState.run {
        currentPageOffset - currentPageOffset.toInt()
    }
    val fraction = currentPageOffsetDecimal.absoluteValue.coerceIn(0f, 1f)
    return TransitionData(dataList[currentIndex], dataList[nextIndex], fraction)
}

@OptIn(ExperimentalPagerApi::class)
fun PagerState.getCurrentAndNextIndex(): Pair<Int, Int> {
    val scrollDirection = getScrollDirection()
    val pageOffset = getPageOffset()
    val currentIndex = when (scrollDirection) {
        ScrollDirection.RIGHT -> pageOffset.toInt() + 1
        else -> pageOffset.toInt()
    }
    val nextIndex = when (scrollDirection) {
        ScrollDirection.LEFT -> pageOffset.toInt() + 1
        else -> pageOffset.toInt()
    }

    return currentIndex to nextIndex
}

@OptIn(ExperimentalPagerApi::class)
fun PagerState.getScrollDirection(): ScrollDirection {
    val currentPageOffsetDecimal = this.run {
        currentPageOffset - currentPageOffset.toInt()
    }
    return when {
        currentPageOffsetDecimal > 0f -> ScrollDirection.LEFT
        currentPageOffsetDecimal < 0f -> ScrollDirection.RIGHT
        else -> ScrollDirection.IDLE
    }
}

@OptIn(ExperimentalPagerApi::class)
private fun PagerState.getPageOffset(): Float {
    return (this.currentPage + this.currentPageOffset).coerceAtLeast(0f)
}

data class TransitionData<Data>(
    val data: Data,
    val nextData: Data,
    val fraction: Float
)

enum class ScrollDirection {
    LEFT, RIGHT, IDLE
}
