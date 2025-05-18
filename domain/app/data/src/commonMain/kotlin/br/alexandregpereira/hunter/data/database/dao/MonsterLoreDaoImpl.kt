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
    ): MonsterLoreCompleteEntity? = withContext(dispatcher) {
        queries.getMonsterLore(monsterIndex).executeAsList().asEntities().firstOrNull()
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
