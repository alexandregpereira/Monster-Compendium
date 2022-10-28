/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.monster.compendium

import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.monster.compendium.domain.MonsterPair
import br.alexandregpereira.hunter.monster.compendium.domain.MonstersBySection
import br.alexandregpereira.hunter.ui.compendium.SectionState
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterRowState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState

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
            leftCardState = it.first.asState(),
            rightCardState = it.second?.asState()
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
