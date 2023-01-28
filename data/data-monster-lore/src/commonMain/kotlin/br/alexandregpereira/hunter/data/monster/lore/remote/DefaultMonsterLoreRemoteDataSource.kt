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

package br.alexandregpereira.hunter.data.monster.lore.remote

import br.alexandregpereira.hunter.data.monster.lore.remote.model.MonsterLoreDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal class DefaultMonsterLoreRemoteDataSource(
    private val client: HttpClient,
    private val json: Json
) : MonsterLoreRemoteDataSource {

    override fun getMonstersLore(sourceAcronym: String, lang: String): Flow<List<MonsterLoreDto>> {
        return flow {
            emit(
                client.get("$lang/lore/${sourceAcronym.lowercase()}/monster-lore.json")
                    .bodyAsText()
                    .let { json.decodeFromString(it) }
            )
        }
    }
}
