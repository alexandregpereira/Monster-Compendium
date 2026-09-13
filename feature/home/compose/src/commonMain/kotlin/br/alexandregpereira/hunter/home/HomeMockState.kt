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

import br.alexandregpereira.hunter.home.ui.HomeCategoryState
import br.alexandregpereira.hunter.home.ui.HomeCategoryType
import br.alexandregpereira.hunter.home.ui.HomeFolderState
import br.alexandregpereira.hunter.home.ui.HomeSectionState
import br.alexandregpereira.hunter.home.ui.HomeViewState
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.FolderImageState

/**
 * Mocked content shared by [HomeStateHolder] and the Compose previews until the Home is connected to
 * the domain layer.
 */
internal val homeMockViewState = HomeViewState(
    sections = listOf(
        HomeSectionState.Search,
        HomeSectionState.Categories(
            categories = listOf(
                HomeCategoryState(type = HomeCategoryType.CREATURES, total = 400),
                HomeCategoryState(type = HomeCategoryType.SPELLS, total = 500),
            ),
        ),
        HomeSectionState.ExtraContent(added = 0, total = 8),
        HomeSectionState.RecentlyViewed(
            monsters = listOf(
                MonsterCardState(
                    index = "aboleth",
                    name = "Aboleth",
                    imageState = mockImage(MonsterTypeState.ABERRATION, "#D9E3E0", "10"),
                ),
                MonsterCardState(
                    index = "deva",
                    name = "Deva",
                    imageState = mockImage(MonsterTypeState.CELESTIAL, "#EAE2D8", "10"),
                ),
                MonsterCardState(
                    index = "ancient-red-dragon",
                    name = "Ancient Red Dragon",
                    imageState = mockImage(MonsterTypeState.DRAGON, "#E8DCE0", "24"),
                ),
                MonsterCardState(
                    index = "lich",
                    name = "Lich",
                    imageState = mockImage(MonsterTypeState.UNDEAD, "#DCE3E8", "21"),
                ),
            ),
        ),
        HomeSectionState.Folders(
            folders = listOf(
                HomeFolderState(
                    name = "Boss Fights",
                    image1 = mockFolderImage(MonsterTypeState.DRAGON, "#E8DCE0"),
                    image2 = mockFolderImage(MonsterTypeState.FIEND, "#DADBE6"),
                    image3 = mockFolderImage(MonsterTypeState.UNDEAD, "#DCE3E8"),
                ),
                HomeFolderState(
                    name = "Goblinoids",
                    image1 = mockFolderImage(MonsterTypeState.HUMANOID, "#DDE6D6"),
                    image2 = mockFolderImage(MonsterTypeState.HUMANOID, "#E8DCE0"),
                    image3 = mockFolderImage(MonsterTypeState.BEAST, "#D6E3E6"),
                ),
                HomeFolderState(
                    name = "Undead",
                    image1 = mockFolderImage(MonsterTypeState.UNDEAD, "#DADBE6"),
                    image2 = mockFolderImage(MonsterTypeState.UNDEAD, "#E8DCE0"),
                ),
            ),
        ),
        HomeSectionState.Create,
    ),
)

/**
 * Uses the same default type images the app shows for monsters without a custom image.
 */
private fun mockImage(
    type: MonsterTypeState,
    color: String,
    challengeRating: String = "",
) = MonsterImageState(
    url = type.mockImageUrl(),
    type = type,
    backgroundColor = ColorState(light = color, dark = color),
    challengeRating = challengeRating,
    contentScale = AppImageContentScale.Fit,
)

private fun mockFolderImage(
    type: MonsterTypeState,
    color: String,
) = FolderImageState(
    url = type.mockImageUrl(),
    backgroundColorLight = color,
)

private fun MonsterTypeState.mockImageUrl(): String {
    return "$MOCK_IMAGE_BASE_URL/default-${name.lowercase()}.png"
}

private const val MOCK_IMAGE_BASE_URL =
    "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images"
