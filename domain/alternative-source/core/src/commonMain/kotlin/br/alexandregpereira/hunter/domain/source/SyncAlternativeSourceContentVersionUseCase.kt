/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.domain.source

import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

class SyncAlternativeSourceContentVersionUseCase internal constructor(
    private val remoteRepository: AlternativeSourceRemoteRepository,
    private val localRepository: AlternativeSourceLocalRepository,
    private val settingsRepository: AlternativeSourceSettingsRepository,
) {

    operator fun invoke(): Flow<Pair<Boolean, Map<String, Int>>> {
        return settingsRepository.getLanguage().flatMapLatest { lang ->
            flow {
                val (remoteSources, localSources) = coroutineScope {
                    val remoteDeferred = async { remoteRepository.getAlternativeSources(lang).single() }
                    val localDeferred = async { localRepository.getAlternativeSources().single() }
                    remoteDeferred.await() to localDeferred.await()
                }

                val localMap: Map<String, AlternativeSource> = localSources.associateBy { it.acronym }
                val rollbackMap: Map<String, Int> = localMap.mapValues { it.value.contentVersion }

                val changedMap: Map<String, Int> = remoteSources
                    .filter { remote ->
                        val local = localMap[remote.acronym]
                        local != null && remote.contentVersion != local.contentVersion
                    }
                    .associate { it.acronym to it.contentVersion }

                if (changedMap.isEmpty()) {
                    emit(false to emptyMap())
                } else {
                    localRepository.saveContentVersions(changedMap).single()
                    emit(true to rollbackMap)
                }
            }
        }
    }
}
