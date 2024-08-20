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

package br.alexandregpereira.hunter.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState.Item
import br.alexandregpereira.hunter.ui.compendium.CompendiumItemState.Title
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCompendium

@Composable
internal fun SearchGrid(
    monsterRows: List<MonsterCardState>,
    totalResults: String,
    listState: LazyGridState,
    contentPadding: PaddingValues = PaddingValues(),
    onCardClick: (String) -> Unit = {},
    onCardLongClick: (String) -> Unit = {},
) = AnimatedVisibility(
    visible = monsterRows.isNotEmpty(),
    enter = fadeIn(animationSpec = spring()),
    exit = fadeOut(animationSpec = spring()),
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
        listState = listState,
        contentPadding = contentPadding,
        onItemCLick = onCardClick,
        onItemLongCLick = onCardLongClick
    )
}
