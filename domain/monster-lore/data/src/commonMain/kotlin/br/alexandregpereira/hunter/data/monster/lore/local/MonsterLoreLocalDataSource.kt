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

package br.alexandregpereira.hunter.data.monster.lore.local

import br.alexandregpereira.hunter.data.monster.lore.local.dao.MonsterLoreDao
import br.alexandregpereira.hunter.data.monster.lore.local.entity.MonsterLoreCompleteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class MonsterLoreLocalDataSource(
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

    fun getMonstersLoreEdited(): Flow<List<MonsterLoreCompleteEntity>> {
        return flow {
            emit(monsterLoreDao.getMonstersLoreEdited())
        }
    }

    fun save(
        monsters: List<MonsterLoreCompleteEntity>,
        isSync: Boolean = false
    ): Flow<Unit> = flow {
        mutex.withLock {
            monsterLoreDao.insert(monsters, deleteAll = isSync)
        }
        emit(Unit)
    }
}
