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

package br.alexandregpereira.hunter.folder.insert

data class FolderInsertState(
    val isOpen: Boolean = false,
    val folderName: String = "",
    val folderIndexSelected: Int = -1,
    val monsterIndexes: List<String> = emptyList(),
    val folders: List<Pair<String, Int>> = emptyList(),
    val monsterPreviews: List<MonsterPreviewState> = emptyList(),
    val strings: FolderInsertStrings = FolderInsertEmptyStrings(),
)

data class MonsterPreviewState(
    val index: String,
    val name: String = "",
    val imageUrl: String = "",
    val backgroundColorLight: String,
    val backgroundColorDark: String
)
