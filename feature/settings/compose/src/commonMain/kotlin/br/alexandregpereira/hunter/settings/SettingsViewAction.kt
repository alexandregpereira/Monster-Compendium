package br.alexandregpereira.hunter.settings

internal sealed class SettingsViewAction {
    data class GoToExternalUrl(val url: String) : SettingsViewAction()
}
