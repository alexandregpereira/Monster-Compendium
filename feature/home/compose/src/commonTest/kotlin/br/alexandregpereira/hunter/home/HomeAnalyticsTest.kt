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
import br.alexandregpereira.hunter.home.ui.HomeCategoryState
import br.alexandregpereira.hunter.home.ui.HomeCategoryType
import br.alexandregpereira.hunter.home.ui.HomeSectionState
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeAnalyticsTest {

    private val analytics = FakeAnalytics()
    private val homeAnalytics = HomeAnalytics(analytics)

    @Test
    fun `folder click is tracked with the folder name`() {
        homeAnalytics.trackIntent(HomeIntent.OpenFolderDetail(folderName = "Dragons"))

        assertEquals(
            listOf("Home - folder click" to mapOf<String, Any?>("folderName" to "Dragons")),
            analytics.events,
        )
    }

    @Test
    fun `recently viewed monster click is tracked with the monster index`() {
        homeAnalytics.trackIntent(HomeIntent.OpenMonsterDetail(index = "aboleth"))

        assertEquals(
            listOf(
                "Home - recently viewed monster click" to mapOf<String, Any?>(
                    "monsterIndex" to "aboleth"
                )
            ),
            analytics.events,
        )
    }

    @Test
    fun `sections loaded is tracked with the sections order and totals`() {
        homeAnalytics.trackSectionsLoaded(
            sections = listOf(
                HomeSectionState.Search,
                HomeSectionState.Categories(
                    categories = listOf(
                        HomeCategoryState(type = HomeCategoryType.CREATURES, total = 300),
                        HomeCategoryState(type = HomeCategoryType.SPELLS, total = 400),
                    ),
                ),
                HomeSectionState.ExtraContent(added = 0, total = 8),
                HomeSectionState.Create,
            ),
            isReload = true,
        )

        assertEquals(
            listOf(
                "Home - sections loaded" to mapOf<String, Any?>(
                    "sections" to "Search, Categories, ExtraContent, Create",
                    "isReload" to true,
                    "monsters" to 300,
                    "spells" to 400,
                    "folders" to null,
                    "recentlyViewed" to null,
                    "extraContentAdded" to 0,
                    "extraContentTotal" to 8,
                )
            ),
            analytics.events,
        )
    }

    private class FakeAnalytics : Analytics {

        val events = mutableListOf<Pair<String, Map<String, Any?>>>()

        override fun track(eventName: String, params: Map<String, Any?>) {
            events.add(eventName to params)
        }

        override fun setUserProperty(name: String, value: Any) {}

        override fun getDeviceId(): String? = null

        override fun logException(throwable: Throwable) {}
    }
}
