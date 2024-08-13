package br.alexandregpereira.hunter.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import br.alexandregpereira.hunter.settings.ui.AdvancedSettings
import br.alexandregpereira.hunter.settings.ui.AppearanceSettingsBottomSheet
import br.alexandregpereira.hunter.settings.ui.SettingsBottomSheet
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import org.koin.compose.koinInject

@Composable
fun SettingsBottomSheets() {
    val stateHolder: SettingsStateHolder = koinInject()
    val state by stateHolder.state.collectAsState()
    BottomSheet(
        opened = state.advancedSettingsOpened,
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
        onLanguageChange = stateHolder::onLanguageChange,
        onSaveButtonClick = stateHolder::onSettingsSaveClick,
        onClose = stateHolder::onSettingsCloseClick
    )

    AppearanceSettingsBottomSheet(
        opened = state.appearanceSettingsOpened,
        state = state.appearanceState,
        strings = state.strings,
        onStateChange = stateHolder::onAppearanceChange,
        onSaveButtonClick = stateHolder::onAppearanceSettingsSaveClick,
        onClose = stateHolder::onAppearanceSettingsCloseClick
    )    
}
