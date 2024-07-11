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

package br.alexandregpereira.hunter.data.monster.local

import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import kotlinx.coroutines.flow.Flow

internal interface MonsterLocalDataSource {

    fun getMonsterPreviews(): Flow<List<MonsterEntity>>
    fun getMonsterPreviewsEdited(): Flow<List<MonsterEntity>>
    fun getMonsters(): Flow<List<MonsterCompleteEntity>>
    fun getMonsters(indexes: List<String>): Flow<List<MonsterCompleteEntity>>
    fun getMonstersByQuery(query: String): Flow<List<MonsterEntity>>
    fun saveMonsters(monsters: List<MonsterCompleteEntity>, isSync: Boolean): Flow<Unit>
    fun getMonster(index: String): Flow<MonsterCompleteEntity>
    fun deleteMonster(index: String): Flow<Unit>
}
