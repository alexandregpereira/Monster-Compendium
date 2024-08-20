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

import br.alexandregpereira.hunter.localization.Language

interface FolderInsertStrings {
    val addToFolder: String
    val folderNameLabel: String
    val save: String
    val share: String
}

internal data class FolderInsertEnStrings(
    override val addToFolder: String = "Add to folder",
    override val folderNameLabel: String = "Folder name",
    override val save: String = "Save",
    override val share: String = "Share",
) : FolderInsertStrings

internal data class FolderInsertPtStrings(
    override val addToFolder: String = "Adicionar à pasta",
    override val folderNameLabel: String = "Nome da pasta",
    override val save: String = "Salvar",
    override val share: String = "Compartilhar",
) : FolderInsertStrings

internal data class FolderInsertEmptyStrings(
    override val addToFolder: String = "",
    override val folderNameLabel: String = "",
    override val save: String = "",
    override val share: String = "",
) : FolderInsertStrings

internal fun getFolderInsertStrings(lang: Language): FolderInsertStrings {
    return when (lang) {
        Language.ENGLISH -> FolderInsertEnStrings()
        Language.PORTUGUESE -> FolderInsertPtStrings()
    }
}
