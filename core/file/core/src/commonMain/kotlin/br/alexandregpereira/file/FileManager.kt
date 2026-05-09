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

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.copyTo

interface FileManager {

    suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType
    ): String

    suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType)

    suspend fun deleteAllFilesFromAppStorage(fileType: FileType)
}

suspend fun FileManager.copyToAppStorage(
    file: FileEntry,
    name: String,
    fileType: FileType,
): FileEntry {
    val destination = PlatformFile(getAppStorageFileFolderPath(name, fileType))
    file.platformFile.copyTo(
        destination
    )

    return FileEntry(destination)
}

internal expect fun FileManager.getAppStorageFileFolderPath(fileFolder: String): String

internal fun FileManager.getAppStorageFileFolderPath(fileName: String, fileType: FileType): String {
    return getAppStorageFileFolderPath(fileFolder = fileType.folder) + "/" + fileName
}

