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

import br.alexandregpereira.hunter.ui.StateRecovery

internal fun StateRecovery.getState(): FolderListState {
    return FolderListState(
        isItemSelectionOpen = this["folderList:isItemSelectionOpen"] as? Boolean ?: false,
        itemSelectionCount = this["folderList:itemSelectionCount"] as? Int ?: 0,
        folders = (this["folderList:selectedFolders"] as? Set<*>)?.map {
            FolderCardState(
                folderName = it as String,
                selected = true,
            )
        } ?: emptyList(),
    )
}

internal fun FolderListState.saveState(stateRecovery: StateRecovery): FolderListState {
    stateRecovery["folderList:isItemSelectionOpen"] = isItemSelectionOpen
    stateRecovery["folderList:itemSelectionCount"] = itemSelectionCount
    stateRecovery["folderList:selectedFolders"] = itemSelection
    stateRecovery.dispatchChanges()
    return this
}
