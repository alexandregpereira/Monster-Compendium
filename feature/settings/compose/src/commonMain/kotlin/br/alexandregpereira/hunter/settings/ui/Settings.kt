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

package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.settings.SettingsEnStrings
import br.alexandregpereira.hunter.settings.SettingsLanguageState
import br.alexandregpereira.hunter.settings.SettingsState
import br.alexandregpereira.hunter.settings.SettingsStrings
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.PickerField
import br.alexandregpereira.hunter.ui.compose.Window
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun SettingsBottomSheet(
    settingsOpened: Boolean,
    state: SettingsState,
    strings: SettingsStrings,
    contentPadding: PaddingValues = PaddingValues(),
    onLanguageChange: (SettingsLanguageState) -> Unit = {},
    onSaveButtonClick: () -> Unit = {},
    onClose: () -> Unit = {},
) = BottomSheet(
    opened = settingsOpened,
    onClose = onClose,
    contentPadding = contentPadding
) {
    Settings(
        state = state,
        strings = strings,
        onLanguageChange = onLanguageChange,
        onSaveButtonClick = onSaveButtonClick
    )
}

@Composable
private fun Settings(
    state: SettingsState,
    strings: SettingsStrings,
    modifier: Modifier = Modifier,
    onLanguageChange: (SettingsLanguageState) -> Unit = {},
    onSaveButtonClick: () -> Unit = {}
) = Form(
    title = strings.settingsTitle,
    modifier = modifier
        .padding(16.dp),
) {

    PickerField(
        label = strings.languageLabel,
        value = state.language.value,
        options = state.languages.map { it.value },
        onValueChange = { i -> onLanguageChange(state.languages[i]) },
    )

    AppButton(
        text = strings.save,
        onClick = onSaveButtonClick,
        modifier = Modifier.padding(top = 40.dp)
    )
}

@Preview
@Composable
private fun SettingsPreview() = Window {
    Settings(
        state = SettingsState(
            language = SettingsLanguageState(value = "English"),
            languages = listOf(
                SettingsLanguageState(value = "English"),
                SettingsLanguageState(value = "Portuguese")
            )
        ),
        strings = SettingsEnStrings(),
    )
}
