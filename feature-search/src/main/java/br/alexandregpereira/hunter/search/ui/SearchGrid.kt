/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.search.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.MonsterCard

@Composable
fun SearchGrid(
    monsters: List<MonsterCardState>,
    contentPadding: PaddingValues = PaddingValues(),
    ) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding() + 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(monsters) { monsterCardState ->
            MonsterCard(
                name = monsterCardState.name,
                url = monsterCardState.imageUrl,
                iconRes = monsterCardState.type.iconRes,
                backgroundColor = if (isSystemInDarkTheme()) {
                    monsterCardState.backgroundColorDark
                } else {
                    monsterCardState.backgroundColorLight
                },
                challengeRating = monsterCardState.challengeRating,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
