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

package br.alexandregpereira.hunter.data.monster.folder.local

import br.alexandregpereira.hunter.data.monster.folder.local.dao.MonsterFolderDao
import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderCompleteEntity
import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderEntity
import br.alexandregpereira.hunter.data.monster.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock

internal class DefaultMonsterFolderLocalDataSource(
    private val monsterFolderDao: MonsterFolderDao,
    private val monsterDao: MonsterDao
) : MonsterFolderLocalDataSource {

    override fun addMonsters(folderName: String, monsterIndexes: List<String>): Flow<Unit> = flow {
        val timeInMillis = Clock.System.now().toEpochMilliseconds()

        monsterFolderDao.addMonstersToFolder(
            monsterIndexes.mapIndexed { i, monsterIndex ->
                MonsterFolderEntity(
                    folderName = folderName,
                    monsterIndex = monsterIndex,
                    createdAt = timeInMillis + i
                )
            }
        )
        emit(Unit)
    }

    override fun getMonsterFolders(): Flow<List<MonsterFolderCompleteEntity>> = flow {
        emit(monsterFolderDao.getMonsterFolders().asMonsterFolderCompleteEntity())
    }

    override fun getMonstersFromFolder(
        folderName: String
    ): Flow<MonsterFolderCompleteEntity?> = flow {
        val value = monsterFolderDao.getMonstersFromFolder(folderName)
            .asMonsterFolderCompleteEntity()
            .firstOrNull()

        emit(value)
    }

    override fun removeMonsters(
        folderName: String,
        monsterIndexes: List<String>
    ): Flow<Unit> = flow {
        emit(
            monsterFolderDao.removeMonsterFromFolder(
                folderName = folderName,
                monsterIndexes = monsterIndexes
            )
        )
    }

    private fun Map<MonsterFolderEntity, List<MonsterEntity>>.asMonsterFolderCompleteEntity():
            List<MonsterFolderCompleteEntity> = map { entry ->
        MonsterFolderCompleteEntity(monsterFolderEntity = entry.key, monsters = entry.value)
    }

    override fun getFolderMonsterPreviewsByIds(
        monsterIndexes: List<String>
    ): Flow<List<MonsterEntity>> = flow {
        emit(monsterDao.getMonsterPreviews(monsterIndexes))
    }

    override fun removeMonsterFolders(folderNames: List<String>): Flow<Unit> = flow {
        emit(monsterFolderDao.removeMonsterFolders(folderNames))
    }

    override fun getMonstersFromFolders(foldersName: List<String>): Flow<List<MonsterEntity>> = flow {
        emit(monsterFolderDao.getMonstersFromFolders(foldersName))
    }
}
