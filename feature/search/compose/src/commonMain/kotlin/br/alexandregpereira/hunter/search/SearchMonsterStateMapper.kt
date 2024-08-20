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

package br.alexandregpereira.hunter.search

import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.search.domain.SearchKey
import br.alexandregpereira.hunter.search.domain.SearchMonsterResult
import br.alexandregpereira.hunter.search.domain.SearchValueType
import br.alexandregpereira.hunter.search.ui.SearchKeyState
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale

internal fun List<SearchMonsterResult>.asState(): List<MonsterCardState> {
    return this.asMonsterCardStates()
}

internal fun List<SearchMonsterResult>.asMonsterCardStates(): List<MonsterCardState> {
    return this.map { result ->
        MonsterCardState(
            index = result.index,
            name = result.name,
            imageState = MonsterImageState(
                url = result.imageUrl,
                type = MonsterTypeState.valueOf(result.type.name),
                backgroundColor = ColorState(
                    light = result.backgroundColorLight,
                    dark = result.backgroundColorDark
                ),
                challengeRating = result.challengeRating,
                isHorizontal = result.isHorizontalImage,
                contentDescription = result.name,
                contentScale = when (result.imageContentScale) {
                    MonsterImageContentScale.Fit -> AppImageContentScale.Fit
                    MonsterImageContentScale.Crop -> AppImageContentScale.Crop
                },
            ),
        )
    }
}

internal fun List<SearchKey>.toState(): List<SearchKeyState> {
    return this.associateWith { searchKey ->
        val symbols = when (searchKey.valueType) {
            SearchValueType.String -> listOf("=")
            SearchValueType.Boolean -> listOf("!")
            SearchValueType.Float -> listOf(">", "<", "=")
        }
        symbols
    }.map { (searchKey, symbols) ->
        symbols.map { symbol ->
            SearchKeyState(
                key = searchKey.key,
                symbol = symbol,
            )
        }
    }.flatten()
}
