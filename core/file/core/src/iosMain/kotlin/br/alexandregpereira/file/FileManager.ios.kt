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

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.writeToFile

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal class IosFileManager(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FileManager {

    override suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType,
    ): String = withContext(dispatcher) {
        val filesDir = filesDirectory(fileFolder = fileType.folder)
        createDirectoryAtPath(filesDir)
        val filePath = "$filesDir/$fileName"
        @OptIn(ExperimentalForeignApi::class)
        bytes.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
        }.writeToFile(filePath, atomically = true)
        "file://$filePath"
    }

    override suspend fun createZipFile(
        zipEntryFiles: List<FileEntry>,
        zipFileName: String,
    ): String = withContext(dispatcher) {
        val zipBytes = createStoreZip(zipEntryFiles)
        saveFileToAppStorage(
            bytes = zipBytes,
            fileName = zipFileName,
            fileType = FileType.ZIP,
        )
    }

    override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {
        withContext(dispatcher) {
            val filePath = "${filesDirectory(fileFolder = fileType.folder)}/$fileName"
            NSFileManager.defaultManager.removeItemAtPath(filePath, error = null)
        }
    }

    override suspend fun deleteAllsFilesFromAppStorage(
        fileType: FileType
    ): Unit = withContext(dispatcher) {
        val folderPath = filesDirectory(fileFolder = fileType.folder)
        NSFileManager.defaultManager.removeItemAtPath(folderPath, error = null)
    }

    override suspend fun getFileFromAppStorage(filePath: String): ByteArray = withContext(dispatcher) {
        val path = filePath.removePrefix("file://")
        val data = NSFileManager.defaultManager.contentsAtPath(path)
            ?: error("File not found: $path")
        ByteArray(data.length.toInt()).also { bytes ->
            bytes.usePinned { pinned ->
                platform.posix.memcpy(pinned.addressOf(0), data.bytes, data.length)
            }
        }
    }

    override suspend fun extractZipFile(bytes: ByteArray): List<FileEntry> = withContext(dispatcher) {
        extractStoreZip(bytes)
    }

    override suspend fun getFileNamesFromAppStorage(
        fileType: FileType,
    ): List<String> = withContext(dispatcher) {
        val folderPath = filesDirectory(fileFolder = fileType.folder)
        NSFileManager.defaultManager.contentsOfDirectoryAtPath(
            folderPath, error = null
        )?.mapNotNull { file ->
            file as? String
        }.orEmpty()
    }

    private fun filesDirectory(fileFolder: String): String {
        val documents = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory, NSUserDomainMask, true
        ).first() as String
        return "$documents/$fileFolder"
    }

    private fun createDirectoryAtPath(path: String) {
        NSFileManager.defaultManager.createDirectoryAtPath(
            path,
            withIntermediateDirectories = true,
            attributes = null,
            error = null,
        )
    }
}
