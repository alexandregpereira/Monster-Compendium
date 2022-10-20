/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster

import br.alexandregpereira.hunter.domain.model.MonsterAlternativeSource
import br.alexandregpereira.hunter.domain.model.MonsterSource
import br.alexandregpereira.hunter.domain.repository.MonsterAlternativeSourceRepository
import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MonsterAlternativeSourceRepositoryImpl @Inject constructor(
    private val getAlternativeSourcesUseCase: GetAlternativeSourcesUseCase,
) : MonsterAlternativeSourceRepository {

    override fun getAlternativeSources(): Flow<List<MonsterAlternativeSource>> {
        return getAlternativeSourcesUseCase().map {
            it.map { alternativeSource ->
                MonsterAlternativeSource(
                    source = MonsterSource(
                        name = alternativeSource.source.name,
                        acronym = alternativeSource.source.acronym
                    ),
                    totalMonsters = alternativeSource.totalMonsters
                )
            }
        }
    }
}
