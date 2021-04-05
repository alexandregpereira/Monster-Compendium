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
import br.alexandregpereira.hunter.domain.MonsterRepository
import br.alexandregpereira.hunter.domain.model.Monster
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoroutinesApi::class)
internal class MonsterRepositoryImpl(
    private val remoteDataSource: MonsterRemoteDataSource,
    private val localDataSource: MonsterLocalDataSource
) : MonsterRepository {

    override fun getMonsters(): Flow<List<Monster>> {
        return localDataSource.getMonsters().flatMapLatest { entityList ->
            if (entityList.isEmpty()) getMonstersRemote()
            else flowOf(entityList.toDomain())
        }
    }

    private fun getMonstersRemote(): Flow<List<Monster>> {
        return remoteDataSource.getMonsters().map { it.toDomain() }.onEach {
            localDataSource.saveMonsters(it.toEntity()).collect()
        }.flatMapLatest {
            localDataSource.getMonsters()
        }.map { it.toDomain() }
    }
}