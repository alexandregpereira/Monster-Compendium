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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.settings.AppearanceSettingsState
import br.alexandregpereira.hunter.settings.SettingsStrings
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.AppSwitch
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.ColorTextField
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.PickerField

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

        val monsterImageContentScaleOptions = state.monsterImageContentScaleOptions
        PickerField(
            label = strings.monsterImageContentScale,
            options = remember(monsterImageContentScaleOptions, strings) {
                monsterImageContentScaleOptions.map { it.asString(strings) }
            },
            value = state.monsterImageContentSelected.asString(strings),
            onValueChange = {
                onStateChange(state.copy(monsterImageContentSelectedOptionIndex = it))
            },
        )

        AppButton(
            text = strings.save,
            onClick = onSaveButtonClick,
            modifier = Modifier.padding(top = 40.dp)
        )
    }
}

private fun AppImageContentScale.asString(strings: SettingsStrings): String = when (this) {
    AppImageContentScale.Fit -> strings.imageContentScaleFit
    AppImageContentScale.Crop -> strings.imageContentScaleCrop
}
