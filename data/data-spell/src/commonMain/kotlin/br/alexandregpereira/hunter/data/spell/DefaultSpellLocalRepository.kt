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

import br.alexandregpereira.hunter.domain.settings.SettingsSpellDataRepository
import br.alexandregpereira.hunter.domain.spell.SpellLocalRepository
import br.alexandregpereira.hunter.domain.spell.model.Spell
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//TODO Implement SQLDelight
internal class DefaultSpellLocalRepository : SpellLocalRepository, SettingsSpellDataRepository {

    private val cache = mutableListOf<Spell>()

    override fun saveSpells(spells: List<Spell>): Flow<Unit> {
        return flow {
            cache.clear()
            cache.addAll(spells)
            emit(Unit)
        }
    }

    override fun getLocalSpell(index: String): Flow<Spell> {
        return flow {
            emit(
                cache.find { it.index == index }
                    ?: throw RuntimeException("Spell with index $index not found")
            )
        }
    }

    override fun getLocalSpells(indexes: List<String>): Flow<List<Spell>> {
        return flow { emit(cache.toList()) }
    }

    override fun deleteLocalSpells(): Flow<Unit> {
        return flow {
            cache.clear()
            emit(Unit)
        }
    }

    override fun deleteData(): Flow<Unit> {
        return deleteLocalSpells()
    }
}
