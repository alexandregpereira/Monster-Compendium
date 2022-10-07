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

package br.alexandregpereira.hunter.data.folder

import br.alexandregpereira.domain.folder.MonsterFolderRepository
import br.alexandregpereira.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.data.local.MonsterFolderLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class MonsterFolderRepositoryImpl @Inject constructor(
    private val monsterFolderLocalDataSource: MonsterFolderLocalDataSource
) : MonsterFolderRepository {

    override fun addMonster(folderName: String, index: String): Flow<Unit> {
        return monsterFolderLocalDataSource.addMonster(folderName, monsterIndex = index)
    }

    override fun getMonsterFolders(): Flow<List<MonsterFolder>> {
        return monsterFolderLocalDataSource.getMonsterFolders().map { it.asDomain() }
    }

    override fun getMonstersFromFolder(folderName: String): Flow<MonsterFolder?> {
        return monsterFolderLocalDataSource.getMonstersFromFolder(folderName).map { it?.asDomain() }
    }

    override fun removeMonster(folderName: String, index: String): Flow<Unit> {
        return monsterFolderLocalDataSource.removeMonster(folderName, monsterIndex = index)
    }
}
