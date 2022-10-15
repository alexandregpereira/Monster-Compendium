package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.settings.SettingsViewState
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun SettingsScreen(
    state: SettingsViewState,
    contentPadding: PaddingValues,
    onImageBaseUrlChange: (String) -> Unit = {},
    onAlternativeSourceBaseUrlChange: (String) -> Unit = {},
    onSaveButtonClick: () -> Unit = {}
) = Window(modifier = Modifier.fillMaxSize()) {
    Settings(
        imageBaseUrl = state.imageBaseUrl,
        alternativeSourceBaseUrl = state.alternativeSourceBaseUrl,
        saveButtonEnabled = state.saveButtonEnabled,
        onImageBaseUrlChange = onImageBaseUrlChange,
        onAlternativeSourceBaseUrlChange = onAlternativeSourceBaseUrlChange,
        onSaveButtonClick = onSaveButtonClick,
        modifier = Modifier.padding(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding()
        )
    )
}
