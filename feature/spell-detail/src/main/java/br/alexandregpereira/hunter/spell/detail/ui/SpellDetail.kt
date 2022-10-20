/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun SpellDetail(
    spell: SpellState
) = SelectionContainer {
    Column {
        SpellHeader(spell = spell)

        SpellInfoGrid(
            spell = spell,
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

@Preview
@Composable
private fun SpellDetailPreview() = HunterTheme {
    SpellDetail(
        spell = SpellState(
            index = "index",
            name = "Detect Good and Evil",
            level = 1,
            castingTime = "castingTime",
            components = "components",
            duration = "duration",
            range = "range",
            ritual = true,
            concentration = true,
            savingThrowType = SavingThrowTypeState.CONSTITUTION,
            school = SchoolOfMagicState.TRANSMUTATION,
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
