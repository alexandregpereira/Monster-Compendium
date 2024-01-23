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

package br.alexandregpereira.hunter.data.spell

import br.alexandregpereira.hunter.data.spell.local.SpellLocalDataSource
import br.alexandregpereira.hunter.data.spell.local.mapper.toDomain
import br.alexandregpereira.hunter.data.spell.local.mapper.toEntity
import br.alexandregpereira.hunter.domain.spell.SpellLocalRepository
import br.alexandregpereira.hunter.domain.spell.model.Spell
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultSpellLocalRepository(
    private val localDataSource: SpellLocalDataSource
) : SpellLocalRepository {

    override fun saveSpells(spells: List<Spell>): Flow<Unit> {
        return localDataSource.saveSpells(spells.toEntity())
    }

    override fun getLocalSpell(index: String): Flow<Spell> {
        return localDataSource.getSpell(index).map { it.toDomain() }
    }

    override fun getLocalSpells(indexes: List<String>): Flow<List<Spell>> {
        return localDataSource.getSpells(indexes).map { spells ->
            spells.map { it.toDomain() }
        }
    }

    override fun getLocalSpells(): Flow<List<Spell>> {
        return localDataSource.getSpells().map { spells ->
            spells.map { it.toDomain() }
        }
    }

    override fun deleteLocalSpells(): Flow<Unit> {
        return localDataSource.deleteSpells()
    }
}
