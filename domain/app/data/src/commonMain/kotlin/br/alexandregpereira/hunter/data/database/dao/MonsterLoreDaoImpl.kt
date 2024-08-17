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

package br.alexandregpereira.hunter.data.database.dao

import br.alexandregpereira.hunter.data.monster.lore.local.dao.MonsterLoreDao
import br.alexandregpereira.hunter.data.monster.lore.local.entity.MonsterLoreCompleteEntity
import br.alexandregpereira.hunter.data.monster.lore.local.entity.MonsterLoreEntity
import br.alexandregpereira.hunter.data.monster.lore.local.entity.MonsterLoreEntityStatus
import br.alexandregpereira.hunter.data.monster.lore.local.entity.MonsterLoreEntryEntity
import br.alexandregpereira.hunter.database.MonsterLoreCompleteEntityView
import br.alexandregpereira.hunter.database.MonsterLoreEntryQueries
import br.alexandregpereira.hunter.database.MonsterLoreQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import br.alexandregpereira.hunter.database.MonsterLoreEntity as MonsterLoreDatabaseEntity
import br.alexandregpereira.hunter.database.MonsterLoreEntryEntity as MonsterLoreEntryDatabaseEntity

internal class MonsterLoreDaoImpl(
    private val queries: MonsterLoreQueries,
    private val monsterLoreEntryQueries: MonsterLoreEntryQueries,
    private val dispatcher: CoroutineDispatcher
) : MonsterLoreDao {

    override suspend fun getMonstersLore(
        indexes: List<String>
    ): List<MonsterLoreCompleteEntity> = withContext(dispatcher) {
        queries.getMonstersLore(indexes).executeAsList().asEntities()
    }

    override suspend fun getMonsterLore(
        monsterIndex: String
    ): MonsterLoreCompleteEntity = withContext(dispatcher) {
        queries.getMonsterLore(monsterIndex).executeAsList().asEntities().first()
    }

    override suspend fun getMonstersLoreEdited(): List<MonsterLoreCompleteEntity> {
        return withContext(dispatcher) {
            queries.getMonstersLoreEdited().executeAsList().asEntities()
        }
    }

    override suspend fun insert(
        monstersLore: List<MonsterLoreCompleteEntity>,
        deleteAll: Boolean
    ) = withContext(dispatcher) {
        queries.transaction {
            val monsterIndexes = if (deleteAll) {
                queries.getOriginalMonstersLore().executeAsList()
                    .map { it.monsterLoreIndex }.also { monsterIndexes ->
                        queries.deleteWithIndexes(monsterIndexes)
                    }
            } else {
                monstersLore.map { it.monsterLore.monsterLoreIndex }
            }

            monsterLoreEntryQueries.deleteWithMonsterIndexes(monsterIndexes)

            monstersLore.map { it.monsterLore }.forEach {
                queries.insert(
                    MonsterLoreDatabaseEntity(
                        monsterLoreIndex = it.monsterLoreIndex,
                        status = when (it.status) {
                            MonsterLoreEntityStatus.Original -> 0L
                            MonsterLoreEntityStatus.Imported -> 1L
                        }
                    )
                )
            }

            monstersLore.mapAndReduce { entries }.forEach {
                monsterLoreEntryQueries.insert(
                    MonsterLoreEntryDatabaseEntity(
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        monsterIndex = it.monsterIndex
                    )
                )
            }
        }
    }

    private fun List<MonsterLoreCompleteEntityView>.asEntities(): List<MonsterLoreCompleteEntity> {
        return map {
            MonsterLoreEntity(
                monsterLoreIndex = it.monsterLoreIndex,
                status = when (it.status) {
                    0L -> MonsterLoreEntityStatus.Original
                    else -> MonsterLoreEntityStatus.Imported
                },
            ) to MonsterLoreEntryEntity(
                id = it.id,
                title = it.title,
                description = it.description,
                monsterIndex = it.monsterIndex
            )
        }.groupBy(keySelector = { it.first }, valueTransform = { it.second })
            .map { (monsterLore, monsterLoreEntries) ->
                MonsterLoreCompleteEntity(
                    monsterLore = monsterLore,
                    entries = monsterLoreEntries
                )
            }
    }
}
