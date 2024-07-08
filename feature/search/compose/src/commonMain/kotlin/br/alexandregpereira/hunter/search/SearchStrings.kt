package br.alexandregpereira.hunter.search

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

internal interface SearchStrings {
    val search: String
    val searchResults: (Int) -> String
}

internal data class SearchEnStrings(
    override val search: String = "Search",
    override val searchResults: (Int) -> String = { results ->
        if (results == 1) {
            "1 result"
        } else {
            "$results results"
        }
    }
) : SearchStrings

internal data class SearchPtStrings(
    override val search: String = "Buscar",
    override val searchResults: (Int) -> String = { results ->
        if (results == 1) {
            "1 resultado"
        } else {
            "$results resultados"
        }
    }
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
