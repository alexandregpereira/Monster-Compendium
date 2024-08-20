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