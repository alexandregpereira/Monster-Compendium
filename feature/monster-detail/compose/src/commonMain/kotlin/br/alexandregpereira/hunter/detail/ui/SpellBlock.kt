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

@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.monster.detail.MonsterState
import br.alexandregpereira.hunter.monster.detail.SpellPreviewState
import br.alexandregpereira.hunter.monster.detail.SpellcastingState
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState
import br.alexandregpereira.hunter.ui.compose.SpellIconInfo
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.transition.getPageOffset
import br.alexandregpereira.hunter.ui.transition.getTransitionData
import br.alexandregpereira.hunter.ui.util.toColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

internal fun LazyListScope.spellBlock(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    getItemsKeys: () -> List<Any> = { emptyList() },
    onSpellClicked: (String) -> Unit = {}
) {
    val transitionData = getTransitionData(monsters, getPageOffset = { pagerState.getPageOffset() })
    transitionData.data.spellcastings.forEachIndexed { i, spellcasting ->
        item(key = "$SPELLCASTING_ITEM_KEY$i") {
            MonsterSectionAlphaTransition(
                dataList = monsters,
                pagerState = pagerState,
                getItemsKeys = getItemsKeys,
                modifier = Modifier.animateItemPlacement()
            ) {
                SpellBlock(spellcasting = spellcasting, index = i)
            }
        }

        spellcasting.spellsByGroup.toList().forEachIndexed { j, (group, spells) ->
            item(key = "$SPELLCASTING_ITEM_KEY-group$i-$j") {
                MonsterSectionAlphaTransition(
                    dataList = monsters,
                    pagerState = pagerState,
                    getItemsKeys = getItemsKeys,
                    modifier = Modifier.animateItemPlacement()
                ) {
                    Spells(group = group, spells = spells, onSpellClicked = onSpellClicked)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun SpellBlock(
    spellcasting: SpellcastingState,
    index: Int,
) = Column {
    if (index == 0) {
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
        )
    }

    if (index == 0 ) {
        BlockTitle(
            title = strings.spells,
            modifier = Modifier.padding(top = 16.dp)
        )
    }

    val paddingTop = if (index == 0 ) 16.dp else 0.dp
    AbilityDescription(
        name = spellcasting.name,
        description = spellcasting.description,
        modifier = Modifier.padding(top = paddingTop, bottom = 16.dp)
    )
}

@Composable
private fun Spells(
    group: String,
    spells: List<SpellPreviewState>,
    onSpellClicked: (String) -> Unit = {}
) {
    Text(
        text = group,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 14.sp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(spells) { spell ->
            SpellIconInfo(
                name = spell.name,
                school = spell.school.asState(),
                onClick = { onSpellClicked(spell.index) }
            )
        }
    }
}

private fun SchoolOfMagic.asState(): SchoolOfMagicState {
    return when (this) {
        SchoolOfMagic.ABJURATION -> SchoolOfMagicState.ABJURATION
        SchoolOfMagic.CONJURATION -> SchoolOfMagicState.CONJURATION
        SchoolOfMagic.DIVINATION -> SchoolOfMagicState.DIVINATION
        SchoolOfMagic.ENCHANTMENT -> SchoolOfMagicState.ENCHANTMENT
        SchoolOfMagic.EVOCATION -> SchoolOfMagicState.EVOCATION
        SchoolOfMagic.ILLUSION -> SchoolOfMagicState.ILLUSION
        SchoolOfMagic.NECROMANCY -> SchoolOfMagicState.NECROMANCY
        SchoolOfMagic.TRANSMUTATION -> SchoolOfMagicState.TRANSMUTATION
    }
}

internal const val SPELLCASTING_ITEM_KEY = "spellcasting"

@Preview
@Composable
private fun SpellBlockPreview() = Window {
    val spellcastings = listOf(
        SpellcastingState(
            name = "Spellcaster",
            description = "The couatl's spellcasting ability is Charisma (spell save DC 14). It can innately cast the following spells, requiring only verbal components:",
            spellsByGroup = mapOf(
                "At Will" to listOf(
                    SpellPreviewState(
                        index = "index",
                        name = "Detect Evil and Good",
                        school = SchoolOfMagic.DIVINATION,
                    ),
                    SpellPreviewState(
                        index = "index",
                        name = "Some Magic",
                        school = SchoolOfMagic.CONJURATION
                    ),
                ),
                "3/day each" to (0..10).map {
                    SpellPreviewState(
                        index = "index",
                        name = "Some Magic $it",
                        school = SchoolOfMagic.ILLUSION
                    )
                }
            )
        ),
        SpellcastingState(
            name = "Innate Spellcaster",
            description = "The couatl's spellcasting ability is Charisma (spell save DC 14). It can innately cast the following spells, requiring only verbal components:",
            spellsByGroup = mapOf(
                "At Will" to listOf(
                    SpellPreviewState(
                        index = "index",
                        name = "Detect Evil and Good",
                        school = SchoolOfMagic.ABJURATION
                    ),
                    SpellPreviewState(
                        index = "index",
                        name = "Some Magic",
                        school = SchoolOfMagic.NECROMANCY
                    ),
                )
            )
        )
    )

    val pagerState = rememberPagerState(
        pageCount = { spellcastings.size }
    )

    LazyColumn {
        spellBlock(
            monsters = listOf(
                MonsterState(
                    spellcastings = spellcastings,
                )
            ),
            pagerState = pagerState
        )
    }
}

@Preview
@Composable
private fun SchoolsOfMagicPreview() = Window {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 4),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(SchoolOfMagic.entries.toTypedArray()) { school ->
            val iconColor =
                if (isSystemInDarkTheme()) {
                    school.asState().iconColorDark
                } else school.asState().iconColorLight
            IconInfo(
                title = school.name,
                painter = painterResource(school.asState().icon),
                iconColor = iconColor.toColor()
            )
        }
    }
}

@Composable
private fun SchoolsOfMagicDarkThemePreview() = PreviewWindow(darkTheme = true) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 4),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(SchoolOfMagic.entries.toTypedArray()) { school ->
            val iconColor = school.asState().iconColorDark
            IconInfo(
                title = school.name,
                painter = painterResource(school.asState().icon),
                iconColor = iconColor.toColor()
            )
        }
    }
}
