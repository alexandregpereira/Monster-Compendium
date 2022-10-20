/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.folder.preview

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.folder.preview.domain.model.MonsterFolderPreview

internal data class FolderPreviewViewState(
    val showPreview: Boolean = false,
    val monsters: List<MonsterFolderPreview> = emptyList()
)

internal fun SavedStateHandle.getState(): FolderPreviewViewState {
    return FolderPreviewViewState(showPreview = this["showPreview"] ?: false)
}

internal fun SavedStateHandle.containsState(): Boolean {
    return this.get<Boolean>("showPreview") != null
}

internal fun FolderPreviewViewState.saveState(
    savedStateHandle: SavedStateHandle
): FolderPreviewViewState {
    savedStateHandle["showPreview"] = this.showPreview
    return this
}

internal fun FolderPreviewViewState.changeMonsters(
    monsters: List<MonsterFolderPreview>
): FolderPreviewViewState {
    return this.copy(monsters = monsters)
}

internal fun FolderPreviewViewState.changeShowPreview(
    show: Boolean,
    savedStateHandle: SavedStateHandle
): FolderPreviewViewState {
    return this.copy(showPreview = show).saveState(savedStateHandle)
}
