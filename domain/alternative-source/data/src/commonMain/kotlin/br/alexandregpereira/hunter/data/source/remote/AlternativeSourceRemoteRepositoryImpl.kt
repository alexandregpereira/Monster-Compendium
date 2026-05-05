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

package br.alexandregpereira.hunter.data.source.remote

import br.alexandregpereira.hunter.data.source.remote.mapper.toDomain
import br.alexandregpereira.hunter.data.source.remote.model.AlternativeSourceDto
import br.alexandregpereira.hunter.domain.source.AlternativeSourceRemoteRepository
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import br.alexandregpereira.hunter.featureFlag.FeatureFlagProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class AlternativeSourceRemoteRepositoryImpl(
    private val remoteDataSource: AlternativeSourceRemoteDataSource,
    private val featureFlagProvider: FeatureFlagProvider,
) : AlternativeSourceRemoteRepository {

    override fun getAlternativeSources(lang: String): Flow<List<AlternativeSource>> = flow {
        if (featureFlagProvider.isFeatureEnabled(feature = "alternative-sources-complete")) {
            coroutineScope {
                val completeAsync = async { remoteDataSource.getAlternativeSources(lang) }
                val basicAsync = async { remoteDataSource.getBasicAlternativeSources(lang) }
                val completeSources = completeAsync.await()
                val basicSources = basicAsync.await()
                val completeSourcesMap = completeSources.associateBy {
                    it.source.acronym.lowercase()
                }
                val basicSourcesMap = basicSources.associateBy {
                    it.source.acronym.lowercase()
                }
                val sources = completeSources + basicSources
                // If completeSources and basicSources has the same source content,
                // uses the completeSource instead of the basic source
                val sourcesMap = mutableMapOf<String, AlternativeSourceDto?>()
                sources.forEach {
                    val acronym = it.source.acronym.lowercase()
                    sourcesMap[acronym] = completeSourcesMap[acronym] ?: basicSourcesMap[acronym]
                }
                val finalSources = sourcesMap.values
                    .filterNotNull()
                    .toList()
                    .toDomain()
                emit(finalSources)
            }
        } else {
            emit(remoteDataSource.getBasicAlternativeSources(lang).toDomain())
        }
    }

    override fun getDefaultSources(lang: String): Flow<List<AlternativeSource>> {
        return remoteDataSource.getDefaultSources(lang).map { dtos ->
            dtos.toDomain().map { it.copy(isDefault = true) }
        }
    }
}
