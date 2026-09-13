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

package br.alexandregpereira.hunter.domain.folder

import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

/**
 * The recently viewed monsters are stored in a hidden monster folder. Adding a monster again moves it
 * to the top, since the folder entries are ordered by the last time they were saved.
 */
internal const val RECENTLY_VIEWED_FOLDER_NAME = "RECENTLY_VIEWED_FOLDER"

const val RECENTLY_VIEWED_MONSTERS_LIMIT = 10

class AddMonsterToRecentlyViewedUseCase(
    private val repository: MonsterFolderRepository,
) {

    operator fun invoke(monsterIndex: String): Flow<Unit> {
        return repository.addMonsters(RECENTLY_VIEWED_FOLDER_NAME, listOf(monsterIndex))
            .map { removeMonstersAfterLimit() }
    }

    private suspend fun removeMonstersAfterLimit() {
        val indexesToRemove = repository.getMonstersFromFolder(RECENTLY_VIEWED_FOLDER_NAME)
            .first()
            ?.monsters
            .orEmpty()
            .drop(RECENTLY_VIEWED_MONSTERS_LIMIT)
            .map { it.index }
        if (indexesToRemove.isNotEmpty()) {
            repository.removeMonsters(RECENTLY_VIEWED_FOLDER_NAME, indexesToRemove).single()
        }
    }
}

class GetRecentlyViewedMonstersUseCase(
    private val getMonstersByFolder: GetMonstersByFolderUseCase,
) {

    /**
     * Returns the recently viewed monsters, the most recent first.
     */
    operator fun invoke(): Flow<List<MonsterPreviewFolder>> {
        return getMonstersByFolder(RECENTLY_VIEWED_FOLDER_NAME)
            .map { monsters -> monsters.take(RECENTLY_VIEWED_MONSTERS_LIMIT) }
    }
}
