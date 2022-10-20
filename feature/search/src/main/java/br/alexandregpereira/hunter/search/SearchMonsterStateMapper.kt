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

package br.alexandregpereira.hunter.search

import br.alexandregpereira.hunter.search.domain.SearchMonsterResult
import br.alexandregpereira.hunter.search.ui.MonsterCardState
import br.alexandregpereira.hunter.search.ui.MonsterTypeState

internal fun List<SearchMonsterResult>.asState(): List<MonsterCardState> {
    return this.map { result ->
        MonsterCardState(
            index = result.index,
            name = result.name,
            imageUrl = result.imageUrl,
            type = MonsterTypeState.valueOf(result.type.name),
            challengeRating = result.challengeRating,
            contentDescription = result.name,
            backgroundColorLight = result.backgroundColorLight,
            backgroundColorDark = result.backgroundColorDark
        )
    }
}
