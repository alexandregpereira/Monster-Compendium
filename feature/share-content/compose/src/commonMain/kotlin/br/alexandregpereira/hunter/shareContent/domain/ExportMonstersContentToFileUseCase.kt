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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

internal class ExportMonstersContentToFileUseCase(
    private val getMonstersContentToExport: GetMonstersContentToExport,
    private val fileManager: FileManager,
) : ExportMonstersContentToFile {

    override suspend fun invoke(
        monsterIndexes: List<String>
    ): String {
        return getMonstersContentToExport(monsterIndexes).map { contentJson ->
            val timestamp = Clock.System.now().epochSeconds
            fileManager.createZipFile(
                content = contentJson,
                jsonEntryName = "content-$timestamp.json",
                zipFileName = "content-$timestamp.compendium",
            )
        }.first()
    }
}
