package br.alexandregpereira.hunter.folder.insert

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder

internal class FolderInsertAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackMonsterRemoved() {
        analytics.track(
            eventName = "Folder Insert - monster removed",
        )
    }

    fun trackFolderSaved() {
        analytics.track(
            eventName = "Folder Insert - folder saved",
        )
    }

    fun trackClosed() {
        analytics.track(
            eventName = "Folder Insert - closed",
        )
    }

    fun trackFoldersLoaded(folders: List<Pair<String, Int>>) {
        analytics.track(
            eventName = "Folder Insert - folders loaded",
            params = mapOf(
                "folders" to folders.toString()
            )
        )
    }

    fun trackMonsterPreviewsLoaded(monsters: List<MonsterPreviewFolder>) {
        analytics.track(
            eventName = "Folder Insert - monster previews loaded",
            params = mapOf(
                "monsters" to monsters.map { it.index }.toString()
            )
        )
    }

    fun trackOpened(monsterIndexes: List<String>) {
        analytics.track(
            eventName = "Folder Insert - opened",
            params = mapOf(
                "monsterIndexes" to monsterIndexes.toString()
            )
        )
    }
}