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
