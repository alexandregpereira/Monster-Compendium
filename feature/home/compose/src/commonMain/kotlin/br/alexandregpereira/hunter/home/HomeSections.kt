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

import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderImageContentScale
import br.alexandregpereira.hunter.home.domain.HomeContentTotals
import br.alexandregpereira.hunter.home.domain.HomeExtraContentProgress
import br.alexandregpereira.hunter.home.ui.HomeCategoryState
import br.alexandregpereira.hunter.home.ui.HomeCategoryType
import br.alexandregpereira.hunter.home.ui.HomeFolderState
import br.alexandregpereira.hunter.home.ui.HomeSectionState
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.FolderImageState

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
        folders?.let(::add)
        recentlyViewed?.let(::add)
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

/**
 * Keeps the folders in the same order of the folder list screen, limited to [HOME_FOLDERS_LIMIT].
 * Returns null when there is no folder to show.
 */
internal fun List<MonsterFolder>.toFoldersSection(): HomeSectionState.Folders? {
    val folders = filter { it.monsters.isNotEmpty() }
        .take(HOME_FOLDERS_LIMIT)
        .map { it.toHomeFolderState() }
    if (folders.isEmpty()) return null
    return HomeSectionState.Folders(folders = folders)
}

private fun MonsterFolder.toHomeFolderState(): HomeFolderState {
    return HomeFolderState(
        name = name,
        image1 = monsters.first().toFolderImageState(),
        image2 = monsters.getOrNull(1)?.toFolderImageState(),
        image3 = monsters.getOrNull(2)?.toFolderImageState(),
    )
}

private fun MonsterPreviewFolder.toFolderImageState(): FolderImageState {
    return FolderImageState(
        url = imageUrl,
        contentDescription = name,
        isHorizontalImage = isHorizontalImage,
        backgroundColorLight = backgroundColorLight,
        backgroundColorDark = backgroundColorDark,
    )
}

internal const val HOME_FOLDERS_LIMIT = 5

/**
 * Keeps the recently viewed order, the most recent first. Returns null when no monster was viewed.
 */
internal fun List<MonsterPreviewFolder>.toRecentlyViewedSection(): HomeSectionState.RecentlyViewed? {
    if (isEmpty()) return null
    return HomeSectionState.RecentlyViewed(monsters = map { it.toMonsterCardState() })
}

private fun MonsterPreviewFolder.toMonsterCardState(): MonsterCardState {
    return MonsterCardState(
        index = index,
        name = name,
        imageState = MonsterImageState(
            url = imageUrl,
            type = MonsterTypeState.valueOf(type.name),
            backgroundColor = ColorState(
                light = backgroundColorLight,
                dark = backgroundColorDark,
            ),
            challengeRating = challengeRating,
            contentScale = when (imageContentScale) {
                MonsterPreviewFolderImageContentScale.Fit -> AppImageContentScale.Fit
                MonsterPreviewFolderImageContentScale.Crop -> AppImageContentScale.Crop
            },
            isHorizontal = isHorizontalImage,
            contentDescription = name,
        ),
    )
}
