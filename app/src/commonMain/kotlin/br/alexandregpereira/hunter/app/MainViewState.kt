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

package br.alexandregpereira.hunter.app

import br.alexandregpereira.hunter.app.ui.resources.Res
import br.alexandregpereira.hunter.app.ui.resources.ic_book
import br.alexandregpereira.hunter.app.ui.resources.ic_folder
import br.alexandregpereira.hunter.app.ui.resources.ic_menu
import br.alexandregpereira.hunter.app.ui.resources.ic_search
import org.jetbrains.compose.resources.DrawableResource

internal data class MainViewState(
    val bottomBarItemSelectedIndex: Int = 0,
    val bottomBarItems: List<BottomBarItem> = emptyList(),
) {

    val bottomBarItemSelected: BottomBarItem? = bottomBarItems.getOrNull(bottomBarItemSelectedIndex)
}

internal enum class BottomBarItemIcon(val value: DrawableResource) {
    COMPENDIUM(value = Res.drawable.ic_book),
    SEARCH(value = Res.drawable.ic_search),
    FOLDERS(value = Res.drawable.ic_folder),
    SETTINGS(value = Res.drawable.ic_menu)
}

internal data class BottomBarItem(
    val icon: BottomBarItemIcon = BottomBarItemIcon.COMPENDIUM,
    val text: String = "",
)
