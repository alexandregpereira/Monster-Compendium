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

import java.io.File

internal class JvmFileManager : FileManager {

    override suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType,
    ): String {
        return "file://" + filesDirectory(fileFolder = getFileFolder(fileType))
            .apply { mkdirs() }
            .let { File(it, fileName).also { f -> f.writeBytes(bytes) } }
            .absolutePath
    }

    override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {
        File(filesDirectory(getFileFolder(fileType)), fileName).delete()
    }

    override suspend fun getFileNamesFromAppStorage(): List<String> {
        val folders = getFileFolders().map {
            filesDirectory(fileFolder = it)
        }
        return folders.mapNotNull { folder ->
            folder.listFiles()
        }.fold(emptyList<File>()) { acc, list ->
            acc + list
        }.map { file ->
            file.name
        }
    }

    private fun filesDirectory(fileFolder: String): File = File(
        System.getProperty("user.home"),
        ".monster-compendium/$fileFolder"
    )
}
