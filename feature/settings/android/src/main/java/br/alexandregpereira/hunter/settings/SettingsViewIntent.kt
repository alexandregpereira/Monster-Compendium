package br.alexandregpereira.hunter.settings

internal interface SettingsViewIntent {

    fun onImageBaseUrlChange(value: String)

    fun onAlternativeSourceBaseUrlChange(value: String)

    fun onSaveButtonClick()

    fun onManageMonsterContentClick()

    fun onAdvancedSettingsClick()
    fun onAdvancedSettingsCloseClick()
}

internal class EmptySettingsViewIntent : SettingsViewIntent {

    override fun onImageBaseUrlChange(value: String) {}

    override fun onAlternativeSourceBaseUrlChange(value: String) {}

    override fun onSaveButtonClick() {}

    override fun onManageMonsterContentClick() {}

    override fun onAdvancedSettingsClick() {}

    override fun onAdvancedSettingsCloseClick() {}
}