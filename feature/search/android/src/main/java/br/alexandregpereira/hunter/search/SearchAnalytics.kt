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