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

package br.alexandregpereira.hunter.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState.Item
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState.Title
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCompendium

@Composable
fun SearchGrid(
    monsterRows: List<MonsterCardState>,
    totalResults: String,
    contentPadding: PaddingValues = PaddingValues(),
    onCardClick: (String) -> Unit = {},
    onCardLongClick: (String) -> Unit = {},
) = AnimatedVisibility(
    visible = monsterRows.isNotEmpty(),
    enter = fadeIn(),
) {
    val monstersBySection = listOf(
        Title(
            value = totalResults,
            id = "results",
        )
    ) + monsterRows.map {
        Item(it)
    }
    MonsterCompendium(
        items = monstersBySection,
        contentPadding = contentPadding,
        onItemCLick = onCardClick,
        onItemLongCLick = onCardLongClick
    )
}
