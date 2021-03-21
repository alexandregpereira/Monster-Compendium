/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.monster.compendium.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.ui.compose.MonsterItemType
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@ExperimentalFoundationApi
@Composable
fun MonsterCompendium(monsters: List<MonsterCardItem>) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(monsters) {
            MonsterCard(
                imageUrl = it.imageData.url,
                backgroundColor = it.imageData.backgroundColor,
                contentDescription = it.name,
                challengeRating = it.challengeRating,
                type = MonsterItemType.valueOf(it.type.name)
            )
        }
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun MonsterCompendiumPreview() = HunterTheme {
    Surface {
        MonsterCompendium(
            monsters = (0..10).map {
                MonsterCardItem(
                    index = "asdasdasd",
                    type = MonsterType.ABERRATION,
                    challengeRating = 8.0f,
                    name = "Monster of monsters",
                    imageData = br.alexandregpereira.hunter.domain.model.MonsterImageData(
                        url = "sadasd",
                        backgroundColor = "#ffe0e0"
                    )
                )
            }
        )
    }
}