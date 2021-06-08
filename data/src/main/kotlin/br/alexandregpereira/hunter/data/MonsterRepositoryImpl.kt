/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data

import br.alexandregpereira.hunter.data.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.local.mapper.toDomain
import br.alexandregpereira.hunter.data.local.mapper.toEntity
import br.alexandregpereira.hunter.data.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.remote.mapper.toDomain
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import br.alexandregpereira.hunter.domain.model.Monster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MonsterRepositoryImpl(
    private val remoteDataSource: MonsterRemoteDataSource,
    private val localDataSource: MonsterLocalDataSource
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

    override fun getLocalMonsters(): Flow<List<Monster>> {
        return localDataSource.getMonsters().map { it.toDomain() }
    }
}
