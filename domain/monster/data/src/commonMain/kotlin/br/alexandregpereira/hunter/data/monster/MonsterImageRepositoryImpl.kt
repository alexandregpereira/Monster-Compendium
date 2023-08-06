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
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import br.alexandregpereira.hunter.domain.settings.GetMonsterImageJsonUrlUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MonsterImageRepositoryImpl(
    private val remoteDataSource: MonsterRemoteDataSource,
    private val getMonsterImageJsonUrlUseCase: GetMonsterImageJsonUrlUseCase
) : MonsterImageRepository {

    override fun getMonsterImages(jsonUrl: String): Flow<List<MonsterImage>> {
        return remoteDataSource.getMonsterImages(jsonUrl)
            .map { it.toDomain() }
    }

    override fun getMonsterImageJsonUrl(): Flow<String> {
        return getMonsterImageJsonUrlUseCase()
    }
}
