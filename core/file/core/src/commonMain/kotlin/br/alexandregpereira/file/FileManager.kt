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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.time.Clock

interface FileManager {

    suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType
    ): String

    suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType)

    suspend fun getFileNamesFromAppStorage(): List<String>
}

enum class FileType(val extension: String) {
    PNG(extension = "png"),
}

suspend fun FileManager.saveImageToAppStorage(bytes: ByteArray, imageName: String): String {
    return saveFileToAppStorage(bytes, imageName, FileType.PNG)
}

internal fun getFileFolder(fileType: FileType): String {
    return when (fileType) {
        FileType.PNG -> "images"
    }
}

internal fun getFileFolders(): List<String> {
    return FileType.entries.map {
        getFileFolder(it)
    }
}

internal class FileManagerImpl internal constructor(
    private val platformFileManager: FileManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FileManager {

    private val mutex = Mutex()

    override suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType,
    ): String = withContext(dispatcher) {
        mutex.withLock {
            deleteFileFromAppStorage(fileName, fileType)
            val epochMilliSeconds = Clock.System.now().toEpochMilliseconds()
            val fileNameWithTimestamp = "$fileName-$epochMilliSeconds.${fileType.extension}"
            platformFileManager.saveFileToAppStorage(bytes, fileNameWithTimestamp, fileType)
        }
    }

    override suspend fun deleteFileFromAppStorage(
        fileName: String,
        fileType: FileType,
    ) {
        withContext(dispatcher) {
            val files = platformFileManager.getFileNamesFromAppStorage()
            files.firstOrNull { it.startsWith(fileName) }?.let {
                platformFileManager.deleteFileFromAppStorage(fileName = it, fileType)
            }
        }
    }

    override suspend fun getFileNamesFromAppStorage(): List<String> = withContext(dispatcher) {
        platformFileManager.getFileNamesFromAppStorage()
    }
}
