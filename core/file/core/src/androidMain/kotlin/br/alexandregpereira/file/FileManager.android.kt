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

import android.app.Application
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class AndroidFileManager(
    private val app: Application,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FileManager {

    override suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType
    ): String = withContext(dispatcher) {
        val dir = filesDirectory(fileFolder = fileType.folder).apply { mkdirs() }
        "file://" + File(dir, fileName).also {
            it.writeBytes(bytes)
        }.absolutePath
    }

    override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {
        withContext(dispatcher) {
            val dir = filesDirectory(fileFolder = fileType.folder)
            File(dir, fileName).delete()
        }
    }

    override suspend fun deleteAllFilesFromAppStorage(
        fileType: FileType,
    ): Unit = withContext(dispatcher) {
        filesDirectory(fileFolder = fileType.folder).deleteRecursively()
    }

    internal fun filesDirectory(fileFolder: String): File = File(
        app.filesDir,
        fileFolder,
    )
}

internal actual fun FileManager.getAppStorageFileFolderPath(fileFolder: String): String {
    return (this as AndroidFileManager).filesDirectory(fileFolder).also {
        it.mkdirs()
    }.absolutePath
}
