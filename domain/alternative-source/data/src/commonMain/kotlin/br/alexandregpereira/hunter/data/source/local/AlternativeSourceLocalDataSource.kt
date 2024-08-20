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

import br.alexandregpereira.hunter.data.source.local.dao.AlternativeSourceDao
import br.alexandregpereira.hunter.data.source.local.entity.AlternativeSourceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class AlternativeSourceLocalDataSource(
    private val dao: AlternativeSourceDao
) {

    fun getAlternativeSources(): Flow<List<AlternativeSourceEntity>> = flow {
        emit(dao.getAlternativeSources())
    }

    fun addAlternativeSource(alternativeSource: AlternativeSourceEntity): Flow<Unit> = flow {
        emit(dao.addAlternativeSource(alternativeSource))
    }

    fun removeAlternativeSource(acronym: String): Flow<Unit> = flow {
        emit(dao.removeAlternativeSource(acronym))
    }
}
