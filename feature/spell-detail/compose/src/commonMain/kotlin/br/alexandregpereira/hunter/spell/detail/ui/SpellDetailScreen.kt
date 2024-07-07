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
