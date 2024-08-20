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

import br.alexandregpereira.hunter.data.source.local.dao.AlternativeSourceDao
import br.alexandregpereira.hunter.data.source.local.entity.AlternativeSourceEntity
import br.alexandregpereira.hunter.database.AlternativeSourceQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class AlternativeSourceDaoImpl(
    private val dispatcher: CoroutineDispatcher,
    private val queries: AlternativeSourceQueries,
) : AlternativeSourceDao {

    override suspend fun getAlternativeSources(): List<AlternativeSourceEntity> = withContext(dispatcher) {
        queries.getAll().executeAsList().map { it.toLocalEntity() }
    }

    override suspend fun addAlternativeSource(alternativeSource: AlternativeSourceEntity) = withContext(dispatcher) {
        queries.insert(alternativeSource.toDatabaseEntity())
    }

    override suspend fun removeAlternativeSource(acronym: String) = withContext(dispatcher) {
        queries.deleteByAcronym(acronym)
    }
}
