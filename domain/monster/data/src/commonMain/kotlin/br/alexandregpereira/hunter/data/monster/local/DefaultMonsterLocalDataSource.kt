/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster.local

import br.alexandregpereira.hunter.data.monster.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntityStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class DefaultMonsterLocalDataSource(
    private val monsterDao: MonsterDao,
) : MonsterLocalDataSource {

    private val mutex = Mutex()

    override fun getMonsterPreviews(): Flow<List<MonsterEntity>> = flow {
        mutex.withLock {
            monsterDao.getMonsterPreviews()
        }.let { monsters ->
            emit(monsters)
        }
    }

    override fun getMonsterPreviewsEdited(): Flow<List<MonsterEntity>> = flow {
        emit(mutex.withLock { monsterDao.getMonstersPreviewsEdited() })
    }

    override fun getMonsters(): Flow<List<MonsterCompleteEntity>> = flow {
        mutex.withLock {
            monsterDao.getMonsters()
        }.let { monsters ->
            emit(monsters)
        }
    }

    override fun getMonsters(indexes: List<String>): Flow<List<MonsterCompleteEntity>> = flow {
        emit(monsterDao.getMonsters(indexes))
    }

    override fun getMonster(index: String): Flow<MonsterCompleteEntity> = flow {
        emit(monsterDao.getMonster(index))
    }

    override fun getMonstersByQuery(query: String): Flow<List<MonsterEntity>> = flow {
        mutex.withLock {
            monsterDao.getMonstersByQuery("SELECT * FROM MonsterEntity WHERE $query")
        }.let { monsters ->
            emit(monsters)
        }
    }

    override fun saveMonsters(
        monsters: List<MonsterCompleteEntity>,
        isSync: Boolean
    ): Flow<Unit> = flow {
        mutex.withLock {
            monsterDao.insert(monsters, deleteAll = isSync)
        }
        emit(Unit)
    }

    override fun deleteMonster(index: String): Flow<Unit> = flow {
        emit(monsterDao.deleteMonster(index))
    }

    override fun getMonstersByStatus(
        status: Set<MonsterEntityStatus>
    ): Flow<List<MonsterCompleteEntity>> = flow {
        emit(monsterDao.getMonstersByStatus(status))
    }
}
