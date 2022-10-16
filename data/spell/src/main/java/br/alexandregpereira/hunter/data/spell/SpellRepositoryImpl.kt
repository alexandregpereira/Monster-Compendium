/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022. Alexandre Gomes Pereira.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
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
import br.alexandregpereira.hunter.data.spell.remote.SpellRemoteDataSource
import br.alexandregpereira.hunter.data.spell.remote.mapper.toDomain
import br.alexandregpereira.hunter.domain.spell.SpellRepository
import br.alexandregpereira.hunter.domain.spell.model.Spell
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SpellRepositoryImpl @Inject constructor(
    private val remoteDataSource: SpellRemoteDataSource,
    private val localDataSource: SpellLocalDataSource
) : SpellRepository {

    override fun saveSpells(spells: List<Spell>): Flow<Unit> {
        return localDataSource.saveSpells(spells.toEntity())
    }

    override fun getRemoteSpells(): Flow<List<Spell>> {
        return remoteDataSource.getSpells().map { it.toDomain() }
    }

    override fun getLocalSpell(index: String): Flow<Spell> {
        return localDataSource.getSpell(index).map { it.toDomain() }
    }

    override fun deleteLocalSpells(): Flow<Unit> {
        return localDataSource.deleteSpells()
    }
}
