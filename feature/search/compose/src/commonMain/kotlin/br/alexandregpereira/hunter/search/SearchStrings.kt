package br.alexandregpereira.hunter.search

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

internal interface SearchStrings {
    val search: String
    val searchResultsPlural: String
    val searchResultsSingular: String
}

internal data class SearchEnStrings(
    override val search: String = "Search",
    override val searchResultsPlural: String = "{0} results",
    override val searchResultsSingular: String = "{0} result"
) : SearchStrings

internal data class SearchPtStrings(
    override val search: String = "Buscar",
    override val searchResultsPlural: String = "{0} resultados",
    override val searchResultsSingular: String = "{0} resultado"
) : SearchStrings

internal fun AppLocalization.getStrings(): SearchStrings {
    return getLanguage().getStrings()
}

internal fun Language.getStrings(): SearchStrings {
    return when (this) {
        Language.ENGLISH -> SearchEnStrings()
        Language.PORTUGUESE -> SearchPtStrings()
    }
}
