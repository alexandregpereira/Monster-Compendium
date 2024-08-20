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

package br.alexandregpereira.hunter.folder.list

data class FolderListState(
    val folders: List<FolderCardState> = emptyList(),
    val strings: FolderListStrings = FolderListEmptyStrings(),
    val isItemSelectionOpen: Boolean = false,
    val itemSelectionCount: Int = 0,
    val firstVisibleItemIndex: Int = 0,
    val firstVisibleItemScrollOffset: Int = 0,
) {

    internal val itemSelection: Set<String> = folders
        .filter { it.selected }
        .map { it.folderName }
        .toSet()

    internal val itemSelectionEnabled = isItemSelectionOpen
}

data class FolderCardState(
    val folderName: String = "",
    val image1: FolderCardImageState = FolderCardImageState(),
    val image2: FolderCardImageState? = null,
    val image3: FolderCardImageState? = null,
    val selected: Boolean = false,
)

data class FolderCardImageState(
    val url: String = "",
    val contentDescription: String = "",
    val isHorizontalImage: Boolean = false,
    val backgroundColorLight: String = "",
    val backgroundColorDark: String = backgroundColorLight,
)
