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

package br.alexandregpereira.hunter.folder.insert

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.folder.insert.ui.MonsterPreviewState

internal data class FolderInsertViewState(
    val isOpen: Boolean = false,
    val folderName: String = "",
    val folderIndexSelected: Int = -1,
    val monsterIndexes: List<String> = emptyList(),
    val folders: List<Pair<String, Int>> = emptyList(),
    val monsterPreviews: List<MonsterPreviewState> = emptyList()
)

internal fun SavedStateHandle.getState(): FolderInsertViewState {
    return FolderInsertViewState(
        isOpen = this["isOpen"] ?: false,
        folderName = this["folderName"] ?: "",
        folderIndexSelected = this["folderIndexPositionSelected"] ?: -1,
        monsterIndexes = this["monsterIndexes"] ?: emptyList()
    )
}

internal fun FolderInsertViewState.saveState(
    savedStateHandle: SavedStateHandle
): FolderInsertViewState {
    savedStateHandle["isOpen"] = isOpen
    savedStateHandle["folderName"] = folderName
    savedStateHandle["folderIndexSelected"] = folderIndexSelected
    savedStateHandle["monsterIndexes"] = monsterIndexes
    return this
}
