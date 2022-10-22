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

package br.alexandregpereira.hunter.monster.compendium.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import br.alexandregpereira.hunter.ui.compose.MonsterCard
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun MonsterCompendium(
    monstersBySection: Map<SectionState, List<MonsterRowState>>,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onItemCLick: (index: String) -> Unit = {},
    onItemLongCLick: (index: String) -> Unit = {},
) = LazyColumn(state = listState, contentPadding = contentPadding) {

    monstersBySection.entries.forEach { monsterSectionEntry ->
        val monsterSection = monsterSectionEntry.key
        val monsterRows = monsterSectionEntry.value

        val sectionTitlePaddingTop = 32.dp
        val sectionTitlePaddingBottom = 16.dp
        item {
            monsterSection.parentTitle?.let {
                SectionTitle(
                    title = it,
                    isHeader = true,
                    modifier = Modifier.padding(
                        top = sectionTitlePaddingTop,
                        bottom = sectionTitlePaddingBottom
                    )
                )
            }
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
                val leftMonster = monsterRow.leftMonsterCardState
                val rightMonster = monsterRow.rightMonsterCardState

                MonsterRow(
                    leftMonster = leftMonster,
                    rightMonster = rightMonster,
                    onItemClick = onItemCLick,
                    onItemLongCLick = onItemLongCLick,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun MonsterRow(
    leftMonster: MonsterCardState,
    modifier: Modifier = Modifier,
    rightMonster: MonsterCardState? = null,
    onItemClick: (index: String) -> Unit = {},
    onItemLongCLick: (index: String) -> Unit = {},
) = Surface {
    Row(modifier) {

        MonsterCard(
            name = leftMonster.name,
            url = leftMonster.imageState.url,
            iconRes = leftMonster.imageState.type.iconRes,
            backgroundColor = leftMonster.imageState.backgroundColor.getColor(isSystemInDarkTheme()),
            challengeRating = leftMonster.imageState.challengeRating,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            onCLick = { onItemClick(leftMonster.index) },
            onLongCLick = { onItemLongCLick(leftMonster.index) }
        )
        rightMonster?.let {
            MonsterCard(
                name = it.name,
                url = it.imageState.url,
                iconRes = it.imageState.type.iconRes,
                backgroundColor = it.imageState.backgroundColor.getColor(isSystemInDarkTheme()),
                challengeRating = it.imageState.challengeRating,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                onCLick = { onItemClick(it.index) },
                onLongCLick = { onItemLongCLick(it.index) }
            )
        }
    }
}

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

@Preview
@Composable
fun MonsterCompendiumPreview() = Window {
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
        monstersBySection = mapOf(
            SectionState(title = "Any") to (0..10).map {
                val leftMonster = MonsterCardState(
                    index = "asdasdasd",
                    name = "Monster of monsters",
                    imageState = imageState,
                )
                val rightMonster = if (it == 1) {
                    MonsterCardState(
                        index = "asdasdasd",
                        name = "Monster of monsters",
                        imageState = imageState,
                    )
                } else {
                    null
                }
                leftMonster and rightMonster
            }
        )
    )
}

@Preview
@Composable
fun MonsterCompendiumWithSectionTitlePreview() = Window {
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
        monstersBySection = mapOf(
            SectionState(title = "Title") to (0..10).map {
                val leftMonster = MonsterCardState(
                    index = "asdasdasd",
                    name = "Monster of monsters",
                    imageState = imageState,
                )
                val rightMonster = if (it == 1) {
                    MonsterCardState(
                        index = "asdasdasd",
                        name = "Monster of monsters",
                        imageState = imageState,
                    )
                } else {
                    null
                }
                leftMonster and rightMonster
            }
        )
    )
}

@Preview
@Composable
fun MonsterSection2ItemsPreview() = Window {
    val imageState = MonsterImageState(
        url = "",
        type = MonsterTypeState.ABERRATION,
        challengeRating = 8.0f,
        backgroundColor = ColorState(
            light = "#ffe0e0",
            dark = "#ffe0e0"
        )
    )
    MonsterRow(
        leftMonster = MonsterCardState(
            index = "asdasdasd",
            name = "Monster of monsters",
            imageState = imageState
        ),
        rightMonster = MonsterCardState(
            index = "asdasdasd",
            name = "Monster of monsters",
            imageState = imageState
        )
    )
}

@Preview
@Composable
fun MonsterSection1ItemPreview() = Window {
    val imageState = MonsterImageState(
        url = "",
        type = MonsterTypeState.ABERRATION,
        challengeRating = 8.0f,
        backgroundColor = ColorState(
            light = "#ffe0e0",
            dark = "#ffe0e0"
        )
    )
    MonsterRow(
        leftMonster = MonsterCardState(
            index = "asdasdasd",
            name = "Monster of monsters",
            imageState = imageState
        )
    )
}
