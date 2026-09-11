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

package br.alexandregpereira.hunter.domain.sort

import br.alexandregpereira.hunter.domain.collections.removeSpecialCharacters
import br.alexandregpereira.hunter.domain.model.CompendiumSortType
import br.alexandregpereira.hunter.domain.model.Monster

fun List<Monster>.sortMonstersByChallengeRating(descending: Boolean = false): List<Monster> {
    val challengeRatingComparator = if (descending) {
        compareByDescending<Monster> { it.challengeRatingData.value }
    } else {
        compareBy { it.challengeRatingData.value }
    }
    return this.sortedWith(
        challengeRatingComparator.thenBy { it.name.removeSpecialCharacters().lowercase() }
    )
}

fun List<Monster>.sortMonstersBy(sortType: CompendiumSortType): List<Monster> {
    return when (sortType) {
        CompendiumSortType.ALPHABETICAL -> sortMonstersByNameAndGroup()
        CompendiumSortType.CHALLENGE_RATING_ASC -> sortMonstersByChallengeRating()
        CompendiumSortType.CHALLENGE_RATING_DESC -> sortMonstersByChallengeRating(descending = true)
    }
}
