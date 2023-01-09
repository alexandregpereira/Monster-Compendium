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

package br.alexandregpereira.hunter.ui.compendium.monster

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compendium.Compendium
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState
import br.alexandregpereira.hunter.ui.compose.MonsterCard
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun MonsterCompendium(
    items: List<CompendiumItemState>,
    animateItems: Boolean = false,
    listState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onItemCLick: (index: String) -> Unit = {},
    onItemLongCLick: (index: String) -> Unit = {},
) = Compendium(
    items = items,
    animateItems = animateItems,
    listState = listState,
    contentPadding = contentPadding,
    key = { it.getMonsterCardState().index },
    isHorizontalCard = { it.getMonsterCardState().imageState.isHorizontal },
    cardContent = { item ->
        val monsterCardState = item.getMonsterCardState()
        MonsterCard(
            name = monsterCardState.name,
            url = monsterCardState.imageState.url,
            iconRes = monsterCardState.imageState.type.iconRes,
            backgroundColor = monsterCardState.imageState.backgroundColor.getColor(
                isSystemInDarkTheme()
            ),
            challengeRating = monsterCardState.imageState.challengeRating,
            modifier = Modifier,
            onCLick = { onItemCLick(monsterCardState.index) },
            onLongCLick = { onItemLongCLick(monsterCardState.index) }
        )
    }
)

private fun CompendiumItemState.Item.getMonsterCardState() = this.value as MonsterCardState

@Preview
@Composable
private fun MonsterCompendiumPreview() = Window {
    val imageState = MonsterImageState(
        url = "",
        type = MonsterTypeState.ABERRATION,
        challengeRating = 8.0f,
        backgroundColor = ColorState(
            light = "#ffe0e0",
            dark = "#ffe0e0"
        )
    )
    MonsterCompendium(
        items = mutableListOf<CompendiumItemState>(
            CompendiumItemState.Title(value = "Any"),
        ).also {
            it.addAll(
                (0..10).map { i ->
                    CompendiumItemState.Item(
                        value = MonsterCardState(
                            index = "asdasdasd$i",
                            name = "Monster of monsters $i",
                            imageState = imageState.copy(isHorizontal = i == 2),
                        )
                    )
                }
            )
        }
    )
}
