package br.alexandregpereira.hunter.folder.detail

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder

internal class FolderDetailAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackItemClicked(index: String) {
        analytics.track(
            eventName = "Folder Detail - item clicked",
            params = mapOf(
                "index" to index
            )
        )
    }

    fun trackItemLongClicked(index: String) {
        analytics.track(
            eventName = "Folder Detail - item long clicked",
            params = mapOf(
                "index" to index
            )
        )
    }

    fun trackClose() {
        analytics.track(
            eventName = "Folder Detail - close",
        )
    }

    fun trackMonstersLoaded(monsters: List<MonsterPreviewFolder>) {
        analytics.track(
            eventName = "Folder Detail - monsters loaded",
            params = mapOf(
                "monsters" to monsters.map { it.index }.toString()
            )
        )
    }

    fun trackShow() {
        analytics.track(
            eventName = "Folder Detail - show",
        )
    }
}
