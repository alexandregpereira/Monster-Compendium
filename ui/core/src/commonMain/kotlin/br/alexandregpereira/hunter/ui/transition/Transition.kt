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

@file:OptIn(ExperimentalFoundationApi::class)

package br.alexandregpereira.hunter.ui.transition

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <Data> AlphaTransition(
    dataList: List<Data>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    enableGesture: () -> Boolean = { true },
    content: @Composable (data: Data) -> Unit
) = Transition(dataList, pagerState, modifier, enableGesture) { data, fraction, isTarget ->
    Box(
        Modifier.graphicsLayer {
            alpha = lerp(
                start = if (isTarget) 0f else 1f,
                stop = if (isTarget) 1f else 0f,
                fraction = fraction()
            )
        }
    ) {
        content(data)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <Data> HorizontalSlideTransition(
    dataList: List<Data>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    enableGesture: () -> Boolean = { true },
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

        val value = lerp(
            start = if (isTarget) {
                width
            } else 0,
            stop = if (isTarget.not()) {
                -width
            } else 0,
            fraction = fraction()
        )

        layout(width, height) {
            currentPlaceable.placeRelative(x = value, y = 0)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <Data> Transition(
    dataList: List<Data>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    enableGesture: () -> Boolean = { true },
    content: @Composable (data: Data, fraction: () -> Float, isTarget: Boolean) -> Unit
) {
    val transitionData = getTransitionData(dataList, getPageOffset = { pagerState.getPageOffset() })

    val boxModifier = if (enableGesture()) {
        modifier.transitionHorizontalScrollable(pagerState)
    } else modifier
    Box(boxModifier) {
        content(
            transitionData.data,
            { transitionData.fraction },
            false
        )
        if (transitionData.data != transitionData.nextData) {
            content(
                transitionData.nextData,
                { transitionData.fraction },
                true
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.transitionHorizontalScrollable(
    pagerState: PagerState
): Modifier = composed {
    scrollable(
        orientation = Orientation.Horizontal,
        reverseDirection = true,
        flingBehavior = PagerDefaults.flingBehavior(pagerState),
        state = pagerState
    )
}

fun <Data> getTransitionData(
    dataList: List<Data>,
    getPageOffset: () -> Float
): TransitionData<Data> {
    val pageOffset = getPageOffset()
    val currentPageOffsetDecimal = pageOffset - pageOffset.toInt()
    val currentIndex = pageOffset.toInt()
    val nextIndex = if (currentPageOffsetDecimal > 0f) {
        pageOffset.toInt() + 1
    } else pageOffset.toInt()

    val fraction = currentPageOffsetDecimal.absoluteValue.coerceIn(0f, 1f)
    return TransitionData(dataList[currentIndex], dataList[nextIndex], fraction)
}

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.getPageOffset(): Float {
    return (this.currentPage + this.currentPageOffsetFraction).coerceAtLeast(0f)
}

data class TransitionData<Data>(
    val data: Data,
    val nextData: Data,
    val fraction: Float
)
