package br.alexandregpereira.hunter.settings

internal interface SettingsViewIntent {

    fun onImageBaseUrlChange(value: String)

    fun onAlternativeSourceBaseUrlChange(value: String)

    fun onSaveButtonClick()

    fun onManageMonsterContentClick()

    fun onAdvancedSettingsClick()
    fun onAdvancedSettingsCloseClick()

    fun onSettingsClick()

    fun onSettingsCloseClick()

    fun onSettingsSaveClick()

    fun onLanguageChange(language: SettingsLanguageState)

    fun onAppearanceSettingsClick()

    fun onAppearanceSettingsCloseClick()

    fun onAppearanceSettingsSaveClick()

    fun onAppearanceChange(appearance: AppearanceSettingsState)
}
