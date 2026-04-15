/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
    val subscribePremium: String
    val spells: String
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
    override val subscribePremium: String = "Remove ads with Premium",
    override val spells: String = "Spells",
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
    override val subscribePremium: String = "Remover anúncios com Premium",
    override val spells: String = "Magias",
) : SettingsStrings

internal data class SettingsEsStrings(
    override val additionalContent: String = "Contenido Adicional",
    override val monsterImagesJson: String = "URL JSON de Imágenes de Monstruos",
    override val alternativeSourcesJson: String = "URL JSON de Fuentes Alternativas",
    override val manageMonsterContent: String = "Gestionar Contenido de Monstruos",
    override val sync: String = "Sincronizar",
    override val manageAdvancedSettings: String = "Ajustes Avanzados",
    override val settingsTitle: String = "Idioma",
    override val languageLabel: String = "Idioma",
    override val save: String = "Guardar",
    override val appearanceSettingsTitle: String = "Apariencia",
    override val forceLightImageBackground: String = "Usar Color de Fondo Claro en Imágenes",
    override val defaultLightBackground: String = "Color de Fondo Claro Predeterminado",
    override val defaultDarkBackground: String = "Color de Fondo Oscuro Predeterminado",
    override val importContent: String = "Importar Contenido Compartido",
    override val monsterImageContentScale: String = "Escala de Contenido de Imagen",
    override val imageContentScaleFit: String = "Mostrar imagen completa",
    override val imageContentScaleCrop: String = "Expandir la imagen",
    override val openGitHubProject: String = "Abrir Proyecto en GitHub",
    override val subscribePremium: String = "Eliminar anuncios con Premium",
    override val spells: String = "Magias",
) : SettingsStrings

internal fun getSettingsStrings(lang: Language): SettingsStrings {
    return when (lang) {
        Language.ENGLISH -> SettingsEnStrings()
        Language.PORTUGUESE -> SettingsPtStrings()
        Language.SPANISH -> SettingsEsStrings()
    }
}
