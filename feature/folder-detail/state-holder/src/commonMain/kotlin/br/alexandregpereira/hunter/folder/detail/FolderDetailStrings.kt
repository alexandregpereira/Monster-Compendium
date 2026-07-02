/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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

import br.alexandregpereira.hunter.localization.Language

interface FolderDetailStrings {
    val delete: String
    val addToPreview: String
    val itemSelected: (Int) -> String
}

internal data class FolderDetailEnStrings(
    override val delete: String = "Delete",
    override val addToPreview: String = "Add to Preview",
    override val itemSelected: (Int) -> String = { count ->
        if (count == 1) "$count item selected" else "$count items selected"
    },
) : FolderDetailStrings

internal data class FolderDetailPtStrings(
    override val delete: String = "Deletar",
    override val addToPreview: String = "Adicionar ao Preview",
    override val itemSelected: (Int) -> String = { count ->
        if (count == 1) "$count item selecionado" else "$count itens selecionados"
    },
) : FolderDetailStrings

internal data class FolderDetailEsStrings(
    override val delete: String = "Eliminar",
    override val addToPreview: String = "Añadir a Vista Previa",
    override val itemSelected: (Int) -> String = { count ->
        if (count == 1) "$count elemento seleccionado" else "$count elementos seleccionados"
    },
) : FolderDetailStrings

internal data class FolderDetailEmptyStrings(
    override val delete: String = "",
    override val addToPreview: String = "",
    override val itemSelected: (Int) -> String = { _ -> "" },
) : FolderDetailStrings

internal fun getFolderDetailStrings(lang: Language): FolderDetailStrings {
    return when (lang) {
        Language.ENGLISH -> FolderDetailEnStrings()
        Language.PORTUGUESE -> FolderDetailPtStrings()
        Language.SPANISH -> FolderDetailEsStrings()
    }
}
