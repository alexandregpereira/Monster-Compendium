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
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

internal class JvmFileManager(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FileManager {

    override suspend fun saveFileToAppStorage(
        bytes: ByteArray,
        fileName: String,
        fileType: FileType,
    ): String = withContext(dispatcher) {
        "file://" + filesDirectory(fileFolder = fileType.folder)
            .apply { mkdirs() }
            .let { File(it, fileName).also { f -> f.writeBytes(bytes) } }
            .absolutePath
    }

    override suspend fun createZipFile(
        zipEntryFiles: List<FileEntry>,
        zipFileName: String,
    ): String = withContext(dispatcher) {
        val folder = filesDirectory(fileFolder = FileType.COMPENDIUM.folder).apply { mkdirs() }
        val zipFile = File(folder, zipFileName)
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { zos ->
            zipEntryFiles.forEach {
                zos.putNextEntry(ZipEntry(it.name))
                zos.write(it.content)
                zos.closeEntry()
            }
        }
        "file://${zipFile.absolutePath}"
    }

    override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {
        withContext(dispatcher) {
            File(filesDirectory(fileType.folder), fileName).delete()
        }
    }

    override suspend fun deleteAllFilesFromAppStorage(
        fileType: FileType,
    ): Unit = withContext(dispatcher) {
        filesDirectory(fileFolder = fileType.folder).deleteRecursively()
    }

    override suspend fun getFileFromAppStorage(filePath: String): FileEntry = withContext(dispatcher) {
        File(filePath.removePrefix("file://")).let {
            FileEntry(
                name = it.name,
                content = it.readBytes(),
            )
        }
    }

    override suspend fun extractZipFile(bytes: ByteArray): List<FileEntry> = withContext(dispatcher) {
        val result = mutableListOf<FileEntry>()
        ZipInputStream(bytes.inputStream()).use { zip ->
            var entry = zip.nextEntry
            while (entry != null) {
                if (!entry.isDirectory) {
                    result.add(FileEntry(name = entry.name, content = zip.readBytes()))
                }
                zip.closeEntry()
                entry = zip.nextEntry
            }
        }
        result
    }

    override suspend fun getFileNamesFromAppStorage(
        fileType: FileType,
    ): List<String> = withContext(dispatcher) {
        val folder = filesDirectory(fileFolder = fileType.folder)
        folder.listFiles()?.mapNotNull { file ->
            file.name
        }.orEmpty()
    }

    private fun filesDirectory(fileFolder: String): File = File(
        System.getProperty("user.home"),
        ".monster-compendium/$fileFolder"
    )
}
