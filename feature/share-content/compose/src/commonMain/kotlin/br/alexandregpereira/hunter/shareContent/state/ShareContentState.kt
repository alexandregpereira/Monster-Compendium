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

internal data class ShareContentState(
    val contentToImport: String = "",
    val contentToExport: String = "",
    val exportCopyButtonEnabled: Boolean = false,
    val importError: ShareContentImportError? = null,
    val strings: ShareContentStrings = ShareContentStrings(),
) {
    val exportCopyButtonText: String = strings.copyButton.takeIf { exportCopyButtonEnabled }
        ?: strings.copiedButton

    val importErrorMessage: String = importError?.let {
        when (it) {
            ShareContentImportError.InvalidContent -> strings.importInvalidContentErrorMessage
        }
    } ?: ""

    val contentToExportShort: String = contentToExport.take(1000)
        .takeIf { it.isNotBlank() }?.let { "$it..." }.orEmpty()

    val contentToImportShort: String = contentToImport.take(1000)
        .takeIf { it.isNotBlank() }
        ?.replace("\t", "")
        ?.replace("\n", "")
        ?.let { "$it..." }
        .orEmpty()
}

internal enum class ShareContentImportError {
    InvalidContent
}
