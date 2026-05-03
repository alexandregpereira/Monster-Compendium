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

package br.alexandregpereira.file

interface FileManager {

    suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType
    ): String

    suspend fun createZipFile(
        zipEntryFiles: List<ZipFile>,
        zipFileName: String,
    ): String

    suspend fun getFileFromAppStorage(filePath: String): ByteArray

    suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType)

    suspend fun deleteAllsFilesFromAppStorage(fileType: FileType)

    suspend fun getFileNamesFromAppStorage(fileType: FileType): List<String>
}

class ZipFile(
    val name: String,
    val content: ByteArray,
)

enum class FileType(
    val folder: String
) {
    IMAGE(folder = "images"),
    ZIP(folder = "content"),
}
