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
    val appearanceSettingsTitle: String
    val forceLightImageBackground: String
    val defaultLightBackground: String
    val defaultDarkBackground: String
    val importContent: String
    val monsterImageContentScale: String
    val imageContentScaleFit: String
    val imageContentScaleCrop: String
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
    override val appearanceSettingsTitle: String = "Appearance",
    override val forceLightImageBackground: String = "Use Light Background Color in Images",
    override val defaultLightBackground: String = "Default Image Light Background Color",
    override val defaultDarkBackground: String = "Default Image Dark Background Color",
    override val importContent: String = "Import Shared Content",
    override val monsterImageContentScale: String = "Monster Image Content Scale",
    override val imageContentScaleFit: String = "Show entire image",
    override val imageContentScaleCrop: String = "Expand the image",
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
    override val appearanceSettingsTitle: String = "Aparência",
    override val forceLightImageBackground: String = "Usar Cor de Fundo Claro nas Imagens",
    override val defaultLightBackground: String = "Cor Padrão de Fundo das Imagens Light",
    override val defaultDarkBackground: String = "Cor Padrão de Fundo das Imagens Dark",
    override val importContent: String = "Importar Conteúdo Compartilhado",
    override val monsterImageContentScale: String = "Escala de Conteúdo de Imagem de Monstro",
    override val imageContentScaleFit: String = "Mostrar imagem inteira",
    override val imageContentScaleCrop: String = "Expandir a imagem",
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
    override val appearanceSettingsTitle: String = "",
    override val forceLightImageBackground: String = "",
    override val defaultLightBackground: String = "",
    override val defaultDarkBackground: String = "",
    override val importContent: String = "",
    override val monsterImageContentScale: String = "",
    override val imageContentScaleFit: String = "",
    override val imageContentScaleCrop: String = "",
) : SettingsStrings

internal fun getSettingsStrings(lang: Language): SettingsStrings {
    return when (lang) {
        Language.ENGLISH -> SettingsEnStrings()
        Language.PORTUGUESE -> SettingsPtStrings()
    }
}
