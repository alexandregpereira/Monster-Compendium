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

package br.alexandregpereira.hunter.data.monster.cache

import br.alexandregpereira.hunter.domain.model.Monster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MonsterCacheDataSource {

    private val monsters: MutableList<Monster> = mutableListOf()

    fun getMonsters(): Flow<List<Monster>> {
        return flow { emit(monsters.toList()) }
    }

    fun saveMonsters(monsters: List<Monster>): Flow<Unit> {
        return flow {
            this@MonsterCacheDataSource.monsters.clear()
            this@MonsterCacheDataSource.monsters.addAll(monsters)
            emit(Unit)
        }
    }

    fun clear(): Flow<Unit> {
        return flow {
            monsters.clear()
            emit(Unit)
        }
    }
}
