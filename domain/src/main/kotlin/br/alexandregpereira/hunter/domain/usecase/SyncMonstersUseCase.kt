/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.exception.MonstersSourceNotFoundedException
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.Source
import br.alexandregpereira.hunter.domain.repository.AlternativeSourceRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce

@OptIn(FlowPreview::class)
class SyncMonstersUseCase internal constructor(
    private val repository: MonsterRepository,
    private val alternativeSourceRepository: AlternativeSourceRepository,
    private val saveMonstersUseCase: SaveMonstersUseCase
) {

    private val srdSource = Source("SRD", "SRD")

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Unit> {
        return repository.deleteMonsters()
            .flatMapLatest {
                alternativeSourceRepository.getAlternativeSources()
            }
            .map { alternativeSources ->
                alternativeSources.map { it.source }
                    .run { this + srdSource }
                    .asFlow()
                    .flatMapMerge { source ->
                        source.getRemoteMonsters()
                    }
                    .reduce { accumulator, value -> accumulator + value }
            }
            .flatMapLatest {
                saveMonstersUseCase(monsters = it)
            }
    }

    private fun Source.getRemoteMonsters(): Flow<List<Monster>> {
        return if (this == srdSource) {
            repository.getRemoteMonsters()
        } else {
            repository.getRemoteMonsters(source = this).catch { error ->
                when (error) {
                    is MonstersSourceNotFoundedException -> emit(emptyList())
                    else -> throw error
                }
            }
        }
    }
}
