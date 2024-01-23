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

import br.alexandregpereira.hunter.domain.spell.SpellLocalRepository
import br.alexandregpereira.hunter.domain.spell.SpellRemoteRepository
import br.alexandregpereira.hunter.domain.spell.SpellRepository
import br.alexandregpereira.hunter.domain.spell.model.Spell
import kotlinx.coroutines.flow.Flow

internal class DefaultSpellRepository(
    private val localRepository: SpellLocalRepository,
    private val remoteRepository: SpellRemoteRepository
) : SpellRepository {

    override fun saveSpells(spells: List<Spell>): Flow<Unit> {
        return localRepository.saveSpells(spells)
    }

    override fun getRemoteSpells(lang: String): Flow<List<Spell>> {
        return remoteRepository.getRemoteSpells(lang)
    }

    override fun getLocalSpell(index: String): Flow<Spell> {
        return localRepository.getLocalSpell(index)
    }

    override fun getLocalSpells(indexes: List<String>): Flow<List<Spell>> {
        return localRepository.getLocalSpells(indexes)
    }

    override fun getLocalSpells(): Flow<List<Spell>> {
        return localRepository.getLocalSpells()
    }

    override fun deleteLocalSpells(): Flow<Unit> {
        return localRepository.deleteLocalSpells()
    }
}
