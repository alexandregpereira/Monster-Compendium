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

package br.alexandregpereira.hunter.data.source.remote

import br.alexandregpereira.hunter.data.source.remote.mapper.toDomain
import br.alexandregpereira.hunter.domain.source.AlternativeSourceRepository
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AlternativeSourceRepositoryImpl(
    private val remoteDataSource: AlternativeSourceRemoteDataSource
) : AlternativeSourceRepository {

    override fun getAlternativeSources(): Flow<List<AlternativeSource>> {
       return remoteDataSource.getAlternativeSources().map { it.toDomain() }
    }

    override fun getMonsterLoreSources(): Flow<List<AlternativeSource>> {
        return remoteDataSource.getMonsterLoreSources().map { it.toDomain() }
    }
}
