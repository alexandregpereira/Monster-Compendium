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

package br.alexandregpereira.hunter.data.monster.remote

import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
