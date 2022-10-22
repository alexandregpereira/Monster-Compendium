/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
