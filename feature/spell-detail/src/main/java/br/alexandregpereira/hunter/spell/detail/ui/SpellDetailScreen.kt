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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.alexandregpereira.hunter.spell.detail.SpellDetailViewState
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun SpellDetailScreen(
    state: SpellDetailViewState,
    contentPadding: PaddingValues = PaddingValues(),
    onClose: () -> Unit = {}
) = HunterTheme {
    BottomSheet(opened = state.showDetail, contentPadding = contentPadding, onClose = onClose) {
        state.spell?.let { SpellDetail(it) }
    }
}

@Preview
@Composable
private fun SpellDetailScreenPreview() = HunterTheme {
    SpellDetailScreen(
        SpellDetailViewState(
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
                school = SchoolOfMagicState.ABJURATION,
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
    )
}
