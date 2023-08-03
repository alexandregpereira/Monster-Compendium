package br.alexandregpereira.hunter.folder.preview

import br.alexandregpereira.hunter.analytics.Analytics

internal class FolderPreviewAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackLoadMonstersResult(it: FolderPreviewState) {
        analytics.track(
            eventName = "Folder Preview - load monsters result",
            params = mapOf(
                "monstersSize" to it.monsters.size,
                "monsters" to it.monsters.map { it.index }.toString(),
                "showPreview" to it.showPreview
            )
        )
    }

    fun trackItemClick(monsterIndex: String) {
        analytics.track(
            eventName = "Folder Preview - item click",
            params = mapOf(
                "monsterIndex" to monsterIndex
            )
        )
    }

    fun trackItemLongClick(monsterIndex: String) {
        analytics.track(
            eventName = "Folder Preview - item long click",
            params = mapOf(
                "monsterIndex" to monsterIndex
            )
        )
    }

    fun trackSave() {
        analytics.track(
            eventName = "Folder Preview - save",
        )
    }

    fun trackSaveSuccess() {
        analytics.track(
            eventName = "Folder Preview - save success",
        )
    }

    fun trackSaveMonsterRemoved() {
        analytics.track(
            eventName = "Folder Preview - save monster removed",
        )
    }

    fun trackAddMonster(index: String) {
        analytics.track(
            eventName = "Folder Preview - add monster",
            params = mapOf(
                "monsterIndex" to index
            )
        )
    }

    fun trackHideFolderPreview() {
        analytics.track(
            eventName = "Folder Preview - hide",
        )
    }

    fun trackShowFolderPreview() {
        analytics.track(
            eventName = "Folder Preview - show",
        )
    }
}