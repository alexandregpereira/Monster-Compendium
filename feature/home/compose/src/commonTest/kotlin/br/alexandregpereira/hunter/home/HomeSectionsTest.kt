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

import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import br.alexandregpereira.hunter.domain.source.model.Source
import br.alexandregpereira.hunter.home.domain.HomeContentTotals
import br.alexandregpereira.hunter.home.domain.HomeExtraContentProgress
import br.alexandregpereira.hunter.home.domain.toHomeExtraContentProgress
import br.alexandregpereira.hunter.home.ui.HomeCategoryState
import br.alexandregpereira.hunter.home.ui.HomeCategoryType
import br.alexandregpereira.hunter.home.ui.HomeSectionState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HomeSectionsTest {

    private val categories = HomeSectionState.Categories(
        categories = listOf(HomeCategoryState(type = HomeCategoryType.CREATURES, total = 1)),
    )
    private val recentlyViewed = HomeSectionState.RecentlyViewed(monsters = emptyList())
    private val folders = HomeSectionState.Folders(folders = emptyList())

    @Test
    fun `extra content without content added is placed after the categories`() {
        val extraContent = HomeSectionState.ExtraContent(added = 0, total = 8)

        val sections = buildHomeSections(
            categories = categories,
            extraContent = extraContent,
            recentlyViewed = recentlyViewed,
            folders = folders,
        )

        assertEquals(
            expected = listOf(
                HomeSectionState.Search,
                categories,
                extraContent,
                recentlyViewed,
                folders,
                HomeSectionState.Create,
            ),
            actual = sections,
        )
    }

    @Test
    fun `extra content with content added is placed at the end`() {
        val extraContent = HomeSectionState.ExtraContent(added = 1, total = 8)

        val sections = buildHomeSections(
            categories = categories,
            extraContent = extraContent,
            recentlyViewed = recentlyViewed,
            folders = folders,
        )

        assertEquals(
            expected = listOf(
                HomeSectionState.Search,
                categories,
                recentlyViewed,
                folders,
                HomeSectionState.Create,
                extraContent,
            ),
            actual = sections,
        )
    }

    @Test
    fun `sections not loaded are not shown`() {
        val sections = buildHomeSections(
            categories = null,
            extraContent = null,
            recentlyViewed = null,
            folders = null,
        )

        assertEquals(
            expected = listOf(HomeSectionState.Search, HomeSectionState.Create),
            actual = sections,
        )
    }

    @Test
    fun `categories are not shown when there is no content`() {
        assertNull(HomeContentTotals(monsters = 0, spells = 0).toCategoriesSection())
    }

    @Test
    fun `categories show the monsters and spells totals`() {
        assertEquals(
            expected = HomeSectionState.Categories(
                categories = listOf(
                    HomeCategoryState(type = HomeCategoryType.CREATURES, total = 400),
                    HomeCategoryState(type = HomeCategoryType.SPELLS, total = 500),
                ),
            ),
            actual = HomeContentTotals(monsters = 400, spells = 500).toCategoriesSection(),
        )
    }

    @Test
    fun `extra content is not shown when there is no extra content`() {
        assertNull(HomeExtraContentProgress(added = 0, total = 0).toExtraContentSection())
    }

    @Test
    fun `extra content progress ignores the default source`() {
        val sources = listOf(
            alternativeSource(acronym = "SRD", isDefault = true, isAdded = true),
            alternativeSource(acronym = "MM", isAdded = true),
            alternativeSource(acronym = "PHB", isAdded = false),
            alternativeSource(acronym = "VGM", isAdded = false),
        )

        assertEquals(
            expected = HomeExtraContentProgress(added = 1, total = 3),
            actual = sources.toHomeExtraContentProgress(),
        )
    }

    private fun alternativeSource(
        acronym: String,
        isDefault: Boolean = false,
        isAdded: Boolean = false,
    ) = AlternativeSource(
        source = Source(
            name = acronym,
            acronym = acronym,
            originalAcronym = null,
            originalName = null,
        ),
        totalMonsters = 10,
        totalSpells = 0,
        summary = "",
        coverImageUrl = "",
        isEnabled = true,
        isLoreEnabled = false,
        isDefault = isDefault,
        isAdded = isAdded,
    )
}
