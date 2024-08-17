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

package br.alexandregpereira.hunter.data.monster.lore

import br.alexandregpereira.hunter.data.monster.lore.local.MonsterLoreLocalDataSource
import br.alexandregpereira.hunter.data.monster.lore.local.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.lore.local.mapper.toEntity
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreLocalRepository
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultMonsterLoreLocalRepository(
    private val monsterLoreLocalDataSource: MonsterLoreLocalDataSource,
) : MonsterLoreLocalRepository {

    override fun getMonsterLore(index: String): Flow<MonsterLore> {
        return monsterLoreLocalDataSource.getMonsterLore(index).map { it.toDomain() }
    }

    override fun getLocalMonstersLore(indexes: List<String>): Flow<List<MonsterLore>> {
        return monsterLoreLocalDataSource.getMonstersLore(indexes).map { monsters ->
            monsters.map { it.toDomain() }
        }
    }

    override fun getMonstersLoreEdited(): Flow<List<MonsterLore>> {
        return monsterLoreLocalDataSource.getMonstersLoreEdited().map { monsters ->
            monsters.map { it.toDomain() }
        }
    }

    override fun save(monstersLore: List<MonsterLore>, isSync: Boolean): Flow<Unit> {
        return monsterLoreLocalDataSource.save(monstersLore.map { it.toEntity() }, isSync)
    }
}
