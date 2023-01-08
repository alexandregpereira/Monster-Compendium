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

package br.alexandregpereira.hunter.data.monster.lore.local

import br.alexandregpereira.hunter.data.monster.lore.local.dao.MonsterLoreDao
import br.alexandregpereira.hunter.data.monster.lore.local.entity.MonsterLoreCompleteEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class MonsterLoreLocalDataSource @Inject constructor(
    private val monsterLoreDao: MonsterLoreDao
) {
    private val mutex = Mutex()

    fun getMonstersLore(indexes: List<String>): Flow<List<MonsterLoreCompleteEntity>> {
        return flow {
            emit(monsterLoreDao.getMonstersLore(indexes))
        }
    }

    fun getMonsterLore(monsterIndex: String): Flow<MonsterLoreCompleteEntity> {
        return flow {
            emit(monsterLoreDao.getMonsterLore(monsterIndex))
        }
    }

    fun save(
        monsters: List<MonsterLoreCompleteEntity>,
    ): Flow<Unit> = flow {
        mutex.withLock {
            deleteAllData()
            insertAll(monsters)
        }
        emit(Unit)
    }

    fun deleteAll(): Flow<Unit> = flow {
        emit(deleteAllData())
    }

    private suspend fun deleteAllData() {
        monsterLoreDao.deleteAllEntries()
        monsterLoreDao.deleteAll()
    }

    private suspend fun insertAll(monsters: List<MonsterLoreCompleteEntity>) {
        monsterLoreDao.insert(monsters.map { it.monsterLore })
        monsterLoreDao.insertEntries(monsters.map { it.entries }.reduceList())
    }

    private fun <T> List<List<T>>.reduceList(): List<T> {
        return this.reduceOrNull { acc, list -> acc + list } ?: emptyList()
    }
}
