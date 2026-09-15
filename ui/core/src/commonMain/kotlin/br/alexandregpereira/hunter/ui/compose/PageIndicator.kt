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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import br.alexandregpereira.hunter.ui.transition.transitionHorizontalScrollable
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

val PageIndicatorHeight: Dp = 32.dp

/**
 * Dots indicator where the current page dot stays at the horizontal center and the row of
 * dots slides following the pager offset. The dots fill the whole width, fading out at the edges.
 */
@Composable
fun PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    dotColor: Color = MaterialTheme.colors.onSurface,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(PageIndicatorHeight)
            .transitionHorizontalScrollable(pagerState)
    ) {
        // The pager state is read in the draw phase to avoid recompositions while swiping
        val pageCount = pagerState.pageCount
        if (pageCount <= 0) return@Canvas

        val position = (pagerState.currentPage + pagerState.currentPageOffsetFraction)
            .coerceIn(0f, (pageCount - 1).toFloat())
        val spacing = DotSpacing.toPx()
        val fadeWidth = EdgeFadeWidth.toPx()
        val radius = DotRadius.toPx()
        val selectedRadius = SelectedDotRadius.toPx()
        val indexes = pageIndicatorVisibleIndexes(position, pageCount, size.width, spacing)

        for (index in indexes) {
            val dot = pageIndicatorDot(index, position, size.width, spacing, fadeWidth)
            if (dot.alpha <= 0f) continue
            drawCircle(
                color = dotColor,
                radius = lerp(radius, selectedRadius, dot.sizeFraction),
                center = Offset(x = dot.x, y = center.y),
                alpha = dot.alpha,
            )
        }
    }
}

internal data class PageIndicatorDot(
    val x: Float,
    val sizeFraction: Float,
    val alpha: Float,
)

/**
 * @param x horizontal center of the dot, where the current page is at the middle of [width].
 * @param sizeFraction 1 when the dot is the current page, 0 when it is one page or more away.
 * @param alpha 1 until [fadeWidth] from an edge, fading to 0 at the edge.
 */
internal fun pageIndicatorDot(
    index: Int,
    position: Float,
    width: Float,
    spacing: Float,
    fadeWidth: Float,
): PageIndicatorDot {
    val offset = index - position
    val x = width / 2f + offset * spacing
    return PageIndicatorDot(
        x = x,
        sizeFraction = 1f - offset.absoluteValue.coerceAtMost(1f),
        alpha = (min(x, width - x) / fadeWidth).coerceIn(0f, 1f),
    )
}

internal fun pageIndicatorVisibleIndexes(
    position: Float,
    pageCount: Int,
    width: Float,
    spacing: Float,
): IntRange {
    val halfDots = ceil(width / 2f / spacing).toInt()
    val first = (floor(position).toInt() - halfDots).coerceAtLeast(0)
    val last = (ceil(position).toInt() + halfDots).coerceAtMost(pageCount - 1)
    return first..last
}

private val DotSpacing = 18.dp
private val DotRadius = 3.dp
private val SelectedDotRadius = 5.dp
private val EdgeFadeWidth = 112.dp

@Preview
@Composable
private fun PageIndicatorFirstPagePreview() = PageIndicatorPreview(initialPage = 0)

@Preview
@Composable
private fun PageIndicatorMiddlePagePreview() = PageIndicatorPreview(initialPage = 20)

@Preview
@Composable
private fun PageIndicatorLastPagePreview() = PageIndicatorPreview(initialPage = 39)

@Composable
private fun PageIndicatorPreview(initialPage: Int) = PreviewWindow {
    PageIndicator(
        pagerState = rememberPagerState(initialPage = initialPage, pageCount = { 40 }),
    )
}
