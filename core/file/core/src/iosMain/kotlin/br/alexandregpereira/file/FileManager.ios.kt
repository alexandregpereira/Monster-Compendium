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
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.writeToFile

@OptIn(ExperimentalForeignApi::class)
internal class IosFileManager : FileManager {

    @OptIn(BetaInteropApi::class)
    override suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType,
    ): String {
        val filesDir = filesDirectory(fileFolder = getFileFolder(fileType))
        NSFileManager.defaultManager.createDirectoryAtPath(
            filesDir,
            withIntermediateDirectories = true,
            attributes = null,
            error = null,
        )
        val filePath = "$filesDir/$fileName"
        @OptIn(ExperimentalForeignApi::class)
        bytes.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
        }.writeToFile(filePath, atomically = true)
        return "file://$filePath"
    }

    override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {
        val filePath = "${filesDirectory(fileFolder = getFileFolder(fileType))}/$fileName"
        NSFileManager.defaultManager.removeItemAtPath(filePath, error = null)
    }

    override suspend fun getFileNamesFromAppStorage(): List<String> {
        val folderPaths = getFileFolders().map {
            filesDirectory(fileFolder = it)
        }

        return folderPaths.mapNotNull { folderPath ->
            NSFileManager.defaultManager.contentsOfDirectoryAtPath(folderPath, error = null)
        }.fold(emptyList<Any?>()) { acc, list ->
            acc + list
        }.mapNotNull { file ->
            file as? String
        }
    }

    private fun filesDirectory(fileFolder: String): String {
        val documents = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory, NSUserDomainMask, true
        ).first() as String
        return "$documents/$fileFolder"
    }
}
