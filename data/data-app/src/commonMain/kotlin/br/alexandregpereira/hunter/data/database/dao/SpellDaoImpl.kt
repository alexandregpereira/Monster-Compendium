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

import br.alexandregpereira.hunter.data.spell.local.dao.SpellDao
import br.alexandregpereira.hunter.data.spell.local.model.SpellEntity
import br.alexandregpereira.hunter.database.SpellQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import br.alexandregpereira.hunter.database.SpellEntity as SpellDatabaseEntity

internal class SpellDaoImpl(
    private val spellQueries: SpellQueries,
    private val dispatcher: CoroutineDispatcher
) : SpellDao {

    override suspend fun insert(entities: List<SpellEntity>) = withContext(dispatcher) {
        spellQueries.transaction {
            entities.forEach {
                spellQueries.insert(
                    SpellDatabaseEntity(
                        spellIndex = it.spellIndex,
                        name = it.name,
                        level = it.level.toLong(),
                        castingTime = it.castingTime,
                        components = it.components,
                        duration = it.duration,
                        range = it.range,
                        ritual = if (it.ritual) 1L else 0L,
                        concentration = if (it.concentration) 1L else 0L,
                        savingThrowType = it.savingThrowType,
                        damageType = it.damageType,
                        school = it.school,
                        description = it.description,
                        higherLevel = it.higherLevel
                    )
                )
            }
        }
    }

    override suspend fun getSpell(index: String): SpellEntity = withContext(dispatcher) {
        spellQueries.getSpell(index).executeAsOne().asSpellEntity()
    }

    override suspend fun deleteAll() = withContext(dispatcher) {
        spellQueries.deleteAll()
    }

    override suspend fun getSpells(
        indexes: List<String>
    ): List<SpellEntity> = withContext(dispatcher) {
        spellQueries.getSpells(indexes).executeAsList().map { it.asSpellEntity() }
    }

    private fun SpellDatabaseEntity.asSpellEntity(): SpellEntity {
        return SpellEntity(
            spellIndex = spellIndex,
            name = name,
            level = level.toInt(),
            castingTime = castingTime,
            components = components,
            duration = duration,
            range = range,
            ritual = ritual == 1L,
            concentration = concentration == 1L,
            savingThrowType = savingThrowType,
            damageType = damageType,
            school = school,
            description = description,
            higherLevel = higherLevel
        )
    }
}
