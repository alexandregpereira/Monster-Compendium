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

package br.alexandregpereira.hunter.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import br.alexandregpereira.hunter.settings.ui.AdvancedSettings
import br.alexandregpereira.hunter.settings.ui.AppearanceSettingsBottomSheet
import br.alexandregpereira.hunter.settings.ui.DonateScreen
import br.alexandregpereira.hunter.settings.ui.SettingsBottomSheet
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import org.koin.compose.koinInject

@Composable
fun SettingsBottomSheets(
    contentPadding: PaddingValues = PaddingValues(),
) {
    val stateHolder: SettingsStateHolder = koinInject()
    val state by stateHolder.state.collectAsState()
    BottomSheet(
        opened = state.advancedSettingsOpened,
        contentPadding = contentPadding,
        onClose = stateHolder::onAdvancedSettingsCloseClick,
    ) {
        AdvancedSettings(
            imageBaseUrl = state.imageBaseUrl,
            alternativeSourceBaseUrl = state.alternativeSourceBaseUrl,
            saveButtonEnabled = state.saveButtonEnabled,
            strings = state.strings,
            onImageBaseUrlChange = stateHolder::onImageBaseUrlChange,
            onAlternativeSourceBaseUrlChange = stateHolder::onAlternativeSourceBaseUrlChange,
            onSaveButtonClick = stateHolder::onSaveButtonClick,
        )
    }

    SettingsBottomSheet(
        settingsOpened = state.settingsOpened,
        state = state.settingsState,
        strings = state.strings,
        contentPadding = contentPadding,
        onLanguageChange = stateHolder::onLanguageChange,
        onSaveButtonClick = stateHolder::onSettingsSaveClick,
        onClose = stateHolder::onSettingsCloseClick
    )

    AppearanceSettingsBottomSheet(
        opened = state.appearanceSettingsOpened,
        state = state.appearanceState,
        strings = state.strings,
        contentPadding = contentPadding,
        onStateChange = stateHolder::onAppearanceChange,
        onSaveButtonClick = stateHolder::onAppearanceSettingsSaveClick,
        onClose = stateHolder::onAppearanceSettingsCloseClick
    )

    DonateScreen(
        isOpen = state.donateIsOpen,
        state = state.donateState,
        strings = state.strings.donateStrings,
        contentPadding = contentPadding,
        onClose = stateHolder::onDonateCloseClick,
        onPixCodeCopy = stateHolder::onPixCodeCopyClick,
        onPixKeyCopy = stateHolder::onPixKeyCopyClick,
        onBuyMeCoffeeClick = stateHolder::onBuyMeCoffeeClick,
    )
}
