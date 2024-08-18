package br.alexandregpereira.hunter.folder.list

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder

internal class FolderListAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackFolderClick(folderName: String) {
        analytics.track(
            eventName = "Folder List - folder click",
            params = mapOf(
                "folderName" to folderName
            )
        )
    }

    fun trackItemSelectionClose() {
        analytics.track(
            eventName = "Folder List - item selection close",
        )
    }

    fun trackItemSelectionDeleteClick() {
        analytics.track(
            eventName = "Folder List - item selection delete click",
        )
    }

    fun trackItemSelect(folderName: String) {
        analytics.track(
            eventName = "Folder List - item select",
            params = mapOf(
                "folderName" to folderName
            )
        )
    }

    fun trackFoldersLoaded(folders: List<Pair<MonsterFolder, Boolean>>) {
        analytics.track(
            eventName = "Folder List - folders loaded",
            params = mapOf(
                "folders" to folders.map { it.first.name }.toString()
            )
        )
    }

    fun trackItemSelectionAddToPreviewClick() {
        analytics.track(
            eventName = "Folder List - item selection add to preview click",
        )
    }
}