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

import br.alexandregpereira.hunter.domain.folder.GetMonstersByTemporaryFolderUseCase.Companion.TEMPORARY_FOLDER_NAME
import kotlinx.coroutines.flow.Flow

class RemoveMonstersFromTemporaryFolderUseCase(
    private val monsterFolderRepository: MonsterFolderRepository,
) {

    operator fun invoke(indexes: List<String>): Flow<Unit> {
        return monsterFolderRepository.removeMonsters(TEMPORARY_FOLDER_NAME, indexes = indexes)
    }
}
