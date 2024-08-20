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

    fun trackShared() {
        analytics.track(
            eventName = "Folder Insert - shared",
        )
    }
}