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

package br.alexandregpereira.hunter.domain.source

import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class GetAlternativeSourcesUseCase(
    private val remoteRepository: AlternativeSourceRemoteRepository,
    private val settingsRepository: AlternativeSourceSettingsRepository,
    private val localRepository: AlternativeSourceLocalRepository
) {

    operator fun invoke(onlyContentEnabled: Boolean = true): Flow<List<AlternativeSource>> {
        return settingsRepository.getLanguage().map { lang ->
            val (remoteSources, localSourcesMap) = coroutineScope {
                val localDeferred = async {
                    localRepository.getAlternativeSources().single().groupBy { it.acronym }
                }
                val remoteDeferred = async { remoteRepository.getAlternativeSources(lang).single() }
                remoteDeferred.await() to localDeferred.await()
            }
            remoteSources.map {
                it.copy(isEnabled = localSourcesMap[it.acronym] != null)
            }
        }.map { alternativeSources ->
            alternativeSources.filter { !onlyContentEnabled || it.isEnabled }
        }
    }
}
