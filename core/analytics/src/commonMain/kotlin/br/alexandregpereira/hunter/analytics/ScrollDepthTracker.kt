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

/**
 * Accumulates how far a single screen visit browsed, so scrolling can be measured without sending
 * an event for every item that appears.
 *
 * [onScroll] is meant to be called on every scroll change, but it only returns events the first
 * time the visit scrolls and the first time it reaches a new threshold. That caps the tracking at
 * 9 events per visit, and browsing back over what was already seen reports nothing.
 *
 * A screen can be restored at a position the user did not scroll to, so the position it starts on
 * becomes the baseline, and the thresholds both below it and above it are still to be reached:
 * scrolling up from the baseline counts as browsing just as much as scrolling down.
 *
 * The visit browses between two frontiers, and each direction moves its own: the bottom of the
 * screen is how far down it got, and the top of the screen is how far up it got.
 *
 * Call [reset] when a visit starts and [summary] when it ends.
 */
class ScrollDepthTracker {

    private val reportedThresholds = mutableSetOf<Pair<ScrollDirection, Int>>()
    private var baselineDepthPercent = NO_POSITION
    private var baselineTopPercent = NO_POSITION
    private var maxDepthPercent = NO_POSITION
    private var minTopPercent = NO_POSITION
    private var maxItemIndex = 0
    private var minItemIndex = 0
    private var itemsSize = 0
    private var scrollStarted = false

    /**
     * @param firstVisibleItemIndex the shallowest item on screen, which is how far up the visit got.
     * @param lastVisibleItemIndex the deepest item on screen, which is how far down it got. The
     * first visible item is not enough for that, since the last items of a list can never be
     * scrolled to the top.
     * @return the events to track, empty when the visit is browsing over what it already saw.
     */
    fun onScroll(
        firstVisibleItemIndex: Int,
        lastVisibleItemIndex: Int,
        itemsSize: Int,
    ): List<ScrollEvent> {
        if (itemsSize <= 0 || firstVisibleItemIndex < 0 || lastVisibleItemIndex < 0) {
            return emptyList()
        }
        this.itemsSize = itemsSize
        val depthPercent = depthPercentOf(lastVisibleItemIndex, itemsSize)
        val topPercent = topPercentOf(firstVisibleItemIndex, itemsSize)

        if (baselineDepthPercent == NO_POSITION) {
            baselineDepthPercent = depthPercent
            baselineTopPercent = topPercent
            maxDepthPercent = depthPercent
            minTopPercent = topPercent
            maxItemIndex = lastVisibleItemIndex
            minItemIndex = firstVisibleItemIndex
            return emptyList()
        }

        val direction = when {
            depthPercent > maxDepthPercent -> ScrollDirection.DOWN
            topPercent < minTopPercent -> ScrollDirection.UP
            // Still inside the range the visit already browsed.
            else -> return emptyList()
        }
        val position = when (direction) {
            ScrollDirection.DOWN -> {
                maxDepthPercent = depthPercent
                maxItemIndex = lastVisibleItemIndex
                ScrollPosition(depthPercent, lastVisibleItemIndex, baselineDepthPercent)
            }
            ScrollDirection.UP -> {
                minTopPercent = topPercent
                minItemIndex = firstVisibleItemIndex
                ScrollPosition(topPercent, firstVisibleItemIndex, baselineTopPercent)
            }
        }

        return buildList {
            if (scrollStarted.not()) {
                scrollStarted = true
                add(ScrollEvent.Started(direction, position, itemsSize))
            }
            val threshold = direction.thresholdReached(position) ?: return@buildList
            if (reportedThresholds.add(direction to threshold)) {
                add(ScrollEvent.DepthReached(threshold, direction, position, itemsSize))
            }
        }
    }

    fun summary(): ScrollDepthSummary = ScrollDepthSummary(
        didScroll = scrollStarted,
        didScrollDown = maxDepthPercent > baselineDepthPercent,
        didScrollUp = minTopPercent < baselineTopPercent,
        maxDepthPercent = maxDepthPercent.coerceAtLeast(0),
        minTopPercent = minTopPercent.coerceAtLeast(0),
        maxItemIndex = maxItemIndex,
        minItemIndex = minItemIndex,
        itemsSize = itemsSize,
        baselineDepthPercent = baselineDepthPercent.coerceAtLeast(0),
        baselineTopPercent = baselineTopPercent.coerceAtLeast(0),
    )

