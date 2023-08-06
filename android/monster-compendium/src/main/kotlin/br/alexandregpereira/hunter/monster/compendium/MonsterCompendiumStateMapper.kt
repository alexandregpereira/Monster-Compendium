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

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentItemState
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentItemTypeState
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState

fun List<MonsterCompendiumItem>.asState(): List<CompendiumItemState> {
    return this.map { item ->
        when (item) {
            is MonsterCompendiumItem.Title -> CompendiumItemState.Title(
                value = item.value,
                id = item.id,
                isHeader = item.isHeader
            )
            is MonsterCompendiumItem.Item -> CompendiumItemState.Item(
                value = item.monster.asState()
            )
        }
    }
}

private fun Monster.asState(): MonsterCardState {
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

@JvmName("asStateTableContentItem")
fun List<TableContentItem>.asState(): List<TableContentItemState> {
    return this.map {
        TableContentItemState(
            id = it.id,
            text = it.text,
            type = TableContentItemTypeState.valueOf(it.type.name)
        )
    }
}
