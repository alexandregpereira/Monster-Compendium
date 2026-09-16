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

package br.alexandregpereira.hunter.analytics

import br.alexandregpereira.hunter.analytics.ScrollDirection.DOWN
import br.alexandregpereira.hunter.analytics.ScrollDirection.UP
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScrollDepthTrackerTest {

    private val tracker = ScrollDepthTracker()

    @Test
    fun `The position the visit starts on is the baseline and reports nothing`() {
        assertEquals(expected = emptyList(), actual = scrollTo(3))
        assertFalse(tracker.summary().didScroll)
    }

    @Test
    fun `The first scroll is reported even when no threshold is reached`() {
        scrollTo(0)

        assertEquals(expected = listOf(DOWN to null), actual = scrollTo(10).describe())
        assertEquals(expected = emptyList(), actual = scrollTo(14))
    }

    @Test
    fun `Each downward threshold is reported once`() {
        scrollTo(0)

        val reported = listOf(10, 20, 45, 70, 90).flatMap { scrollTo(it).describe() }

        assertEquals(
            expected = listOf(DOWN to null, DOWN to 25, DOWN to 50, DOWN to 75, DOWN to 100),
            actual = reported,
        )
    }

    @Test
    fun `Each upward threshold is reported once`() {
        scrollTo(90)

        val reported = listOf(85, 70, 45, 20, 0).flatMap { scrollTo(it).describe() }

        assertEquals(
            expected = listOf(UP to null, UP to 75, UP to 50, UP to 25, UP to 0),
            actual = reported,
        )
    }

    @Test
    fun `A jump reports only the furthest threshold reached`() {
        scrollTo(0)
        assertEquals(
            expected = listOf(DOWN to null, DOWN to 75),
            actual = scrollTo(70).describe(),
        )

        tracker.reset()

        scrollTo(90)
        assertEquals(
            expected = listOf(UP to null, UP to 25),
            actual = scrollTo(20).describe(),
        )
    }

    @Test
    fun `Browsing back over what was already seen is not reported again`() {
        scrollTo(45)
        scrollTo(70)
        scrollTo(20)

        listOf(70, 45, 20, 40, 65).forEach {
            assertEquals(expected = emptyList(), actual = scrollTo(it))
        }
    }

    @Test
    fun `Browsing both ways reports both directions`() {
        scrollTo(45)

        val reported = listOf(70, 20).flatMap { scrollTo(it).describe() }

        assertEquals(
            expected = listOf(DOWN to null, DOWN to 75, UP to 25),
            actual = reported,
        )
        assertEquals(
            expected = ScrollDepthSummary(
                didScroll = true,
                didScrollDown = true,
                didScrollUp = true,
                maxDepthPercent = 80,
                minTopPercent = 20,
                maxItemIndex = 79,
                minItemIndex = 20,
                itemsSize = 100,
                baselineDepthPercent = 55,
                baselineTopPercent = 45,
            ),
            actual = tracker.summary(),
        )
    }

    @Test
    fun `Only one scroll started event is reported per visit`() {
        scrollTo(45)

        val started = listOf(55, 70, 30, 5).flatMap { position ->
            scrollTo(position).filterIsInstance<ScrollEvent.Started>()
        }

        assertEquals(expected = 1, actual = started.size)
        assertEquals(expected = DOWN, actual = started.single().direction)
    }

    /**
     * The restored position is not somewhere the user scrolled to, so browsing up from it is as
     * much of a scroll as browsing down.
     */
    @Test
    fun `Scrolling only upward is reported as scrolling`() {
        scrollTo(90)

        assertEquals(expected = listOf(UP to null, UP to 75), actual = scrollTo(70).describe())

        val summary = tracker.summary()
        assertTrue(summary.didScroll)
        assertTrue(summary.didScrollUp)
        assertFalse(summary.didScrollDown)
        assertEquals(expected = 70, actual = summary.minTopPercent)
        assertEquals(expected = 100, actual = summary.maxDepthPercent)
    }

    @Test
    fun `A list fitting on screen is never reported as scrolled`() {
        scrollTo(0, itemsSize = 10)

        assertEquals(expected = emptyList(), actual = scrollTo(0, itemsSize = 10))
        assertFalse(tracker.summary().didScroll)
        assertEquals(expected = 100, actual = tracker.summary().maxDepthPercent)
    }

    @Test
    fun `An empty or not laid out list is ignored`() {
        assertEquals(expected = emptyList(), actual = scrollTo(0, itemsSize = 0))
        assertEquals(expected = emptyList(), actual = scrollTo(-1))
        assertEquals(expected = 0, actual = tracker.summary().itemsSize)
    }

    @Test
    fun `Resetting starts a new visit`() {
        scrollTo(0)
        scrollTo(90)

        tracker.reset()

        assertFalse(tracker.summary().didScroll)
        assertEquals(expected = emptyList(), actual = scrollTo(0))
        assertEquals(
            expected = listOf(DOWN to null, DOWN to 100),
            actual = scrollTo(90).describe(),
        )
    }

    @Test
    fun `The section tracker reports each section only once`() {
        val sectionTracker = SectionViewTracker()

        assertTrue(sectionTracker.isFirstView("goblin/actions"))
        assertFalse(sectionTracker.isFirstView("goblin/actions"))
        assertTrue(sectionTracker.isFirstView("orc/actions"))

        sectionTracker.reset()

        assertTrue(sectionTracker.isFirstView("goblin/actions"))
    }

    /**
     * Scrolls a list of [itemsSize] items so [firstVisibleItemIndex] is at the top of a screen
     * showing [VIEWPORT_ITEMS] items, like a real viewport with a frontier at each end.
     */
    private fun scrollTo(firstVisibleItemIndex: Int, itemsSize: Int = 100): List<ScrollEvent> =
        tracker.onScroll(
            firstVisibleItemIndex = firstVisibleItemIndex,
            lastVisibleItemIndex = (firstVisibleItemIndex + VIEWPORT_ITEMS - 1)
                .coerceAtMost(itemsSize - 1),
            itemsSize = itemsSize,
        )

    /** A scroll started event is a null threshold, so the order of both kinds stays visible. */
    private fun List<ScrollEvent>.describe(): List<Pair<ScrollDirection, Int?>> = map { event ->
        when (event) {
            is ScrollEvent.Started -> event.direction to null
            is ScrollEvent.DepthReached -> event.direction to event.thresholdPercent
        }
    }

    private companion object {
        const val VIEWPORT_ITEMS = 10
    }
}
