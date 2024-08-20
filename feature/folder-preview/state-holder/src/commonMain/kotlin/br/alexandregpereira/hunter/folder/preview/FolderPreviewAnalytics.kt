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

    fun trackAddMonster(indexes: List<String>) {
        analytics.track(
            eventName = "Folder Preview - add monster",
            params = mapOf(
                "monsterIndexes" to indexes
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

    fun trackClear() {
        analytics.track(
            eventName = "Folder Preview - clear",
        )
    }
}