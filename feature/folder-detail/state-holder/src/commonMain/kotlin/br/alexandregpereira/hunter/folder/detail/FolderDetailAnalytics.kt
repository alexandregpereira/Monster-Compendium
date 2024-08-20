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
