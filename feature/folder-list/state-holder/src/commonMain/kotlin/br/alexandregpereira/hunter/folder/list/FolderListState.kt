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
