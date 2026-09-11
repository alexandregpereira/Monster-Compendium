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

package br.alexandregpereira.hunter.monster.compendium.domain

import br.alexandregpereira.hunter.domain.model.CompendiumSortType
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAlphabetUseCase internal constructor() {

    operator fun invoke(
        items: List<MonsterCompendiumItem>,
        sortType: CompendiumSortType = CompendiumSortType.ALPHABETICAL,
    ): Flow<List<String>> {
        return flow {
            val sectionKeys = items.mapToSectionKeys(sortType)
            val alphabet = when (sortType) {
                CompendiumSortType.ALPHABETICAL -> sectionKeys.sorted().distinct()
                // Keeps the numeric order of the sections, a string sort would put "10" before "2"
                CompendiumSortType.CHALLENGE_RATING_ASC,
                CompendiumSortType.CHALLENGE_RATING_DESC -> sectionKeys.distinct()
            }
            emit(alphabet)
        }
    }
}
