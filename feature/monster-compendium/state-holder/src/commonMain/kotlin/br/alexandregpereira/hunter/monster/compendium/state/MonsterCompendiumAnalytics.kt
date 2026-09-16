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

package br.alexandregpereira.hunter.monster.compendium.state

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.analytics.ScrollDepthSummary
import br.alexandregpereira.hunter.analytics.ScrollEvent
import br.alexandregpereira.hunter.domain.model.CompendiumSortType
import br.alexandregpereira.hunter.monster.compendium.domain.MonsterCompendiumError
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendium

class MonsterCompendiumAnalytics(
    private val analytics: Analytics
) {

    private var monsterCompendium: MonsterCompendium? = null

    fun trackMonsterCompendium(
        monsterCompendium: MonsterCompendium,
        scrollItemPosition: Int,
        sortType: CompendiumSortType,
    ) {
        this.monsterCompendium = monsterCompendium
        analytics.track(
            eventName = "MonsterCompendium - monsters loaded",
            params = mapOf(
                "itemsSize" to monsterCompendium.items.size,
                "scrollItemPosition" to scrollItemPosition,
                "sortType" to sortType.name,
            )
        )
    }

    fun trackItemClick(monsterIndex: String, isFolderCreationMode: Boolean) {
        analytics.track(
            eventName = "MonsterCompendium - item click",
            params = mapOf(
                "monsterIndex" to monsterIndex,
                "isFolderCreationMode" to isFolderCreationMode,
            )
        )
    }

    private fun trackMonsterCompendiumEmpty(throwable: Throwable): Boolean {
        return if (throwable is MonsterCompendiumError.NoMonsterError) {
            analytics.track(eventName = "MonsterCompendium - empty")
            true
        } else false
    }

    fun logException(throwable: Throwable) {
        if (trackMonsterCompendiumEmpty(throwable).not()) {
            analytics.logException(throwable)
        }
    }

    fun trackItemLongClick(index: String, isFolderCreationMode: Boolean) {
        analytics.track(
            eventName = "MonsterCompendium - item long click",
            params = mapOf(
                "monsterIndex" to index,
                "isFolderCreationMode" to isFolderCreationMode,
            )
        )
    }

    fun trackPopupOpened() {
        analytics.track(eventName = "MonsterCompendium - popup opened")
    }

    fun trackPopupClosed() {
        analytics.track(
            eventName = "MonsterCompendium - popup closed",
        )
    }

    fun trackAlphabetIndexClicked(position: Int) {
        analytics.track(
            eventName = "MonsterCompendium - alphabet index clicked",
            params = mapOf(
                "position" to position,
                "letter" to monsterCompendium?.alphabet?.get(position).orEmpty(),
            )
        )
    }

    fun trackTableContentIndexClicked(position: Int) {
        analytics.track(
            eventName = "MonsterCompendium - table content index clicked",
            params = mapOf(
                "position" to position,
                "tableContent" to monsterCompendium?.tableContent?.get(position)?.text.orEmpty(),
            )
        )
    }

    fun trackTableContentClosed() {
        analytics.track(
            eventName = "MonsterCompendium - table content closed",
        )
    }

    fun trackErrorButtonClick() {
        analytics.track(
            eventName = "MonsterCompendium - error button click",
        )
    }

    fun trackSearchClick() {
        analytics.track(
            eventName = "MonsterCompendium - search click",
        )
    }

    fun trackSortClick() {
        analytics.track(
            eventName = "MonsterCompendium - sort click",
        )
    }

    fun trackSortSelected(sortType: CompendiumSortType) {
        analytics.track(
            eventName = "MonsterCompendium - sort selected",
            params = mapOf(
                "sortType" to sortType.name,
            )
        )
    }

    fun trackFolderCreationClick() {
        analytics.track(eventName = "MonsterCompendium - folder creation click")
    }

    fun trackFolderCreationClose() {
        analytics.track(eventName = "MonsterCompendium - folder creation close")
    }

    fun trackFolderCreationConfirm() {
        analytics.track(eventName = "MonsterCompendium - folder creation confirm")
    }

    fun trackOpened(isFolderCreationMode: Boolean) {
        analytics.track(
            eventName = "MonsterCompendium - opened",
            params = mapOf(
                "isFolderCreationMode" to isFolderCreationMode,
            )
        )
    }

    fun trackClosed(scrollDepth: ScrollDepthSummary) {
        analytics.track(
            eventName = "MonsterCompendium - closed",
            params = scrollDepth.params,
        )
    }

    fun trackScroll(scrollEvent: ScrollEvent, sortType: CompendiumSortType) {
        val eventName = when (scrollEvent) {
            is ScrollEvent.Started -> "MonsterCompendium - scroll started"
            is ScrollEvent.DepthReached -> "MonsterCompendium - scroll depth reached"
        }
        analytics.track(
            eventName = eventName,
            params = scrollEvent.params + mapOf("sortType" to sortType.name),
        )
    }
}
