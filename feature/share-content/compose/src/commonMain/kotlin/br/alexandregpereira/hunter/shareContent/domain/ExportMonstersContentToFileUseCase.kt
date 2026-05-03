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

package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.ZipFile
import kotlin.time.Clock

internal class ExportMonstersContentToFileUseCase(
    private val fileManager: FileManager,
) : ExportMonstersContentToFile {

    override suspend fun invoke(
        contentToExport: ContentToExport,
    ): String {
        val timestamp = Clock.System.now().epochSeconds
        val jsonEntry = ZipFile(
            name = "content-$timestamp.json",
            content = contentToExport.contentJson.encodeToByteArray(),
        )
        val imageEntries = contentToExport.monsterImagePaths.mapNotNull { path ->
            val image = runCatching {
                fileManager.getFileFromAppStorage(path)
            }.getOrNull()
            image?.let {
                ZipFile(
                    name = path.substringAfterLast('/'),
                    content = it,
                )
            }
        }
        return fileManager.createZipFile(
            zipEntryFiles = listOf(jsonEntry) + imageEntries,
            zipFileName = "content-$timestamp.compendium",
        )
    }
}
