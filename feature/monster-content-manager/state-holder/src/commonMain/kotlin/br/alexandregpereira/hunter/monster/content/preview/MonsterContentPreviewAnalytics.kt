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

package br.alexandregpereira.hunter.monster.content.preview

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem

internal class MonsterContentPreviewAnalytics(
    private val analytics: Analytics
) {

    fun trackLoaded(sourceAcronym: String, items: List<MonsterCompendiumItem>) {
        analytics.track(
            eventName = "MonsterContentPreview - loaded",
            params = mapOf(
                "itemsSize" to items.size,
                "sourceAcronym" to sourceAcronym,
            )
        )
    }

    fun logException(throwable: Throwable, sourceAcronym: String) {
        analytics.track(
            eventName = "MonsterContentPreview - error loaded",
            params = mapOf(
                "sourceAcronym" to sourceAcronym,
            )
        )
        analytics.logException(throwable)
    }

    fun trackClose(sourceAcronym: String) {
        analytics.track(
            eventName = "MonsterContentPreview - close",
            params = mapOf(
                "sourceAcronym" to sourceAcronym,
            )
        )
    }

    fun trackTableContentOpened() {
        analytics.track(eventName = "MonsterContentPreview - popup opened")
    }

    fun trackTableContentIndexClicked(position: Int, tableContent: List<TableContentItem>) {
        analytics.track(
            eventName = "MonsterContentPreview - table content index clicked",
            params = mapOf(
                "position" to position,
                "tableContent" to tableContent.getOrNull(position)?.text.orEmpty(),
            )
        )
    }

    fun trackTableContentClosed() {
        analytics.track(
            eventName = "MonsterContentPreview - table content closed",
        )
    }
}