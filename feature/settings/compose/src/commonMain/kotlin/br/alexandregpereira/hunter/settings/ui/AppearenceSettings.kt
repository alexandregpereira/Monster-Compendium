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
import br.alexandregpereira.hunter.settings.AppearanceSettingsState
import br.alexandregpereira.hunter.settings.SettingsStrings
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppSwitch
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.ColorTextField
import br.alexandregpereira.hunter.ui.compose.Form

@Composable
internal fun AppearanceSettingsBottomSheet(
    opened: Boolean,
    state: AppearanceSettingsState,
    strings: SettingsStrings,
    contentPadding: PaddingValues = PaddingValues(),
    onStateChange: (AppearanceSettingsState) -> Unit = {},
    onSaveButtonClick: () -> Unit = {},
    onClose: () -> Unit = {},
) = BottomSheet(
    opened = opened,
    onClose = onClose,
    contentPadding = contentPadding
) {
    Form(
        title = strings.appearanceSettingsTitle,
        modifier = Modifier.padding(16.dp),
    ) {
        AppSwitch(
            label = strings.forceLightImageBackground,
            checked = state.forceLightImageBackground,
            onCheckedChange = {
                onStateChange(state.copy(forceLightImageBackground = it))
            },
        )

        ColorTextField(
            label = strings.defaultLightBackground,
            text = state.defaultLightBackground,
            onValueChange = {
                onStateChange(state.copy(defaultLightBackground = it))
            },
        )

        ColorTextField(
            label = strings.defaultDarkBackground,
            text = state.defaultDarkBackground,
            enabled = state.defaultDarkBackgroundEnabled,
            onValueChange = {
                onStateChange(state.copy(defaultDarkBackground = it))
            },
        )

        AppButton(
            text = strings.save,
            onClick = onSaveButtonClick,
            modifier = Modifier.padding(top = 40.dp)
        )
    }
}
