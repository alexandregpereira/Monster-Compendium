/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.folder.preview.domain

import br.alexandregpereira.hunter.domain.folder.GetMonstersByFolderUseCase
import br.alexandregpereira.hunter.folder.preview.domain.model.MonsterFolderPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetMonstersFromFolderPreviewUseCase @Inject constructor(
    private val getMonstersByFolder: GetMonstersByFolderUseCase
) {

    operator fun invoke(): Flow<List<MonsterFolderPreview>> {
        return getMonstersByFolder(
            folderName = TEMPORARY_FOLDER_NAME
        ).map { monsters ->
            monsters.map { monster ->
                MonsterFolderPreview(
                    index = monster.index,
                    name = monster.name,
                    imageUrl = monster.imageData.url,
                    backgroundColorLight = monster.imageData.backgroundColor.light,
                    backgroundColorDark = monster.imageData.backgroundColor.dark
                )
            }
        }
    }

    companion object {
        internal const val TEMPORARY_FOLDER_NAME = "TEMPORARY_FOLDER"
    }
}
