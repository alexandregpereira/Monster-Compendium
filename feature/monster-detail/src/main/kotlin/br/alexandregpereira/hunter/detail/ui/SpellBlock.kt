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

package br.alexandregpereira.hunter.detail.ui

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState
import br.alexandregpereira.hunter.ui.compose.SpellIconInfo
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.util.toColor

@Composable
fun SpellBlock(
    spellcastings: List<SpellcastingState>,
    modifier: Modifier = Modifier,
    onSpellClicked: (String) -> Unit = {}
) = AbilityDescriptionBlock(
    title = stringResource(R.string.monster_detail_spells),
    abilityDescriptions = spellcastings.map {
        AbilityDescriptionState(
            name = stringResource(it.type.nameRes),
            description = it.description
        )
    },
    modifier = modifier
) { index ->
    spellcastings[index].spellsByGroup.forEach { entry ->
        val group = entry.key
        val spells = entry.value

        Spacer(modifier = Modifier.height(24.dp))
        Spells(group = group, spells = spells, onSpellClicked = onSpellClicked)
    }
}


@Composable
private fun Spells(
    group: String,
    spells: List<SpellPreviewState>,
    modifier: Modifier = Modifier,
    onSpellClicked: (String) -> Unit = {}
) = Column(modifier = modifier) {
    Text(
        text = group,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 14.sp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))

//    LazyRow(
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        contentPadding = PaddingValues(horizontal = 16.dp)
//    ) {
//        items(spells) { spell ->
//            SpellIconInfo(
//                name = spell.name,
//                school = spell.school,
//                onClick = { onSpellClicked(spell.index) }
//            )
//        }
//    }
}

@Preview
@Composable
private fun SpellBlockPreview() = Window {
    SpellBlock(
        spellcastings = listOf(
            SpellcastingState(
                type = SpellcastingTypeState.SPELLCASTER,
                description = "The couatl's spellcasting ability is Charisma (spell save DC 14). It can innately cast the following spells, requiring only verbal components:",
                spellsByGroup = mapOf(
                    "At Will" to listOf(
                        SpellPreviewState(
                            index = "index",
                            name = "Detect Evil and Good",
                            school = SchoolOfMagicState.DIVINATION
                        ),
                        SpellPreviewState(
                            index = "index",
                            name = "Some Magic",
                            school = SchoolOfMagicState.CONJURATION
                        ),
                    ),
                    "3/day each" to (0..10).map {
                        SpellPreviewState(
                            index = "index",
                            name = "Some Magic $it",
                            school = SchoolOfMagicState.ILLUSION
                        )
                    }
                )
            ),
            SpellcastingState(
                type = SpellcastingTypeState.INNATE,
                description = "The couatl's spellcasting ability is Charisma (spell save DC 14). It can innately cast the following spells, requiring only verbal components:",
                spellsByGroup = mapOf(
                    "At Will" to listOf(
                        SpellPreviewState(
                            index = "index",
                            name = "Detect Evil and Good",
                            school = SchoolOfMagicState.ABJURATION
                        ),
                        SpellPreviewState(
                            index = "index",
                            name = "Some Magic",
                            school = SchoolOfMagicState.NECROMANCY
                        ),
                    )
                )
            )
        ),
    )
}

@Preview
@Composable
private fun SchoolsOfMagicPreview() = Window {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 4),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(SchoolOfMagicState.values()) { school ->
            val iconColor =
                if (isSystemInDarkTheme()) school.iconColorDark else school.iconColorLight
            IconInfo(
                title = school.name,
                painter = painterResource(school.iconRes),
                iconColor = iconColor.toColor()
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SchoolsOfMagicDarkThemePreview() = Window {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 4),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(SchoolOfMagicState.values()) { school ->
            val iconColor =
                if (isSystemInDarkTheme()) school.iconColorDark else school.iconColorLight
            IconInfo(
                title = school.name,
                painter = painterResource(school.iconRes),
                iconColor = iconColor.toColor()
            )
        }
    }
}
