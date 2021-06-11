/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
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
import androidx.compose.ui.unit.Dp
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
) = LazyColumn(state = listState) {

    val lastIndex = monstersBySection.entries.size - 1
    monstersBySection.entries.forEachIndexed { index, monsterSectionEntry ->
        val monsterSection = monsterSectionEntry.key
        val monsterRows = monsterSectionEntry.value

        if (index == 0) {
            item {
                TopBottomSpace(height = contentPadding.calculateTopPadding())
            }
        }

        val sectionTitlePaddingTop = 32.dp
        val sectionTitlePaddingBottom = 16.dp
        monsterSection.parentTitle?.let {
            item {
                SectionTitle(
                    title = it,
                    isHeader = true,
                    modifier = Modifier.padding(
                        top = sectionTitlePaddingTop,
                        bottom = sectionTitlePaddingBottom
                    )
                )
            }
        }

        item {
            val paddingTop = when {
                monsterSection.parentTitle != null -> 0.dp
                monsterSection.isHeader -> sectionTitlePaddingTop
                else -> 24.dp
            }
            SectionTitle(
                title = monsterSection.title,
                isHeader = monsterSection.isHeader && monsterSection.parentTitle == null,
                modifier = Modifier.padding(top = paddingTop, bottom = sectionTitlePaddingBottom)
            )
        }

        monsterRows.forEach { monsterRow ->
            item {
                val leftMonster = monsterRow.first
                val rightMonster = monsterRow.second

                MonsterRow(
                    leftMonster = leftMonster,
                    rightMonster = rightMonster,
                    onItemClick = onItemCLick,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }

        if (index == lastIndex) {
            item {
                TopBottomSpace(height = contentPadding.calculateBottomPadding())
            }
        }
    }
}

@Composable
fun MonsterRow(
    leftMonster: MonsterPreview,
    modifier: Modifier = Modifier,
    rightMonster: MonsterPreview? = null,
    onItemClick: (index: String) -> Unit = {},
) = Surface {
    Row(modifier) {

        MonsterCard(
            monster = leftMonster,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            onCLick = { onItemClick(leftMonster.index) }
        )
        rightMonster?.let {
            MonsterCard(
                monster = it,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
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

@Composable
private fun SectionTitle(
    title: String,
    isHeader: Boolean,
    modifier: Modifier = Modifier
) {
    val fontSize = if (isHeader) 40.sp else 24.sp
    Surface {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun TopBottomSpace(
    height: Dp
) {
    Surface {
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(height)
        )
    }
}

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
        MonsterRow(
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
        MonsterRow(
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
