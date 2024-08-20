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
