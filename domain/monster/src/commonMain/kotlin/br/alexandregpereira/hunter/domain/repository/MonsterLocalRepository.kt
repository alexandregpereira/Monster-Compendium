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

package br.alexandregpereira.hunter.domain.repository

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImage
import kotlinx.coroutines.flow.Flow

interface MonsterLocalRepository {

    fun saveMonsters(monsters: List<Monster>, isSync: Boolean = false): Flow<Unit>
    fun getMonsterPreviews(): Flow<List<Monster>>
    fun getMonsters(): Flow<List<Monster>>
    fun getMonsters(indexes: List<String>): Flow<List<Monster>>
    fun getMonster(index: String): Flow<Monster>
    fun getMonstersByQuery(query: String): Flow<List<Monster>>
}
