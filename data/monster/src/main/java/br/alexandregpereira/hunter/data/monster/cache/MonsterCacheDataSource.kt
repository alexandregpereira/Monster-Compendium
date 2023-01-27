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

package br.alexandregpereira.hunter.data.monster.cache

import br.alexandregpereira.hunter.domain.model.Monster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MonsterCacheDataSource() {

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
}
