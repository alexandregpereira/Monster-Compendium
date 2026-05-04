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

internal data class ShareContentImportState(
    val isOpen: Boolean = false,
    val isLoading: Boolean = false,
    val contentToImport: String = "",
    val importExtractedState: ShareContentImportExtractedState? = null,
    val importError: ShareContentImportError? = null,
    val strings: ShareContentStrings = ShareContentStrings(),
) {
    val importErrorMessage: String = importError?.let {
        when (it) {
            ShareContentImportError.InvalidContent -> strings.importInvalidContentErrorMessage
        }
    } ?: ""
}

internal data class ShareContentImportExtractedState(
    val contentSize: String = "",
    val monsterQuantity: String = "",
    val monsterLoreQuantity: String = "",
    val spellQuantity: String = "",
)

internal enum class ShareContentImportError {
    InvalidContent
}
