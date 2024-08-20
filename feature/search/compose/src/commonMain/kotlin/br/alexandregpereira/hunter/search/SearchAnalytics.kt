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

package br.alexandregpereira.hunter.search

import br.alexandregpereira.hunter.analytics.Analytics

internal class SearchAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackSearch(totalResults: Int, searchQuery: String) {
        analytics.track(
            eventName = "Search - search",
            params = mapOf(
                "totalResults" to totalResults,
                "searchQuery" to searchQuery
            )
        )
    }

    fun trackItemClick(index: String, searchQuery: String) {
        analytics.track(
            eventName = "Search - item click",
            params = mapOf(
                "index" to index,
                "searchQuery" to searchQuery
            )
        )
    }

    fun trackItemLongClick(index: String, searchQuery: String) {
        analytics.track(
            eventName = "Search - item long click",
            params = mapOf(
                "index" to index,
                "searchQuery" to searchQuery
            )
        )
    }
}