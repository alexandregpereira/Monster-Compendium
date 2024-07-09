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
}

internal class EmptySettingsViewIntent : SettingsViewIntent {

    override fun onImageBaseUrlChange(value: String) {}

    override fun onAlternativeSourceBaseUrlChange(value: String) {}

    override fun onSaveButtonClick() {}

    override fun onManageMonsterContentClick() {}

    override fun onAdvancedSettingsClick() {}

    override fun onAdvancedSettingsCloseClick() {}

    override fun onSettingsClick() {}

    override fun onSettingsCloseClick() {}

    override fun onSettingsSaveClick() {}

    override fun onLanguageChange(language: SettingsLanguageState) {}
}