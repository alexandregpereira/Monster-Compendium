/*
 * Hunter - DnD 5th edition monster compendium application
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

package br.alexandregpereira.hunter.search.domain

import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SearchMonstersByNameUseCase @Inject internal constructor(
    private val monsterRepository: MonsterRepository
) {

    operator fun invoke(name: String): Flow<List<SearchMonsterResult>> {
        if (name.isBlank()) return flowOf(emptyList())

        return monsterRepository.getLocalMonstersByQuery("name LIKE '%$name%' ORDER BY name")
            .catch { error ->
                throw SearchMonstersByNameUnexpectedException(cause = error)
            }
            .map { monsters ->
                monsters.map { monster ->
                    SearchMonsterResult(
                        index = monster.index,
                        name = monster.name,
                        type = monster.type,
                        challengeRating = monster.challengeRating,
                        imageUrl = monster.imageData.url,
                        backgroundColorLight = monster.imageData.backgroundColor.light,
                        backgroundColorDark = monster.imageData.backgroundColor.dark
                    )
                }
            }
    }
}
