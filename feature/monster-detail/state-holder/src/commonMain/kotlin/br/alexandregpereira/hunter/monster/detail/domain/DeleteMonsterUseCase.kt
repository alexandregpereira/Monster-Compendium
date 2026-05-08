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

package br.alexandregpereira.hunter.monster.detail.domain

import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.FileType
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun interface DeleteMonsterUseCase {
    operator fun invoke(id: String): Flow<Unit>
}

internal fun DeleteMonsterUseCase(
    repository: MonsterLocalRepository,
    fileManager: FileManager,
): DeleteMonsterUseCase = DeleteMonsterUseCase { id ->
    flow {
        emit(repository.getMonsterPreview(id))
    }.map { monster ->
        repository.deleteMonster(id).first().also {
            val fileName = monster?.imageData?.url
                ?.takeIf { it.startsWith("file://") }
                ?.substringAfterLast("/")
            if (fileName != null) {
                fileManager.deleteFileFromAppStorage(
                    fileName = fileName,
                    fileType = FileType.IMAGE,
                )
            }
        }
    }
}
