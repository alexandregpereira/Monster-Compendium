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

package br.alexandregpereira.hunter.monster.compendium

import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumItemState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterPreviewState
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentItemState
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentItemTypeState

internal fun List<MonsterCompendiumItemState>.asState(): List<CompendiumItemState> {
    return this.map { item ->
        when (item) {
            is MonsterCompendiumItemState.Title -> CompendiumItemState.Title(
                value = item.value,
                id = item.id,
                isHeader = item.isHeader
            )
            is MonsterCompendiumItemState.Item -> CompendiumItemState.Item(
                value = item.monster.asState()
            )
        }
    }
}

private fun MonsterPreviewState.asState(): MonsterCardState {
    return MonsterCardState(
        index = index,
        name = name,
        imageState = MonsterImageState(
            url = imageUrl,
            type = MonsterTypeState.valueOf(type.name),
            challengeRating = challengeRating,
            backgroundColor = ColorState(
                light = backgroundColorLight,
                dark = backgroundColorDark
            ),
            isHorizontal = isImageHorizontal,
            contentScale = when (imageContentScale) {
                MonsterImageContentScale.Fit -> AppImageContentScale.Fit
                MonsterImageContentScale.Crop -> AppImageContentScale.Crop
            },
        )
    )
}

internal fun List<TableContentItem>.asStateTableContentItem(): List<TableContentItemState> {
    return this.map {
        TableContentItemState(
            id = it.id,
            text = it.text,
            type = TableContentItemTypeState.valueOf(it.type.name)
        )
    }
}
