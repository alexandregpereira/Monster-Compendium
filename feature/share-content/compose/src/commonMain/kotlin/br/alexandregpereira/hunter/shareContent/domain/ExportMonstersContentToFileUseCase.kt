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

import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.file.FileManager
import kotlin.time.Clock

internal class ExportMonstersContentToFileUseCase(
    private val fileManager: FileManager,
    private val clock: Clock = Clock.System,
) : ExportMonstersContentToFile {

    override suspend fun invoke(
        contentToExport: ContentToExport,
    ): String {
        val jsonEntry = FileEntry(
            name = "content.json",
            content = contentToExport.contentJson.encodeToByteArray(),
        )
        val imageEntries = contentToExport.monsterImagePaths.mapNotNull { path ->
            val image = runCatching {
                fileManager.getFileFromAppStorage(path)
            }.getOrNull()
            image?.let {
                FileEntry(
                    name = path.substringAfterLast('/'),
                    content = it,
                )
            }
        }
        val timestamp = clock.now().epochSeconds
        return fileManager.createZipFile(
            zipEntryFiles = listOf(jsonEntry) + imageEntries,
            zipFileName = "content-$timestamp.compendium",
        )
    }
}
