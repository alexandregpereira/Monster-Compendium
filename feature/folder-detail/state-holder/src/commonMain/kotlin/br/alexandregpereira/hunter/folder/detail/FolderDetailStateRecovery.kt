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

package br.alexandregpereira.hunter.folder.detail

import br.alexandregpereira.hunter.ui.StateRecovery

internal fun StateRecovery.getState(): FolderDetailState {
    return FolderDetailState(
        isOpen = this["folderDetail:isOpen"] as? Boolean ?: false,
        folderName = this["folderDetail:folderName"] as? String ?: ""
    )
}

internal fun FolderDetailState.saveState(stateRecovery: StateRecovery): FolderDetailState {
    stateRecovery["folderDetail:isOpen"] = isOpen
    stateRecovery["folderDetail:folderName"] = folderName
    stateRecovery.dispatchChanges()
    return this
}
