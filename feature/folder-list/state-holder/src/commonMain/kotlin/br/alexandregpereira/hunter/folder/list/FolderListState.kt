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

package br.alexandregpereira.hunter.folder.list

import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder

typealias MonsterFolderWithSelection = Pair<MonsterFolder, Boolean>

data class FolderListState(
    val folders: List<MonsterFolderWithSelection> = emptyList(),
    val itemSelection: Set<String> = emptySet(),
    val isItemSelectionOpen: Boolean = false
) {
    val itemSelectionCount = itemSelection.size
    val itemSelectionEnabled = isItemSelectionOpen
}

internal fun FolderListState.changeSelectedFolders(
    folders: List<MonsterFolderWithSelection>
): FolderListState {
    return copy(
        folders = folders.map {
            val folder = it.first
            val selected = isItemSelectionOpen && itemSelection.contains(folder.name)
            folder to selected
        }
    )
}
