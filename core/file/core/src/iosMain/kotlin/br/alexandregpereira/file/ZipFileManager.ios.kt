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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal class IosZipFileManager(
    private val fileManager: FileManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ZipFileManager {

    override suspend fun createZipFile(
        zipEntryFiles: List<FileEntry>,
        zipFileName: String,
    ): String = withContext(dispatcher) {
        val zipBytes = createStoreZip(zipEntryFiles)
        fileManager.saveFileToAppStorage(
            bytes = zipBytes,
            fileName = zipFileName,
            fileType = FileType.COMPENDIUM,
        )
    }

    override suspend fun extractZipFile(
        bytes: ByteArray
    ): List<FileEntry> = withContext(dispatcher) {
        extractStoreZip(bytes)
    }
}
