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

package br.alexandregpereira.hunter.home.ui

import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compose.FolderImageState

/**
 * The Home shows the [sections] in the order of the list. A section that is not in the list is not
 * shown.
 */
internal data class HomeViewState(
    val sections: List<HomeSectionState> = emptyList(),
)

internal sealed interface HomeSectionState {

    data object Search : HomeSectionState

    data class Categories(
        val categories: List<HomeCategoryState>,
    ) : HomeSectionState

    data class RecentlyViewed(
        val monsters: List<MonsterCardState>,
    ) : HomeSectionState

    data class Folders(
        val folders: List<HomeFolderState>,
    ) : HomeSectionState

    data object Create : HomeSectionState

    data class ExtraContent(
        val added: Int,
        val total: Int,
    ) : HomeSectionState
}

internal data class HomeCategoryState(
    val type: HomeCategoryType,
    val total: Int,
)

internal enum class HomeCategoryType {
    CREATURES,
    SPELLS,
    CONDITIONS,
}

internal data class HomeFolderState(
    val name: String,
    val image1: FolderImageState,
    val image2: FolderImageState? = null,
    val image3: FolderImageState? = null,
)
