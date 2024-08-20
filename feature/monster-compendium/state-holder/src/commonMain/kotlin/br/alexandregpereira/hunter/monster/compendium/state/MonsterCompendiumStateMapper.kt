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

package br.alexandregpereira.hunter.monster.compendium.state

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem

internal fun List<MonsterCompendiumItem>.asState(): List<MonsterCompendiumItemState> {
    return this.map { item ->
        when (item) {
            is MonsterCompendiumItem.Title -> MonsterCompendiumItemState.Title(
                value = item.value,
                id = item.id,
                isHeader = item.isHeader
            )
            is MonsterCompendiumItem.Item -> MonsterCompendiumItemState.Item(
                monster = item.monster.asState()
            )
        }
    }
}

private fun Monster.asState(): MonsterPreviewState {
    return MonsterPreviewState(
        index = index,
        name = name,
        type = type,
        challengeRating = challengeRatingFormatted,
        imageUrl = imageData.url,
        backgroundColorLight = imageData.backgroundColor.light,
        backgroundColorDark = imageData.backgroundColor.dark,
        isImageHorizontal = imageData.isHorizontal,
        imageContentScale = imageData.contentScale,
    )
}
