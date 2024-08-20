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

package br.alexandregpereira.hunter.data.monster

import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSourceErrorHandler
import br.alexandregpereira.hunter.data.monster.remote.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.domain.exception.MonstersSourceNotFoundedException
import br.alexandregpereira.hunter.domain.exception.MonstersSourceUnexpectedException
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.repository.MonsterRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class MonsterRemoteRepositoryImpl(
    private val remoteDataSource: MonsterRemoteDataSource,
    private val errorHandler: MonsterRemoteDataSourceErrorHandler
) : MonsterRemoteRepository {


    override fun getMonsters(lang: String): Flow<List<Monster>> {
        return remoteDataSource.getMonsters(lang).toMonstersDomain()
    }

    override fun getMonsters(sourceAcronym: String, lang: String): Flow<List<Monster>> {
        return remoteDataSource.getMonsters(sourceAcronym, lang)
            .toMonstersDomain()
            .catch { error ->
                throw when {
                    errorHandler.isHttpNotFoundException(error) -> {
                        MonstersSourceNotFoundedException(sourceAcronym)
                    }
                    else -> MonstersSourceUnexpectedException(sourceAcronym, error)
                }
            }
    }

    private fun Flow<List<MonsterDto>>.toMonstersDomain(): Flow<List<Monster>> {
        return map { monsterDtos ->
            monsterDtos.toDomain()
        }
    }
}
