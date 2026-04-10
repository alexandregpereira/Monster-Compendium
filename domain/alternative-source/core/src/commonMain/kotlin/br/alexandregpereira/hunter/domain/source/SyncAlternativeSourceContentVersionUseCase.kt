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
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Pair<Boolean, Map<String, Int>>> {
        return settingsRepository.getLanguage().flatMapLatest { lang ->
            flow {
                val (remoteSources, localSources, remoteDefaultSources, localDefaultSources) = coroutineScope {
                    val remoteDeferred = async { remoteRepository.getAlternativeSources(lang).single() }
                    val localDeferred = async { localRepository.getAlternativeSources().single() }
                    val remoteDefaultDeferred = async { remoteRepository.getDefaultSources(lang).single() }
                    val localDefaultDeferred = async { localRepository.getDefaultSources().single() }
                    val (remote, local) = remoteDeferred.await() to localDeferred.await()
                    val (remoteDefault, localDefault) = remoteDefaultDeferred.await() to localDefaultDeferred.await()
                    quadrupleOf(remote, local, remoteDefault, localDefault)
                }

                val localMap: Map<String, AlternativeSource> = localSources.associateBy { it.acronym }
                val rollbackMap: Map<String, Int> = localMap.mapValues { it.value.contentVersion }

                val changedMap: Map<String, Int> = remoteSources
                    .filter { remote ->
                        val local = localMap[remote.acronym]
                        local != null && remote.contentVersion != local.contentVersion
                    }
                    .associate { it.acronym to it.contentVersion }

                val localDefaultMap: Map<String, AlternativeSource> = localDefaultSources.associateBy { it.acronym }
                val defaultRollbackMap: Map<String, Int> = localDefaultMap.mapValues { it.value.contentVersion }

                val newDefaultSources = remoteDefaultSources.filter { remote ->
                    localDefaultMap[remote.acronym] == null
                }
                if (newDefaultSources.isNotEmpty()) {
                    localRepository.saveDefaultSources(
                        newDefaultSources.map { it.copy(isDefault = true) }
                    ).single()
                }

                val changedDefaultMap: Map<String, Int> = remoteDefaultSources
                    .filter { remote ->
                        val local = localDefaultMap[remote.acronym]
                        local != null && remote.contentVersion != local.contentVersion
                    }
                    .associate { it.acronym to it.contentVersion }

                val allChangedMap = changedMap + changedDefaultMap
                val allRollbackMap = rollbackMap + defaultRollbackMap

                if (allChangedMap.isEmpty() && newDefaultSources.isEmpty()) {
                    emit(false to emptyMap())
                } else {
                    if (allChangedMap.isNotEmpty()) {
                        localRepository.saveContentVersions(allChangedMap).single()
                    }
                    emit(true to allRollbackMap)
                }
            }
        }
    }

    @Suppress("unused")
    private fun <A, B, C, D> quadrupleOf(a: A, b: B, c: C, d: D) = object {
        operator fun component1() = a
        operator fun component2() = b
        operator fun component3() = c
        operator fun component4() = d
    }
}
