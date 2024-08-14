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

package br.alexandregpereira.hunter.data.monster.local.dao

import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntityStatus

interface MonsterDao {

    suspend fun getMonsterPreviews(): List<MonsterEntity>

    suspend fun getMonsterPreviews(indexes: List<String>): List<MonsterEntity>

    suspend fun getMonstersPreviewsEdited(): List<MonsterEntity>

    suspend fun getMonsters(): List<MonsterCompleteEntity>

    suspend fun getMonsters(indexes: List<String>): List<MonsterCompleteEntity>

    suspend fun getMonster(index: String): MonsterCompleteEntity

    suspend fun getMonstersByQuery(query: String): List<MonsterEntity>

    suspend fun insert(monsters: List<MonsterCompleteEntity>, deleteAll: Boolean)

    suspend fun deleteMonster(index: String)

    suspend fun getMonstersByStatus(status: Set<MonsterEntityStatus>): List<MonsterCompleteEntity>
}
