package br.alexandregpereira.hunter.settings

import br.alexandregpereira.hunter.localization.Language

interface SettingsStrings {
    val additionalContent: String
    val monsterImagesJson: String
    val alternativeSourcesJson: String
    val manageMonsterContent: String
    val sync: String
    val manageAdvancedSettings: String
    val settingsTitle: String
    val languageLabel: String
    val save: String
}


internal data class SettingsEnStrings(
    override val additionalContent: String = "Additional Content",
    override val monsterImagesJson: String = "Monster Images JSON URL",
    override val alternativeSourcesJson: String = "Alternative Sources JSON URL",
    override val manageMonsterContent: String = "Manage Monsters Content",
    override val sync: String = "Sync",
    override val manageAdvancedSettings: String = "Advanced Settings",
    override val settingsTitle: String = "Settings",
    override val languageLabel: String = "Language",
    override val save: String = "Save",
) : SettingsStrings

internal data class SettingsPtStrings(
    override val additionalContent: String = "Conteúdo Adicional",
    override val monsterImagesJson: String = "URL do JSON de Imagens de Monstros",
    override val alternativeSourcesJson: String = "URL do JSON de Fontes Alternativas",
    override val manageMonsterContent: String = "Gerenciar Conteúdo de Monstros",
    override val sync: String = "Sincronizar",
    override val manageAdvancedSettings: String = "Configurações Avançadas",
    override val settingsTitle: String = "Configurações",
    override val languageLabel: String = "Idioma",
    override val save: String = "Salvar",
) : SettingsStrings

internal data class SettingsEmptyStrings(
    override val additionalContent: String = "",
    override val monsterImagesJson: String = "",
    override val alternativeSourcesJson: String = "",
    override val manageMonsterContent: String = "",
    override val sync: String = "",
    override val manageAdvancedSettings: String = "",
    override val settingsTitle: String = "",
    override val languageLabel: String = "",
    override val save: String = "",
) : SettingsStrings

internal fun getSettingsStrings(lang: Language): SettingsStrings {
    return when (lang) {
        Language.ENGLISH -> SettingsEnStrings()
        Language.PORTUGUESE -> SettingsPtStrings()
    }
}
