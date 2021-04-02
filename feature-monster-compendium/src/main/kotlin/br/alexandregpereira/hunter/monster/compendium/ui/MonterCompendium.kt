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

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.monster.compendium.MonsterCardItem
import br.alexandregpereira.hunter.monster.compendium.MonsterCardItemsBySection
import br.alexandregpereira.hunter.ui.compose.MonsterItemType
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun MonsterCompendium(
    monstersBySection: MonsterCardItemsBySection,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onItemCLick: (index: String) -> Unit = {},
) = LazyColumn(contentPadding = contentPadding) {

    monstersBySection.entries.forEachIndexed { index, monsterSectionEntry ->
        if (index > 0) {
            item {
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .background(MaterialTheme.colors.surface)
                )
            }
        }
        if (monsterSectionEntry.key.showTitle) {
            item {
                Text(
                    text = monsterSectionEntry.key.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }
        monsterSectionEntry.value.entries.forEach { monsterEntry ->
            item {
                val leftMonster = monsterEntry.key
                val rightMonster = monsterEntry.value

                MonsterSection(
                    leftMonster = leftMonster,
                    rightMonster = rightMonster,
                    onItemClick = onItemCLick
                )
            }
        }
    }
}

@Composable
fun MonsterSection(
    leftMonster: MonsterCardItem,
    modifier: Modifier = Modifier,
    rightMonster: MonsterCardItem? = null,
    onItemClick: (index: String) -> Unit = {},
) = Row(
    modifier
        .background(MaterialTheme.colors.surface)
        .padding(horizontal = 8.dp)
) {

    MonsterCard(
        monster = leftMonster,
        modifier = Modifier.weight(1f),
        onCLick = { onItemClick(leftMonster.index) }
    )
    rightMonster?.let {
        MonsterCard(
            monster = it,
            modifier = Modifier.weight(1f),
            onCLick = { onItemClick(it.index) }
        )
    }
}

@Composable
private fun MonsterCard(
    monster: MonsterCardItem,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit = {},
) = MonsterCard(
    name = monster.name,
    imageUrl = monster.imageData.url,
    backgroundColor = monster.imageData.backgroundColor.getColor(isSystemInDarkTheme()),
    contentDescription = monster.name,
    challengeRating = monster.challengeRating,
    type = MonsterItemType.valueOf(monster.type.name),
    modifier = modifier,
    onCLick = onCLick
)

@Preview
@Composable
fun MonsterCompendiumPreview() = HunterTheme {
    Surface {
        MonsterCompendium(
            monstersBySection = mapOf(
                MonsterSection() to (0..10).map {
                    val leftMonster = MonsterCardItem(
                        index = "asdasdasd",
                        type = MonsterType.ABERRATION,
                        challengeRating = 8.0f,
                        name = "Monster of monsters",
                        group = if (it == 1 || it == 2) "Group" else null,
                        imageData = MonsterImageData(
                            url = "sadasd",
                            backgroundColor = Color(
                                light = "#ffe0e0",
                                dark = "#ffe0e0"
                            )
                        ),
                    )
                    val rightMonster = if (it == 1) {
                        MonsterCardItem(
                            index = "asdasdasd",
                            type = MonsterType.ABERRATION,
                            challengeRating = 8.0f,
                            name = "Monster of monsters",
                            imageData = MonsterImageData(
                                url = "sadasd",
                                backgroundColor = Color(
                                    light = "#ffe0e0",
                                    dark = "#ffe0e0"
                                )
                            ),
                        )
                    } else {
                        null
                    }
                    leftMonster to rightMonster
                }.toMap()
            )
        )
    }
}

@Preview
@Composable
fun MonsterCompendiumWithSectionTitlePreview() = HunterTheme {
    Surface {
        MonsterCompendium(
            monstersBySection = mapOf(
                MonsterSection(title = "Title") to (0..10).map {
                    val leftMonster = MonsterCardItem(
                        index = "asdasdasd",
                        type = MonsterType.ABERRATION,
                        challengeRating = 8.0f,
                        name = "Monster of monsters",
                        group = if (it == 1 || it == 2) "Group" else null,
                        imageData = MonsterImageData(
                            url = "sadasd",
                            backgroundColor = Color(
                                light = "#ffe0e0",
                                dark = "#ffe0e0"
                            )
                        ),
                    )
                    val rightMonster = if (it == 1) {
                        MonsterCardItem(
                            index = "asdasdasd",
                            type = MonsterType.ABERRATION,
                            challengeRating = 8.0f,
                            name = "Monster of monsters",
                            imageData = MonsterImageData(
                                url = "sadasd",
                                backgroundColor = Color(
                                    light = "#ffe0e0",
                                    dark = "#ffe0e0"
                                )
                            ),
                        )
                    } else {
                        null
                    }
                    leftMonster to rightMonster
                }.toMap()
            )
        )
    }
}

@Preview
@Composable
fun MonsterSection2ItemsPreview() = HunterTheme {
    Surface {
        MonsterSection(
            leftMonster = MonsterCardItem(
                index = "asdasdasd",
                type = MonsterType.ABERRATION,
                challengeRating = 8.0f,
                name = "Monster of monsters",
                imageData = MonsterImageData(
                    url = "sadasd",
                    backgroundColor = Color(
                        light = "#ffe0e0",
                        dark = "#ffe0e0"
                    )
                )
            ),
            rightMonster = MonsterCardItem(
                index = "asdasdasd",
                type = MonsterType.ABERRATION,
                challengeRating = 8.0f,
                name = "Monster of monsters",
                imageData = MonsterImageData(
                    url = "sadasd",
                    backgroundColor = Color(
                        light = "#ffe0e0",
                        dark = "#ffe0e0"
                    )
                )
            )
        )
    }
}

@Preview
@Composable
fun MonsterSection1ItemPreview() = HunterTheme {
    Surface {
        MonsterSection(
            leftMonster = MonsterCardItem(
                index = "asdasdasd",
                type = MonsterType.ABERRATION,
                challengeRating = 8.0f,
                name = "Monster of monsters",
                imageData = MonsterImageData(
                    url = "sadasd",
                    backgroundColor = Color(
                        light = "#ffe0e0",
                        dark = "#ffe0e0"
                    )
                )
            )
        )
    }
}
