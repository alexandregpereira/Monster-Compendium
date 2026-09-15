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

import kotlin.test.Test
import kotlin.test.assertEquals

class PageIndicatorTest {

    @Test
    fun `first page shows the selected dot at the center and dots only on the right`() {
        assertEquals(
            PageIndicatorDot(x = 180f, sizeFraction = 1f, alpha = 1f),
            dot(index = 0, position = 0f)
        )
        assertEquals(0..10, visibleIndexes(position = 0f))
    }

    @Test
    fun `dots fade out near the edges`() {
        assertEquals(1f, dot(index = 7, position = 0f).alpha)
        assertEquals(1f / 3f, dot(index = 9, position = 0f).alpha, absoluteTolerance = 0.001f)
        assertEquals(0f, dot(index = 10, position = 0f).alpha)
    }

    @Test
    fun `middle page fills both sides up to the edges`() {
        assertEquals(10..30, visibleIndexes(position = 20f))
        assertEquals(0f, dot(index = 10, position = 20f).x)
        assertEquals(0f, dot(index = 10, position = 20f).alpha)
        assertEquals(360f, dot(index = 30, position = 20f).x)
        assertEquals(0f, dot(index = 30, position = 20f).alpha)
    }

    @Test
    fun `last page shows dots only on the left`() {
        assertEquals(29..39, visibleIndexes(position = 39f))
        assertEquals(180f, dot(index = 39, position = 39f).x)
    }

    @Test
    fun `halfway swipe shares the selected size`() {
        assertEquals(
            PageIndicatorDot(x = 171f, sizeFraction = 0.5f, alpha = 1f),
            dot(index = 2, position = 2.5f)
        )
        assertEquals(
            PageIndicatorDot(x = 189f, sizeFraction = 0.5f, alpha = 1f),
            dot(index = 3, position = 2.5f)
        )
    }

    @Test
    fun `few pages shows all dots without fading`() {
        val indexes = visibleIndexes(position = 0f, pageCount = 3)

        assertEquals(0..2, indexes)
        assertEquals(listOf(1f, 1f, 1f), indexes.map { dot(index = it, position = 0f).alpha })
    }

    private fun dot(index: Int, position: Float): PageIndicatorDot {
        return pageIndicatorDot(
            index = index,
            position = position,
            width = WIDTH,
            spacing = SPACING,
            fadeWidth = FADE_WIDTH,
        )
    }

    private fun visibleIndexes(position: Float, pageCount: Int = 40): IntRange {
        return pageIndicatorVisibleIndexes(
            position = position,
            pageCount = pageCount,
            width = WIDTH,
            spacing = SPACING,
        )
    }

    private companion object {
        const val WIDTH = 360f
        const val SPACING = 18f
        const val FADE_WIDTH = 54f
    }
}
