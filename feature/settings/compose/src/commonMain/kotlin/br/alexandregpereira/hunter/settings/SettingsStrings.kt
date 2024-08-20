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
    val openGitHubProject: String
    val donateStrings: DonateStrings
}

interface DonateStrings {
    val buyMeACoffee: String
    val donateDescription: String
    val fromBrazil: String
    val pixCopyAndPaste: String
    val copyPixCode: String
    val fromOtherCountries: String
    val fromOtherCountriesDescription: String
}

internal data class SettingsEnStrings(
    override val additionalContent: String = "Additional Content",
    override val monsterImagesJson: String = "Monster Images JSON URL",
    override val alternativeSourcesJson: String = "Alternative Sources JSON URL",
    override val manageMonsterContent: String = "Manage Monsters Content",
    override val sync: String = "Sync",
    override val manageAdvancedSettings: String = "Advanced Settings",
    override val settingsTitle: String = "Language",
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
    override val openGitHubProject: String = "Open GitHub Project",
    override val donateStrings: DonateStrings = object : DonateStrings {
        override val buyMeACoffee: String = "Buy me a Coffee"
        override val donateDescription: String = "If you are enjoying this app and want to help me maintain it, please consider buying me a coffee."
        override val fromBrazil: String = "From Brazil"
        override val pixCopyAndPaste: String = "Pix Copia e Cola"
        override val copyPixCode: String = "Copy Pix Code"
        override val fromOtherCountries: String = "From Other Countries"
        override val fromOtherCountriesDescription: String = "Soon"
    }
) : SettingsStrings

internal data class SettingsPtStrings(
    override val additionalContent: String = "Conteúdo Adicional",
    override val monsterImagesJson: String = "URL do JSON de Imagens de Monstros",
    override val alternativeSourcesJson: String = "URL do JSON de Fontes Alternativas",
    override val manageMonsterContent: String = "Gerenciar Conteúdo de Monstros",
    override val sync: String = "Sincronizar",
    override val manageAdvancedSettings: String = "Configurações Avançadas",
    override val settingsTitle: String = "Idioma",
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
    override val openGitHubProject: String = "Abrir Projeto no GitHub",
    override val donateStrings: DonateStrings = object : DonateStrings {
        override val buyMeACoffee: String = "Compre-me um Café"
        override val donateDescription: String = "Se você está gostando deste aplicativo e quer me ajudar a mantê-lo, considere me comprar um café."
        override val fromBrazil: String = "Do Brasil"
        override val pixCopyAndPaste: String = "Pix Copia e Cola"
        override val copyPixCode: String = "Copiar código Pix"
        override val fromOtherCountries: String = "De Outros Países"
        override val fromOtherCountriesDescription: String = "Em breve"
    }
) : SettingsStrings

internal fun getSettingsStrings(lang: Language): SettingsStrings {
    return when (lang) {
        Language.ENGLISH -> SettingsEnStrings()
        Language.PORTUGUESE -> SettingsPtStrings()
    }
}
