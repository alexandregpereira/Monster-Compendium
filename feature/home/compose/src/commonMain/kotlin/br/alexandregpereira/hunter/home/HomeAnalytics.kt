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

package br.alexandregpereira.hunter.home

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.home.ui.HomeCategoryType
import br.alexandregpereira.hunter.home.ui.HomeSectionState

internal class HomeAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.OpenSettings -> analytics.track(eventName = "Home - menu click")
            HomeIntent.OpenSearch -> analytics.track(eventName = "Home - search click")
            HomeIntent.OpenMonsterCompendium -> {
                analytics.track(eventName = "Home - monster compendium click")
            }
            HomeIntent.OpenSpellCompendium -> {
                analytics.track(eventName = "Home - spell compendium click")
            }
            HomeIntent.OpenFolderList -> analytics.track(eventName = "Home - see all folders click")
            is HomeIntent.OpenFolderDetail -> {
                analytics.track(
                    eventName = "Home - folder click",
                    params = mapOf(
                        "folderName" to intent.folderName,
                    )
                )
            }
            HomeIntent.OpenExtraContentManager -> {
                analytics.track(eventName = "Home - manage extra content click")
            }
            HomeIntent.CreateMonster -> analytics.track(eventName = "Home - create monster click")
            HomeIntent.CreateSpell -> analytics.track(eventName = "Home - create spell click")
            is HomeIntent.OpenMonsterDetail -> {
                analytics.track(
                    eventName = "Home - recently viewed monster click",
                    params = mapOf(
                        "monsterIndex" to intent.index,
                    )
                )
            }
        }
    }

    fun trackSectionsLoaded(sections: List<HomeSectionState>, isReload: Boolean) {
        val categories = sections.filterIsInstance<HomeSectionState.Categories>().firstOrNull()
            ?.categories.orEmpty()
        analytics.track(
            eventName = "Home - sections loaded",
            params = mapOf(
                "sections" to sections.joinToString { it.analyticsName },
                "isReload" to isReload,
                "monsters" to categories.firstOrNull { it.type == HomeCategoryType.CREATURES }?.total,
                "spells" to categories.firstOrNull { it.type == HomeCategoryType.SPELLS }?.total,
                "folders" to sections.filterIsInstance<HomeSectionState.Folders>().firstOrNull()
                    ?.folders?.size,
                "recentlyViewed" to sections.filterIsInstance<HomeSectionState.RecentlyViewed>()
                    .firstOrNull()?.monsters?.size,
            )
        )
    }

    /**
     * The extra content is loaded from the API in parallel with the other sections, so it has its
     * own event. A null [extraContent] means there is no extra content to show.
     */
    fun trackExtraContentLoaded(extraContent: HomeSectionState.ExtraContent?, isReload: Boolean) {
        analytics.track(
            eventName = "Home - extra content loaded",
            params = mapOf(
                "isReload" to isReload,
                "extraContentAdded" to extraContent?.added,
                "extraContentTotal" to extraContent?.total,
            )
        )
    }

    private val HomeSectionState.analyticsName: String
        get() = when (this) {
            HomeSectionState.Search -> "Search"
            is HomeSectionState.Categories -> "Categories"
            is HomeSectionState.RecentlyViewed -> "RecentlyViewed"
            is HomeSectionState.Folders -> "Folders"
            HomeSectionState.Create -> "Create"
            is HomeSectionState.ExtraContent -> "ExtraContent"
        }
}
