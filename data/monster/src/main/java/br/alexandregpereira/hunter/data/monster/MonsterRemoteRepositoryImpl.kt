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

package br.alexandregpereira.hunter.data.monster

import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.domain.exception.MonstersSourceNotFoundedException
import br.alexandregpereira.hunter.domain.exception.MonstersSourceUnexpectedException
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.repository.MonsterRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

internal class MonsterRemoteRepositoryImpl(
    private val remoteDataSource: MonsterRemoteDataSource,
) : MonsterRemoteRepository {


    override fun getMonsters(lang: String): Flow<List<Monster>> {
        return remoteDataSource.getMonsters(lang).toMonstersDomain()
    }

    override fun getMonsters(sourceAcronym: String, lang: String): Flow<List<Monster>> {
        return remoteDataSource.getMonsters(sourceAcronym, lang)
            .toMonstersDomain()
            .catch { error ->
                throw when {
                    error is HttpException && error.code() == 404 -> {
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
