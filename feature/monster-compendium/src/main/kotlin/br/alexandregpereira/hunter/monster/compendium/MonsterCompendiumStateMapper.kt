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

package br.alexandregpereira.hunter.monster.compendium

import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.monster.compendium.domain.MonsterPair
import br.alexandregpereira.hunter.monster.compendium.domain.MonstersBySection
import br.alexandregpereira.hunter.monster.compendium.ui.ColorState
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCardState
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterImageState
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterRowState
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterTypeState
import br.alexandregpereira.hunter.monster.compendium.ui.SectionState

fun MonstersBySection.asState(): Map<SectionState, List<MonsterRowState>> {
    return this.map {
        it.key.asState() to it.value.asState()
    }.toMap()
}

private fun MonsterSection.asState(): SectionState {
    return SectionState(
        id = id,
        title = title,
        parentTitle = parentTitle,
        isHeader = isHeader
    )
}

private fun List<MonsterPair>.asState(): List<MonsterRowState> {
    return this.map {
        MonsterRowState(
            leftMonsterCardState = it.first.asState(),
            rightMonsterCardState = it.second?.asState()
        )
    }
}

private fun MonsterPreview.asState(): MonsterCardState {
    return MonsterCardState(
        index = index,
        name = name,
        imageState = MonsterImageState(
            url = imageData.url,
            type = MonsterTypeState.valueOf(type.name),
            challengeRating = challengeRating,
            backgroundColor = ColorState(
                light = imageData.backgroundColor.light,
                dark = imageData.backgroundColor.dark
            ),
            isHorizontal = imageData.isHorizontal
        )
    )
}
