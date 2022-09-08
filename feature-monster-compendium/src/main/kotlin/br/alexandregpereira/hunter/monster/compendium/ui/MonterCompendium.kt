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
import br.alexandregpereira.hunter.ui.compose.ColorState
import br.alexandregpereira.hunter.ui.compose.MonsterImageState
import br.alexandregpereira.hunter.ui.compose.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun MonsterCompendium(
    monstersBySection: Map<SectionState, List<MonsterRowState>>,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onItemCLick: (index: String) -> Unit = {},
) = LazyColumn(state = listState) {

    val lastIndex = monstersBySection.entries.size - 1
    monstersBySection.entries.forEachIndexed { index, monsterSectionEntry ->
        val monsterSection = monsterSectionEntry.key
        val monsterRows = monsterSectionEntry.value

        val sectionTitlePaddingTop = 32.dp
        val sectionTitlePaddingBottom = 16.dp
        item {
            if (index == 0) {
                TopBottomSpace(height = contentPadding.calculateTopPadding())
            }
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
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )

                if (index == lastIndex) {
                    TopBottomSpace(height = contentPadding.calculateBottomPadding())
                }
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
) = Surface {
    Row(modifier) {

        MonsterCard(
            monsterCardState = leftMonster,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            onCLick = { onItemClick(leftMonster.index) }
        )
        rightMonster?.let {
            MonsterCard(
                monsterCardState = it,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                onCLick = { onItemClick(it.index) }
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
