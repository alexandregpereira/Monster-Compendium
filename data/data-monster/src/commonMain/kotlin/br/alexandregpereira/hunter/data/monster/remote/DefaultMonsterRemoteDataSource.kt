/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster.remote

import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal class DefaultMonsterRemoteDataSource(
    private val client: HttpClient,
    private val json: Json
) : MonsterRemoteDataSource, MonsterRemoteDataSourceErrorHandler  {

    override fun getMonsters(lang: String): Flow<List<MonsterDto>> {
        return flow {
            emit(json.decodeFromString(client.get("$lang/monsters.json").bodyAsText()))
        }
    }

    override fun getMonsters(sourceAcronym: String, lang: String): Flow<List<MonsterDto>> {
        return flow {
            val response = client.get(
                urlString = "$lang/sources/${sourceAcronym.lowercase()}/monsters.json"
            )

            if (response.status.value != 200) {
                throw HttpError(code = response.status.value, message = response.bodyAsText())
            }

            emit(json.decodeFromString(response.bodyAsText()))
        }
    }

    override fun getMonsterImages(jsonUrl: String): Flow<List<MonsterImageDto>> {
        return flow {
            emit(json.decodeFromString(client.get(jsonUrl).bodyAsText()))
        }
    }

    override fun isHttpNotFoundException(error: Throwable): Boolean {
        return error is HttpError && error.code == 404
    }

    private class HttpError(val code: Int, message: String) : Throwable(message = message)
}
