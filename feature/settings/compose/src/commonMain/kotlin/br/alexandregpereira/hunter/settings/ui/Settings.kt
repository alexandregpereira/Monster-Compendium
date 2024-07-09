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
