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

package br.alexandregpereira.hunter.dndapi.data

import br.alexandregpereira.hunter.dndapi.data.model.Monster
import br.alexandregpereira.hunter.dndapi.data.model.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MonsterApi {

    @GET("monsters")
    suspend fun getMonsters(): Response

    @GET("monsters/{monsterIndex}")
    suspend fun getMonster(@Path("monsterIndex") monsterIndex: String): Monster
}
