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

package br.alexandregpereira.hunter.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import br.alexandregpereira.hunter.detail.ui.MonsterDetailOptionPicker
import br.alexandregpereira.hunter.detail.ui.strings
import br.alexandregpereira.hunter.monster.detail.MonsterDetailStateHolder
import br.alexandregpereira.hunter.ui.compose.ConfirmationBottomSheet
import br.alexandregpereira.hunter.ui.compose.FormBottomSheet
import br.alexandregpereira.hunter.ui.compose.FormField
import org.koin.compose.koinInject

@Composable
fun MonsterDetailBottomSheets(
    contentPadding: PaddingValues = PaddingValues(),
) {
    val stateHolder: MonsterDetailStateHolder = koinInject()
    val state by stateHolder.state.collectAsState()

    val maxWidth: Dp = Dp.Unspecified
    val widthFraction = .3f

    MonsterDetailOptionPicker(
        options = state.options,
        showOptions = state.showOptions,
        maxWidth = maxWidth,
        widthFraction = widthFraction,
        contentPadding = contentPadding,
        onOptionSelected = stateHolder::onOptionClicked,
        onClosed = stateHolder::onShowOptionsClosed,
    )

    FormBottomSheet(
        title = strings.clone,
        formFields = listOf(
            FormField.Text(
                key = "monsterName",
                label = strings.cloneMonsterName,
                value = state.monsterCloneName,
            )
        ),
        buttonText = strings.save,
        opened = state.showCloneForm,
        buttonEnabled = state.monsterCloneName.isNotBlank(),
        maxWidth = maxWidth,
        widthFraction = widthFraction,
        contentPadding = contentPadding,
        onFormChanged = remember(stateHolder) {
            { stateHolder.onCloneFormChanged(it.stringValue) }
        },
        onClosed = stateHolder::onCloneFormClosed,
        onSaved = stateHolder::onCloneFormSaved,
    )

    ConfirmationBottomSheet(
        show = state.showDeleteConfirmation,
        description = strings.deleteQuestion,
        buttonText = strings.deleteConfirmation,
        maxWidth = maxWidth,
        widthFraction = widthFraction,
        contentPadding = contentPadding,
        onConfirmed = stateHolder::onDeleteConfirmed,
        onClosed = stateHolder::onDeleteClosed
    )

    ConfirmationBottomSheet(
        show = state.showResetConfirmation,
        description = strings.resetQuestion,
        buttonText = strings.resetConfirmation,
        maxWidth = maxWidth,
        widthFraction = widthFraction,
        contentPadding = contentPadding,
        onConfirmed = stateHolder::onResetConfirmed,
        onClosed = stateHolder::onResetClosed
    )
}