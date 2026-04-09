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

import br.alexandregpereira.hunter.localization.Language

interface FolderListStrings {
    val title: String
    val delete: String
    val addToPreview: String
    val itemSelected: (Int) -> String
    val emptyScreenTitle: String
    val emptyScreenDescription: String
}

internal data class FolderListEnStrings(
    override val title: String = "Folders",
    override val delete: String = "Delete",
    override val addToPreview: String = "Add to Preview",
    override val itemSelected: (Int) -> String = { count ->
        if (count == 1) {
            "$count item selected"
        } else {
            "$count items selected"
        }
    },
    override val emptyScreenTitle: String = "No folders found",
    override val emptyScreenDescription: String = "Create a folder by selecting a creature and adding it to a new folder. You can also select multiple creatures by long pressing a creature card and adding them to a new folder.",
) : FolderListStrings

internal data class FolderListPtStrings(
    override val title: String = "Pastas",
    override val delete: String = "Deletar",
    override val addToPreview: String = "Adicionar ao Preview",
    override val itemSelected: (Int) -> String = { count ->
        if (count == 1) {
            "$count item selecionado"
        } else {
            "$count itens selecionados"
        }
    },
    override val emptyScreenTitle: String = "Nenhuma pasta encontrada",
    override val emptyScreenDescription: String = "Crie uma pasta selecionando uma criatura e adicionando-o a uma nova pasta. Você também pode selecionar várias criaturas pressionando e segurando a carta de uma criatura e adicionando-as a uma nova pasta.",
) : FolderListStrings

internal data class FolderListEmptyStrings(
    override val title: String = "",
    override val delete: String = "",
    override val addToPreview: String = "",
    override val itemSelected: (Int) -> String = { _ -> "" },
    override val emptyScreenTitle: String = "",
    override val emptyScreenDescription: String = "",
) : FolderListStrings

internal fun getFolderListStrings(lang: Language): FolderListStrings {
    return when (lang) {
        Language.ENGLISH -> FolderListEnStrings()
        Language.PORTUGUESE -> FolderListPtStrings()
    }
}
