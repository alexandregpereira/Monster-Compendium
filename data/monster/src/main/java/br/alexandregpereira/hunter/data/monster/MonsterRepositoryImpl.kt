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

import br.alexandregpereira.hunter.data.monster.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.monster.local.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.local.mapper.toEntity
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.domain.exception.MonstersSourceNotFoundedException
import br.alexandregpereira.hunter.domain.exception.MonstersSourceUnexpectedException
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

internal class MonsterRepositoryImpl @Inject constructor(
    private val remoteDataSource: MonsterRemoteDataSource,
    private val localDataSource: MonsterLocalDataSource,
) : MonsterRepository {

    override fun saveMonsters(monsters: List<Monster>, isSync: Boolean): Flow<Unit> {
        return localDataSource.saveMonsters(monsters.toEntity(), isSync)
    }

    override fun getRemoteMonsters(lang: String): Flow<List<Monster>> {
        return remoteDataSource.getMonsters(lang).toMonstersDomain()
    }

    override fun getRemoteMonsters(sourceAcronym: String, lang: String): Flow<List<Monster>> {
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

    override fun getLocalMonsterPreviews(): Flow<List<Monster>> {
        return localDataSource.getMonsterPreviews().map { it.toDomain() }
    }

    override fun getLocalMonsters(): Flow<List<Monster>> {
        return localDataSource.getMonsters().map { it.toDomain() }
    }

    override fun getLocalMonsters(indexes: List<String>): Flow<List<Monster>> {
        return localDataSource.getMonsters(indexes).map { it.toDomain() }
    }

    override fun getLocalMonster(index: String): Flow<Monster> {
        return localDataSource.getMonster(index).map { it.toDomain() }
    }

    override fun getLocalMonstersByQuery(query: String): Flow<List<Monster>> {
        return localDataSource.getMonstersByQuery(query).map { it.toDomain() }
    }

    private fun Flow<List<MonsterDto>>.toMonstersDomain(): Flow<List<Monster>> {
        return map { monsterDtos ->
            monsterDtos.toDomain()
        }
    }
}
