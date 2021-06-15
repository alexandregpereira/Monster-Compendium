/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data

import br.alexandregpereira.hunter.data.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.local.mapper.toDomain
import br.alexandregpereira.hunter.data.local.mapper.toEntity
import br.alexandregpereira.hunter.data.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.remote.mapper.toDomain
import br.alexandregpereira.hunter.domain.exception.MonstersSourceNotFoundedException
import br.alexandregpereira.hunter.domain.exception.MonstersSourceUnexpectedException
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.Source
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

internal class MonsterRepositoryImpl(
    private val remoteDataSource: MonsterRemoteDataSource,
    private val localDataSource: MonsterLocalDataSource,
) : MonsterRepository {

    override fun deleteMonsters(): Flow<Unit> {
        return localDataSource.deleteMonsters()
    }

    override fun saveMonsters(monsters: List<Monster>): Flow<Unit> {
        return localDataSource.saveMonsters(monsters.toEntity())
    }

    override fun getRemoteMonsters(): Flow<List<Monster>> {
        return remoteDataSource.getMonsters().map { it.toDomain() }
    }

    override fun getRemoteMonsters(source: Source): Flow<List<Monster>> {
        return remoteDataSource.getMonsters(source.acronym)
            .map { it.toDomain() }
            .catch { error ->
                throw when {
                    error is HttpException && error.code() == 404 -> {
                        MonstersSourceNotFoundedException(source.name)
                    }
                    else -> MonstersSourceUnexpectedException(source.name, error)
                }
            }
    }

    override fun getLocalMonsters(): Flow<List<Monster>> {
        return localDataSource.getMonsters().map { it.toDomain() }
    }
}
