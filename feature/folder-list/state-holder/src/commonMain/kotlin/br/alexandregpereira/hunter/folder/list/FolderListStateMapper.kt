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

import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder

internal typealias MonsterFolderWithSelection = Pair<MonsterFolder, Boolean>

internal fun List<MonsterFolderWithSelection>.asState(): List<FolderCardState> {
    return map {
        val folder = it.first.asState()
        folder.copy(selected = it.second)
    }
}

internal fun MonsterFolder.asState(): FolderCardState {
    return FolderCardState(
        folderName = this.name,
        image1 = this.monsters.first().asState(),
        image2 = this.monsters.getOrNull(1)?.asState(),
        image3 = this.monsters.getOrNull(2)?.asState(),
    )
}

private fun MonsterPreviewFolder.asState(): FolderCardImageState {
    return FolderCardImageState(
        url = imageUrl,
        contentDescription = name,
        isHorizontalImage = isHorizontalImage,
        backgroundColorLight = backgroundColorLight,
        backgroundColorDark = backgroundColorDark,
    )
}
