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

package br.alexandregpereira.hunter.data.spell

import br.alexandregpereira.hunter.data.spell.local.SpellLocalDataSource
import br.alexandregpereira.hunter.data.spell.local.mapper.toDomain
import br.alexandregpereira.hunter.data.spell.local.mapper.toEntity
import br.alexandregpereira.hunter.domain.settings.GetLanguageUseCase
import br.alexandregpereira.hunter.domain.spell.SpellLocalRepository
import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.domain.strings.StringsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

internal class DefaultSpellLocalRepository(
    private val localDataSource: SpellLocalDataSource,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val stringsRepository: StringsRepository,
) : SpellLocalRepository {

    override fun saveSpells(spells: List<Spell>): Flow<Unit> {
        return localDataSource.saveSpells(spells.toEntity())
    }

    override fun getLocalSpell(index: String): Flow<Spell> {
        return localDataSource.getSpell(index).map { it.toDomain(getStrings()) }
    }

    override fun getLocalSpells(indexes: List<String>): Flow<List<Spell>> {
        return localDataSource.getSpells(indexes).map { spells ->
            val strings = getStrings()
            spells.map { it.toDomain(strings) }
        }
    }

    override fun getLocalSpells(): Flow<List<Spell>> {
        return localDataSource.getSpells().map { spells ->
            val strings = getStrings()
            spells.map { it.toDomain(strings) }
        }
    }

    override fun getLocalSpellsEdited(): Flow<List<Spell>> {
        return localDataSource.getSpellsEdited().map { spells ->
            val strings = getStrings()
            spells.map { it.toDomain(strings) }
        }
    }

    override fun deleteLocalSpells(): Flow<Unit> {
        return localDataSource.deleteSpells()
    }

    private suspend fun getStrings(): Map<String, String> {
        val lang = getLanguageUseCase().single()
        return stringsRepository.getStrings(lang)
    }
}
