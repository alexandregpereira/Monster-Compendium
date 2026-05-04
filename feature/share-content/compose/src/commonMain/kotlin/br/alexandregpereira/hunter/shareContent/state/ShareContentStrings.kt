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

interface ShareContentStrings {
    val chooseDestinationFolder: String
    val importButton: String
    val shareButton: String
    val importTitle: String
    val exportTitle: String
    val importInvalidContentErrorMessage: String
    val pickCompendiumFile: String
}

internal data class ShareContentEnStrings(
    override val chooseDestinationFolder: String = "Choose destination folder",
    override val importButton: String = "Import",
    override val shareButton: String = "Share",
    override val importTitle: String = "Import Content",
    override val exportTitle: String = "Share Content",
    override val importInvalidContentErrorMessage: String = "Invalid content",
    override val pickCompendiumFile: String = "Pick compendium file",
) : ShareContentStrings

internal data class ShareContentPtStrings(
    override val chooseDestinationFolder: String = "Escolher pasta de destino",
    override val importButton: String = "Importar",
    override val shareButton: String = "Compartilhar",
    override val importTitle: String = "Importar Conteúdo",
    override val exportTitle: String = "Compartilhar Conteúdo",
    override val importInvalidContentErrorMessage: String = "Conteúdo inválido",
    override val pickCompendiumFile: String = "Escolher arquivo compendium",
) : ShareContentStrings

fun ShareContentStrings(): ShareContentStrings = ShareContentEnStrings()

internal data class ShareContentEsStrings(
    override val chooseDestinationFolder: String = "Elegir carpeta de destino",
    override val importButton: String = "Importar",
    override val shareButton: String = "Compartir",
    override val importTitle: String = "Importar Contenido",
    override val exportTitle: String = "Compartir Contenido",
    override val importInvalidContentErrorMessage: String = "Contenido inválido",
    override val pickCompendiumFile: String = "Elegir archivo compendium",
) : ShareContentStrings

internal fun Language.getStrings(): ShareContentStrings {
    return when (this) {
        Language.ENGLISH -> ShareContentEnStrings()
        Language.PORTUGUESE -> ShareContentPtStrings()
        Language.SPANISH -> ShareContentEsStrings()
    }
}
