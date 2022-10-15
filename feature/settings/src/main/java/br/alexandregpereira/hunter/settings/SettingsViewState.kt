package br.alexandregpereira.hunter.settings

internal data class SettingsViewState(
    val imageBaseUrl: String = "",
    val alternativeSourceBaseUrl: String = "",
    val saveButtonEnabled: Boolean = true,
)
