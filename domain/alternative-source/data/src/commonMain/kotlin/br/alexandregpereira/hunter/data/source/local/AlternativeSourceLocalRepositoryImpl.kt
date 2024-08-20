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

package br.alexandregpereira.hunter.data.source.local

import br.alexandregpereira.hunter.data.source.local.mapper.toDomain
import br.alexandregpereira.hunter.data.source.local.mapper.toEntity
import br.alexandregpereira.hunter.domain.source.AlternativeSourceLocalRepository
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class AlternativeSourceLocalRepositoryImpl(
    private val localDataSource: AlternativeSourceLocalDataSource
) : AlternativeSourceLocalRepository {

    override fun getAlternativeSources(): Flow<List<AlternativeSource>> {
        return localDataSource.getAlternativeSources().map { it.toDomain() }
    }

    override fun addAlternativeSource(acronym: String): Flow<Unit> {
        return flowOf(acronym).map { it.toEntity() }.flatMapLatest { entity ->
            localDataSource.addAlternativeSource(entity)
        }
    }

    override fun removeAlternativeSource(acronym: String): Flow<Unit> {
        return localDataSource.removeAlternativeSource(acronym)
    }
}
