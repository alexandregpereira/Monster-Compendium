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

/**
 * Mocked content shared by [HomeStateHolder] and the Compose previews until the Home is connected to
 * the domain layer.
 */
internal val homeMockViewState = HomeViewState(
    hasUnreadNotifications = true,
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
                    images = listOf(
                        mockImage(MonsterTypeState.DRAGON, "#E8DCE0"),
                        mockImage(MonsterTypeState.FIEND, "#DADBE6"),
                        mockImage(MonsterTypeState.UNDEAD, "#DCE3E8"),
                    ),
                ),
                HomeFolderState(
                    name = "Goblinoids",
                    images = listOf(
                        mockImage(MonsterTypeState.HUMANOID, "#DDE6D6"),
                        mockImage(MonsterTypeState.HUMANOID, "#E8DCE0"),
                        mockImage(MonsterTypeState.BEAST, "#D6E3E6"),
                    ),
                ),
                HomeFolderState(
                    name = "Undead",
                    images = listOf(
                        mockImage(MonsterTypeState.UNDEAD, "#DADBE6"),
                        mockImage(MonsterTypeState.UNDEAD, "#E8DCE0"),
                    ),
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
    url = "$MOCK_IMAGE_BASE_URL/default-${type.name.lowercase()}.png",
    type = type,
    backgroundColor = ColorState(light = color, dark = color),
    challengeRating = challengeRating,
    contentScale = AppImageContentScale.Fit,
)

private const val MOCK_IMAGE_BASE_URL =
    "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images"
