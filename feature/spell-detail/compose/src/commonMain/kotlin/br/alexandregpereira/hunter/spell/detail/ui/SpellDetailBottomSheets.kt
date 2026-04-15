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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.spell.detail.SpellDetailOption
import br.alexandregpereira.hunter.spell.detail.SpellDetailOptionState
import br.alexandregpereira.hunter.spell.detail.SpellDetailViewState
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.ConfirmationBottomSheet
import br.alexandregpereira.hunter.ui.compose.FormBottomSheet
import br.alexandregpereira.hunter.ui.compose.FormField
import br.alexandregpereira.hunter.ui.compose.animatePressed

@Composable
internal fun SpellDetailBottomSheets(
    state: SpellDetailViewState,
    contentPadding: PaddingValues = PaddingValues(),
    onOptionsClosed: () -> Unit = {},
    onOptionClicked: (SpellDetailOption) -> Unit = {},
    onCloneFormChanged: (String) -> Unit = {},
    onCloneFormClosed: () -> Unit = {},
    onCloneFormSaved: () -> Unit = {},
    onDeleteConfirmed: () -> Unit = {},
    onDeleteClosed: () -> Unit = {},
    onResetConfirmed: () -> Unit = {},
    onResetClosed: () -> Unit = {},
) {
    SpellDetailOptionPicker(
        options = state.options,
        showOptions = state.showOptions,
        optionsTitle = state.strings.options,
        contentPadding = contentPadding,
        onOptionSelected = { onOptionClicked(it.option) },
        onClosed = onOptionsClosed,
    )

    FormBottomSheet(
        title = state.strings.clone,
        formFields = listOf(
            FormField.Text(
                key = "spellName",
                label = state.strings.cloneSpellName,
                value = state.spellCloneName,
            )
        ),
        buttonText = state.strings.save,
        opened = state.showCloneForm,
        buttonEnabled = state.spellCloneName.isNotBlank(),
        contentPadding = contentPadding,
        onFormChanged = remember(onCloneFormChanged) {
            { onCloneFormChanged(it.stringValue) }
        },
        onClosed = onCloneFormClosed,
        onSaved = onCloneFormSaved,
    )

    ConfirmationBottomSheet(
        show = state.showDeleteConfirmation,
        description = state.strings.deleteConfirmation,
        buttonText = state.strings.delete,
        contentPadding = contentPadding,
        onConfirmed = onDeleteConfirmed,
        onClosed = onDeleteClosed,
    )

    ConfirmationBottomSheet(
        show = state.showResetConfirmation,
        description = state.strings.resetConfirmation,
        buttonText = state.strings.resetToOriginal,
        contentPadding = contentPadding,
        onConfirmed = onResetConfirmed,
        onClosed = onResetClosed,
    )
}

@Composable
private fun SpellDetailOptionPicker(
    options: List<SpellDetailOptionState>,
    showOptions: Boolean,
    optionsTitle: String,
    contentPadding: PaddingValues = PaddingValues(),
    onOptionSelected: (SpellDetailOptionState) -> Unit = {},
    onClosed: () -> Unit = {},
) = BottomSheet(
    opened = showOptions,
    onClose = onClosed,
) {
    Column {
        Text(
            text = optionsTitle,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(all = 16.dp)
        )

        options.forEach { optionState ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
                    .animatePressed(onClick = { onOptionSelected(optionState) })
            ) {
                Text(
                    text = optionState.label,
                    fontSize = 16.sp,
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(contentPadding.calculateBottomPadding() + 16.dp)
                .fillMaxWidth()
        )
    }
}
