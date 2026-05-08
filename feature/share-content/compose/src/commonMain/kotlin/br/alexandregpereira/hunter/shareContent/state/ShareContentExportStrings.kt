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

interface ShareContentExportStrings {
    val chooseDestinationFolder: String
    val shareButton: String
    val exportTitle: String
    val exportErrorTitle: String
    val extractedStrings: ShareContentExtractedStrings
}

internal data class ShareContentEnExportStrings(
    override val chooseDestinationFolder: String = "Choose destination folder",
    override val shareButton: String = "Share",
    override val exportTitle: String = "Share content",
    override val exportErrorTitle: String = "Something went wrong",
    override val extractedStrings: ShareContentExtractedStrings = ShareContentExtractedEnStrings(),
) : ShareContentExportStrings

internal data class ShareContentPtExportStrings(
    override val chooseDestinationFolder: String = "Escolher pasta de destino",
    override val shareButton: String = "Compartilhar",
    override val exportTitle: String = "Compartilhar conteúdo",
    override val exportErrorTitle: String = "Algo deu errado",
    override val extractedStrings: ShareContentExtractedStrings = ShareContentExtractedPtStrings(),
) : ShareContentExportStrings

fun ShareContentExportStrings(): ShareContentExportStrings = ShareContentEnExportStrings()

internal data class ShareContentEsExportStrings(
    override val chooseDestinationFolder: String = "Elegir carpeta de destino",
    override val shareButton: String = "Compartir",
    override val exportTitle: String = "Compartir contenido",
    override val exportErrorTitle: String = "Algo salió mal",
    override val extractedStrings: ShareContentExtractedStrings = ShareContentExtractedEsStrings(),
) : ShareContentExportStrings

internal fun Language.getExportStrings(): ShareContentExportStrings {
    return when (this) {
        Language.ENGLISH -> ShareContentEnExportStrings()
        Language.PORTUGUESE -> ShareContentPtExportStrings()
        Language.SPANISH -> ShareContentEsExportStrings()
    }
}