    fun reset() {
        reportedThresholds.clear()
        baselineDepthPercent = NO_POSITION
        baselineTopPercent = NO_POSITION
        maxDepthPercent = NO_POSITION
        minTopPercent = NO_POSITION
        maxItemIndex = 0
        minItemIndex = 0
        itemsSize = 0
        scrollStarted = false
    }

    private companion object {
        const val NO_POSITION = -1
    }
}

/** How far into the list a frontier moved, and where it started the visit. */
data class ScrollPosition(
    val percent: Int,
    val itemIndex: Int,
    val baselinePercent: Int,
)

enum class ScrollDirection {
    DOWN,
    UP;

    /**
     * The threshold the frontier just reached, or null when it is still between the baseline and
     * the next one. Only the furthest threshold of a jump is returned: the ones before it are
     * implied, since a query for "reached at least 50%" also matches the further buckets.
     */
    internal fun thresholdReached(position: ScrollPosition): Int? = when (this) {
        DOWN -> THRESHOLDS.lastOrNull {
            it <= position.percent && it > position.baselinePercent
        }
        UP -> THRESHOLDS.firstOrNull {
            it >= position.percent && it < position.baselinePercent
        }
    }

    private companion object {
        val THRESHOLDS = listOf(0, 25, 50, 75, 100)
    }
}

sealed interface ScrollEvent {

    val direction: ScrollDirection
    val position: ScrollPosition
    val itemsSize: Int

    val params: Map<String, Any?>
        get() = mapOf(
            "direction" to direction.name,
            "depthPercent" to position.percent,
            "itemIndex" to position.itemIndex,
            "itemsSize" to itemsSize,
            "baselineDepthPercent" to position.baselinePercent,
        )

    /** The first time a visit scrolls away from the position it started on, in any direction. */
    data class Started(
        override val direction: ScrollDirection,
        override val position: ScrollPosition,
        override val itemsSize: Int,
    ) : ScrollEvent

    /** A threshold reached for the first time in a visit, in that direction. */
    data class DepthReached(
        val thresholdPercent: Int,
        override val direction: ScrollDirection,
        override val position: ScrollPosition,
        override val itemsSize: Int,
    ) : ScrollEvent {
        override val params: Map<String, Any?>
            get() = super.params + mapOf("thresholdPercent" to thresholdPercent)
    }
}

/** How far a whole visit browsed, meant to be attached to the event closing the screen. */
data class ScrollDepthSummary(
    val didScroll: Boolean,
    val didScrollDown: Boolean,
    val didScrollUp: Boolean,
    val maxDepthPercent: Int,
    val minTopPercent: Int,
    val maxItemIndex: Int,
    val minItemIndex: Int,
    val itemsSize: Int,
    val baselineDepthPercent: Int,
    val baselineTopPercent: Int,
) {
    val params: Map<String, Any?>
        get() = mapOf(
            "didScroll" to didScroll,
            "didScrollDown" to didScrollDown,
            "didScrollUp" to didScrollUp,
            "maxDepthPercent" to maxDepthPercent,
            "minTopPercent" to minTopPercent,
            "maxItemIndex" to maxItemIndex,
            "minItemIndex" to minItemIndex,
            "itemsSize" to itemsSize,
            "baselineDepthPercent" to baselineDepthPercent,
            "baselineTopPercent" to baselineTopPercent,
        )
}

/** How far down the bottom of the screen is: the whole list is browsed when it reaches 100. */
private fun depthPercentOf(lastVisibleItemIndex: Int, itemsSize: Int): Int =
    ((lastVisibleItemIndex + 1) * 100 / itemsSize).coerceIn(0, 100)

/** How far down the top of the screen is: the list is back to the start when it reaches 0. */
private fun topPercentOf(firstVisibleItemIndex: Int, itemsSize: Int): Int =
    (firstVisibleItemIndex * 100 / itemsSize).coerceIn(0, 100)
