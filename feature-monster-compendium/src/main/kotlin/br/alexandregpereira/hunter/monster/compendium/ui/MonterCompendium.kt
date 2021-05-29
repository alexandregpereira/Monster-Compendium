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

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.monster.compendium.MonsterCardItemsBySection
import br.alexandregpereira.hunter.ui.compose.MonsterItemType
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun MonsterCompendium(
    monstersBySection: MonsterCardItemsBySection,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onItemCLick: (index: String) -> Unit = {},
) {
    LazyColumn(state = listState) {

        val verticalSectionPadding = 24.dp
        val lastIndex = monstersBySection.entries.size - 1
        monstersBySection.entries.forEachIndexed { index, monsterSectionEntry ->
            if (index > 0) {
                item {
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                    )
                }
            }
            item {
                Surface {
                    Text(
                        text = monsterSectionEntry.key.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp, top = verticalSectionPadding)
                    )
                }
            }

            val monsterLastRowIndex = monsterSectionEntry.value.size - 1
            monsterSectionEntry.value.forEachIndexed { monsterRowIndex, monsterEntry ->
                item {
                    val leftMonster = monsterEntry.first
                    val rightMonster = monsterEntry.second

                    val contentPaddingValue = when (index) {
                        0 -> contentPadding.calculateTopPadding()
                        lastIndex -> contentPadding.calculateBottomPadding()
                        else -> 0.dp
                    }
                    val topPadding = 0.dp

                    val modifier = when (monsterRowIndex) {
                        0 -> Modifier.padding(top = topPadding + contentPaddingValue)
                        monsterLastRowIndex -> {
                            if (index == lastIndex) {
                                Modifier.padding(bottom = verticalSectionPadding + contentPaddingValue)
                            } else {
                                Modifier.padding(bottom = verticalSectionPadding)
                            }
                        }
                        else -> Modifier
                    }

                    MonsterSection(
                        leftMonster = leftMonster,
                        rightMonster = rightMonster,
                        onItemClick = onItemCLick,
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
fun MonsterSection(
    leftMonster: MonsterPreview,
    modifier: Modifier = Modifier,
    rightMonster: MonsterPreview? = null,
    onItemClick: (index: String) -> Unit = {},
) = Surface {
    Row(
        modifier
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
}

@Composable
private fun MonsterCard(
    monster: MonsterPreview,
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
                MonsterSection(title = "Any") to (0..10).map {
                    val leftMonster = MonsterPreview(
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
                    val rightMonster = if (it == 1) {
                        MonsterPreview(
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
                }
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
                    val leftMonster = MonsterPreview(
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
                    val rightMonster = if (it == 1) {
                        MonsterPreview(
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
                }
            )
        )
    }
}

@Preview
@Composable
fun MonsterSection2ItemsPreview() = HunterTheme {
    Surface {
        MonsterSection(
            leftMonster = MonsterPreview(
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
            rightMonster = MonsterPreview(
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
            leftMonster = MonsterPreview(
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
