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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.spell.registration.SpellFormState
import br.alexandregpereira.hunter.spell.registration.SpellRegistrationState
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppScreen
import br.alexandregpereira.hunter.ui.compose.AppTopBar

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
    showCloseButton = false,
    onClose = onClose,
) {
    val density = LocalDensity.current
    val listState = rememberLazyListState()
    var topBarHeight by remember { mutableStateOf(0.dp) }
    Box {
        Column {
            SpellRegistrationForm(
                spell = state.spell,
                strings = state.strings,
                listState = listState,
                contentPadding = PaddingValues(top = topBarHeight),
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

        AppTopBar(
            title = if (state.isEditing) state.strings.editSpell else state.strings.addSpell,
            listState = listState,
            onCloseClick = onClose,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .onSizeChanged { size ->
                    topBarHeight = with(density) { size.height.toDp() }
                },
        )
    }
}
