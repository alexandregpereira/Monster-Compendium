package br.alexandregpereira.hunter.app

import br.alexandregpereira.hunter.localization.Language

internal interface MainStrings {
    val compendium: String
    val search: String
    val folders: String
    val menu: String
}

internal data class MainEnStrings(
    override val compendium: String = "Compendium",
    override val search: String = "Search",
    override val folders: String = "Folders",
    override val menu: String = "Menu",
) : MainStrings

internal data class MainPtStrings(
    override val compendium: String = "Compêndio",
    override val search: String = "Buscar",
    override val folders: String = "Pastas",
    override val menu: String = "Menu",
) : MainStrings

internal data class MainEmptyStrings(
    override val compendium: String = "",
    override val search: String = "",
    override val folders: String = "",
    override val menu: String = "",
) : MainStrings

internal fun Language.getStrings(): MainStrings {
    return when (this) {
        Language.ENGLISH -> MainEnStrings()
        Language.PORTUGUESE -> MainPtStrings()
    }
}
