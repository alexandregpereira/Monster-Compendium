/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.spell.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.spell.detail.SpellDetailViewState
import br.alexandregpereira.hunter.spell.detail.SpellState
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun SpellDetailScreen(
    state: SpellDetailViewState,
    contentPadding: PaddingValues = PaddingValues(),
    onClose: () -> Unit = {}
) = HunterTheme {
    BottomSheet(opened = state.showDetail, contentPadding = contentPadding, onClose = onClose) {
        CompositionLocalProvider(LocalStrings provides state.strings) {
            SpellDetail(state.spell)
        }
    }
}

@Composable
private fun SpellDetail(
    spell: SpellState
) = SelectionContainer {
    Column {
        SpellHeader(
            title = spell.name,
            subtitle = spell.subtitle,
            schoolIcon = spell.school.toUiState(),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        SpellInfoGrid(
            castingTime = spell.castingTime,
            range = spell.range,
            components = spell.components,
            duration = spell.duration,
            savingThrowType = spell.savingThrowType,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp, top = 16.dp)
        )

        SpellDescription(
            description = spell.description,
            higherLevel = spell.higherLevel,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
                .padding(bottom = 16.dp)
        )
    }
}

private fun SchoolOfMagic.toUiState() = when (this) {
    SchoolOfMagic.ABJURATION -> SchoolOfMagicState.ABJURATION
    SchoolOfMagic.CONJURATION -> SchoolOfMagicState.CONJURATION
    SchoolOfMagic.DIVINATION -> SchoolOfMagicState.DIVINATION
    SchoolOfMagic.ENCHANTMENT -> SchoolOfMagicState.ENCHANTMENT
    SchoolOfMagic.EVOCATION -> SchoolOfMagicState.EVOCATION
    SchoolOfMagic.ILLUSION -> SchoolOfMagicState.ILLUSION
    SchoolOfMagic.NECROMANCY -> SchoolOfMagicState.NECROMANCY
    SchoolOfMagic.TRANSMUTATION -> SchoolOfMagicState.TRANSMUTATION
}

@Preview
@Composable
private fun SpellDetailPreview() = HunterTheme {
    SpellDetail(
        spell = SpellState(
            index = "index",
            name = "Detect Good and Evil",
            subtitle = "Level 1",
            castingTime = "castingTime",
            components = "components",
            duration = "duration",
            range = "range",
            concentration = true,
            savingThrowType = "Something",
            school = SchoolOfMagic.EVOCATION,
            description = "description description description description description deiption" +
                    "descriptiondesc riptionde scriptiondescriptiondescription descriptionstion" +
                    "descriptiondesc riptionde scriptiondescriptiondescription descriptiription" +
                    "descriptiondesc riptionde scriptiondescriptiondescription descriptioiption" +
                    "dasd asda sd asd asd as as d as",
            higherLevel = "description description description description description descgion" +
                    "descriptiondesc riptionde scriptiondescriptiondescription descriptioiption" +
                    "descriptiondesc riptionde scriptiondescriptiondescription descriptcription"
        )
    )
}
