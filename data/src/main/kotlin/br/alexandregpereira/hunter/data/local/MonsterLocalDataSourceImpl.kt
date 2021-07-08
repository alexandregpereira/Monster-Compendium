/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.local

import br.alexandregpereira.hunter.data.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.local.entity.MonsterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class MonsterLocalDataSourceImpl @Inject constructor(
    private val monsterDao: MonsterDao
) : MonsterLocalDataSource {

    override fun getMonsters(): Flow<List<MonsterEntity>> {
        return flow {
            emit(monsterDao.getMonsters())
        }
    }

    override fun saveMonsters(monsters: List<MonsterEntity>): Flow<Unit> {
        return flow {
            monsterDao.insert(monsters)
            emit(Unit)
        }
    }

    override fun deleteMonsters(): Flow<Unit> {
        return flow {
            emit(monsterDao.deleteAll())
        }
    }
}