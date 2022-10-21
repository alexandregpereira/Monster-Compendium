/*
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

package br.alexandregpereira.hunter.data.monster.folder.local

import br.alexandregpereira.hunter.data.monster.folder.local.dao.MonsterFolderDao
import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderCompleteEntity
import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class MonsterFolderLocalDataSourceImpl @Inject constructor(
    private val monsterFolderDao: MonsterFolderDao
) : MonsterFolderLocalDataSource {

    override fun addMonster(folderName: String, monsterIndex: String): Flow<Unit> = flow {
        monsterFolderDao.addMonsterToFolder(
            MonsterFolderEntity(
                folderName = folderName,
                monsterIndex = monsterIndex,
                createdAt = Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis
            )
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

    override fun removeMonster(folderName: String, monsterIndex: String): Flow<Unit> = flow {
        emit(monsterFolderDao.removeMonsterFromFolder(folderName, monsterIndex))
    }

    private fun Map<MonsterFolderEntity, List<MonsterEntity>>.asMonsterFolderCompleteEntity():
            List<MonsterFolderCompleteEntity> = map { entry ->
        MonsterFolderCompleteEntity(entry.key, entry.value)
    }
}
