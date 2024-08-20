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
