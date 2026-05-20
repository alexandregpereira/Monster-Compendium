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

package br.alexandregpereira.hunter.data.source

import br.alexandregpereira.hunter.domain.source.AlternativeSourceLocalRepository
import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesAdded
import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesUseCase
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.single

internal class GetAlternativeSourcesAddedImpl(
    private val getAlternativeSourcesUseCase: GetAlternativeSourcesUseCase,
    private val localRepository: AlternativeSourceLocalRepository,
) : GetAlternativeSourcesAdded {

    override suspend fun invoke(): List<AlternativeSource> {
        return getAlternativeSourcesUseCase()
            .firstOrNull()
            ?.filter { it.isAdded }
            ?.also { handleOriginalAcronymIfExists(it) }
            .orEmpty()
    }

    private suspend fun handleOriginalAcronymIfExists(
        remoteSources: List<AlternativeSource>,
    ) {
        val originalSourcesToRemove: MutableSet<String> = mutableSetOf()
        remoteSources.forEach { source ->
            val originalAcronym = source.originalAcronym
            if (originalAcronym != null && originalAcronym != source.acronym) {
                val currentOriginalSource = localRepository.getContentSource(originalAcronym)
                if (currentOriginalSource != null &&
                    localRepository.getContentSource(source.acronym) == null
                ) {
                    localRepository.addAlternativeSource(source.acronym).single()
                }
                originalSourcesToRemove.add(originalAcronym)
            }
        }

        originalSourcesToRemove.forEach {
            localRepository.removeAlternativeSource(it).single()
        }
    }
}
