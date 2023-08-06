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
