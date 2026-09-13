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

import br.alexandregpereira.hunter.home.domain.HomeContentTotals
import br.alexandregpereira.hunter.home.domain.HomeExtraContentProgress
import br.alexandregpereira.hunter.home.ui.HomeCategoryState
import br.alexandregpereira.hunter.home.ui.HomeCategoryType
import br.alexandregpereira.hunter.home.ui.HomeSectionState

/**
 * Builds the Home sections in their display order. A null section is not shown.
 *
 * The extra content stays right after the categories while no extra content was added, to invite
 * the user to add some. After the first one is added, it moves to the end of the Home.
 */
internal fun buildHomeSections(
    categories: HomeSectionState.Categories?,
    extraContent: HomeSectionState.ExtraContent?,
    recentlyViewed: HomeSectionState.RecentlyViewed?,
    folders: HomeSectionState.Folders?,
): List<HomeSectionState> {
    val hasExtraContentAdded = extraContent != null && extraContent.added > 0
    return buildList {
        add(HomeSectionState.Search)
        categories?.let(::add)
        if (hasExtraContentAdded.not()) extraContent?.let(::add)
        recentlyViewed?.let(::add)
        folders?.let(::add)
        add(HomeSectionState.Create)
        if (hasExtraContentAdded) extraContent?.let(::add)
    }
}

/**
 * Returns null while there is no content, like before the first sync.
 */
internal fun HomeContentTotals.toCategoriesSection(): HomeSectionState.Categories? {
    if (monsters == 0 && spells == 0) return null
    return HomeSectionState.Categories(
        categories = listOf(
            HomeCategoryState(type = HomeCategoryType.CREATURES, total = monsters),
            HomeCategoryState(type = HomeCategoryType.SPELLS, total = spells),
        ),
    )
}

internal fun HomeExtraContentProgress.toExtraContentSection(): HomeSectionState.ExtraContent? {
    if (total == 0) return null
    return HomeSectionState.ExtraContent(added = added, total = total)
}
