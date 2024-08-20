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
                        higherLevel = it.higherLevel,
                        status = it.status.toLong(),
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
        spellQueries.getSpellsByIds(indexes).executeAsList().map { it.asSpellEntity() }
    }

    override suspend fun getSpells(): List<SpellEntity> = withContext(dispatcher) {
        spellQueries.getSpells().executeAsList().map { it.asSpellEntity() }
    }

    override suspend fun getSpellsEdited(): List<SpellEntity> = withContext(dispatcher) {
        spellQueries.getSpellsEdited().executeAsList().map { it.asSpellEntity() }
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
            higherLevel = higherLevel,
            status = status.toInt(),
        )
    }
}
