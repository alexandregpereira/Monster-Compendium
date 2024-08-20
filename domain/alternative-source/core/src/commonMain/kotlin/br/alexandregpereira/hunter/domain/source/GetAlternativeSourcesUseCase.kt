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
