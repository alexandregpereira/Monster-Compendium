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

package br.alexandregpereira.hunter.spell.registration.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.spell.registration.SpellFormState
import br.alexandregpereira.hunter.spell.registration.SpellRegistrationState
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppScreen

@Composable
internal fun SpellRegistrationScreen(
    state: SpellRegistrationState,
    contentPadding: PaddingValues = PaddingValues(),
    onSpellChanged: (SpellFormState) -> Unit = {},
    onSave: () -> Unit = {},
    onClose: () -> Unit = {},
) = AppScreen(
    isOpen = state.isOpen,
    contentPaddingValues = contentPadding,
    swipeTriggerPercentage = .7f,
    onClose = onClose,
) {
    Column {
        SpellRegistrationForm(
            spell = state.spell,
            isEditing = state.isEditing,
            strings = state.strings,
            modifier = Modifier.weight(1f),
            onSpellChanged = onSpellChanged,
        )

        AppButton(
            text = state.strings.save,
            enabled = state.isSaveEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = onSave,
        )
    }
}
