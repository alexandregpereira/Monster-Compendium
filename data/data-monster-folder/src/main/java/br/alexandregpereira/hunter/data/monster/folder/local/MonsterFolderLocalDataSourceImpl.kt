/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.monster.folder.local

import br.alexandregpereira.hunter.data.monster.folder.local.dao.MonsterFolderDao
import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderCompleteEntity
import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderEntity
import br.alexandregpereira.hunter.data.monster.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import java.util.Calendar
import java.util.TimeZone
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class MonsterFolderLocalDataSourceImpl(
    private val monsterFolderDao: MonsterFolderDao,
    private val monsterDao: MonsterDao
) : MonsterFolderLocalDataSource {

    override fun addMonsters(folderName: String, monsterIndexes: List<String>): Flow<Unit> = flow {
        val timeInMillis = Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis

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
}
