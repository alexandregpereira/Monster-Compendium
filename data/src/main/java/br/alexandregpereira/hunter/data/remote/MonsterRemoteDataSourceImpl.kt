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

package br.alexandregpereira.hunter.data.remote

import br.alexandregpereira.hunter.data.remote.model.MonsterDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class MonsterRemoteDataSourceImpl(
    private val fileManager: FileManager
) : MonsterRemoteDataSource {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    override fun getMonsters(): Flow<List<MonsterDto>> = fileManager.readText().map { jsonValue ->
        json.decodeFromString(jsonValue)
    }

    @ExperimentalCoroutinesApi
    override fun insertMonsters(monsters: List<MonsterDto>): Flow<Unit> = flow {
        emit(json.encodeToString(monsters))
    }.flatMapLatest { json ->
        fileManager.writeText(json)
    }
}
