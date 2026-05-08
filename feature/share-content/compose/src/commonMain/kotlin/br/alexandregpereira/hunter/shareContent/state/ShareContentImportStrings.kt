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

package br.alexandregpereira.hunter.shareContent.state

import br.alexandregpereira.hunter.localization.Language

interface ShareContentImportStrings {
    val importButton: String
    val importTitle: String
    val importInvalidContentErrorMessage: String
    val pickCompendiumFile: String
    val extractedStrings: ShareContentExtractedStrings
}

internal data class ShareContentEnImportStrings(
    override val importButton: String = "Import",
    override val importTitle: String = "Import content",
    override val importInvalidContentErrorMessage: String = "Invalid content",
    override val pickCompendiumFile: String = "Pick compendium file",
    override val extractedStrings: ShareContentExtractedStrings = ShareContentExtractedEnStrings(),
) : ShareContentImportStrings

internal data class ShareContentPtImportStrings(
    override val importButton: String = "Importar",
    override val importTitle: String = "Importar conteúdo",
    override val importInvalidContentErrorMessage: String = "Conteúdo inválido",
    override val pickCompendiumFile: String = "Escolher arquivo compendium",
    override val extractedStrings: ShareContentExtractedStrings = ShareContentExtractedPtStrings(),
) : ShareContentImportStrings

fun ShareContentImportStrings(): ShareContentImportStrings = ShareContentEnImportStrings()

internal data class ShareContentEsImportStrings(
    override val importButton: String = "Importar",
    override val importTitle: String = "Importar contenido",
    override val importInvalidContentErrorMessage: String = "Contenido inválido",
    override val pickCompendiumFile: String = "Elegir archivo compendium",
    override val extractedStrings: ShareContentExtractedStrings = ShareContentExtractedEsStrings(),
) : ShareContentImportStrings

internal fun Language.getImportStrings(): ShareContentImportStrings {
    return when (this) {
        Language.ENGLISH -> ShareContentEnImportStrings()
        Language.PORTUGUESE -> ShareContentPtImportStrings()
        Language.SPANISH -> ShareContentEsImportStrings()
    }
}
