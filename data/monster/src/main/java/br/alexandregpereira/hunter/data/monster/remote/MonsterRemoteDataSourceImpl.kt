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

package br.alexandregpereira.hunter.data.monster.remote

import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageDto
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class MonsterRemoteDataSourceImpl @Inject constructor(
    private val monsterApi: MonsterApi
) : MonsterRemoteDataSource {

    override fun getMonsters(): Flow<List<MonsterDto>> = flow {
        emit(monsterApi.getMonsters())
    }

    override fun getMonsterImages(jsonUrl: String): Flow<List<MonsterImageDto>> = flow {
        emit(monsterApi.getMonsterImages(jsonUrl))
    }

    override fun getMonsters(sourceAcronym: String): Flow<List<MonsterDto>> = flow {
        emit(monsterApi.getMonsters(sourceAcronym.lowercase(Locale.ROOT)))
    }
}
