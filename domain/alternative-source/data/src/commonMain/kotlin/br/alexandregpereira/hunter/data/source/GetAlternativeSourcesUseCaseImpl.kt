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

package br.alexandregpereira.hunter.data.source

import br.alexandregpereira.hunter.domain.source.AlternativeSourceLocalRepository
import br.alexandregpereira.hunter.domain.source.AlternativeSourceRemoteRepository
import br.alexandregpereira.hunter.domain.source.AlternativeSourceSettingsRepository
import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesUseCase
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

internal class GetAlternativeSourcesUseCaseImpl(
    private val remoteRepository: AlternativeSourceRemoteRepository,
    private val settingsRepository: AlternativeSourceSettingsRepository,
    private val localRepository: AlternativeSourceLocalRepository,
) : GetAlternativeSourcesUseCase {

    override operator fun invoke(onlyContentEnabled: Boolean): Flow<List<AlternativeSource>> {
        return settingsRepository.getLanguage().map { lang ->
            val (remoteSources, localSourcesMap, remoteDefaultSources) = coroutineScope {
                val localDeferred = async {
                    localRepository.getAlternativeSources().single().groupBy { it.acronym }
                }
                val remoteDeferred = async { remoteRepository.getAlternativeSources(lang).single() }
                val remoteDefaultDeferred = async { remoteRepository.getDefaultSources(lang).single() }
                Triple(remoteDeferred.await(), localDeferred.await(), remoteDefaultDeferred.await())
            }
            val defaultSources = remoteDefaultSources.map {
                it.copy(isAdded = true)
            }
            val alternativeSources = remoteSources.map {
                it.copy(isAdded = localSourcesMap[it.acronym] != null)
            }
            alternativeSources + defaultSources
        }.map { sources ->
            sources.filter { !onlyContentEnabled || it.isEnabled }
        }
    }
}
