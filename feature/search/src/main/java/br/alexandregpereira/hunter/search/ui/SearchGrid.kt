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
import androidx.compose.foundation.ExperimentalFoundationApi
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchGrid(
    monsters: List<MonsterCardState>,
    contentPadding: PaddingValues = PaddingValues(),
    onCardClick: (String) -> Unit = {},
    onCardLongClick: (String) -> Unit = {},
) = AnimatedVisibility(
    visible = monsters.isNotEmpty(),
    enter = fadeIn(),
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding() + 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(monsters, key = { it.index }) { monsterCardState ->
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
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .animateItemPlacement(),
                onCLick = {
                    onCardClick(monsterCardState.index)
                },
                onLongCLick = { onCardLongClick(monsterCardState.index) }
            )
        }
    }
}
