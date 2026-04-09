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
    val searchTipsTitle: String
    val searchTips: List<SearchTip>
    val searchNoResultsTitle: String
    val searchNoResultsDescription: String
}

internal data class SearchEnStrings(
    override val search: String = "Search",
    override val searchResultsPlural: String = "{0} results",
    override val searchResultsSingular: String = "{0} result",
    override val searchTipsTitle: String = "Search Examples",
    override val searchNoResultsTitle: String = "No creature found",
    override val searchNoResultsDescription: String = "Try a different search term",
    override val searchTips: List<SearchTip> = listOf(
        "-challenge>5" tip "Search creatures with a challenge rating greater than 5",
        "-challenge<2" tip "Search creatures with a challenge rating less than 2",
        "-challenge=1" tip "Search creatures with a challenge rating equal to 1",
        "-challenge=0.5" tip "Search creatures with a challenge rating equal to 1/2",
        "-type=humanoid" tip "Search humanoid type creatures",
        "-type=undead" tip "Search undead type creatures",
        "-spellName=fireball" tip "Search creatures that contain the spell name 'fireball'",
        "-legendaryMonster" tip "Search legendary creatures",
        "-type=dragon & -challenge>8" tip "Search dragon type creatures with a challenge rating greater than 8",
        "-lore=tiamat & -challenge>5 & -challenge<10" tip "Search creatures with a lore that mentions tiamat, with a challenge rating greater than 5 and less than 10",
    ),
) : SearchStrings

internal data class SearchPtStrings(
    override val search: String = "Buscar",
    override val searchResultsPlural: String = "{0} resultados",
    override val searchResultsSingular: String = "{0} resultado",
    override val searchTipsTitle: String = "Exemplos de Busca",
    override val searchNoResultsTitle: String = "Nenhuma criatura encontrada",
    override val searchNoResultsDescription: String = "Tente um termo de busca diferente",
    override val searchTips: List<SearchTip> = listOf(
        "-challenge>5" tip "Busca criaturas com nível de desafio maior que 5",
        "-challenge<2" tip "Busca criaturas com nível de desafio menor que 2",
        "-challenge=1" tip "Busca criaturas com nível de desafio igual a 1",
        "-challenge=0.5" tip "Busca criaturas com nível de desafio igual a 1/2",
        "-type=humanoid" tip "Busca criaturas do tipo humanoide*",
        "-type=undead" tip "Busca criaturas do tipo morto vivo*",
        "-spellName=bola de fogo" tip "Busca criaturas que contêm o nome da magia 'bola de fogo'",
        "-legendaryMonster" tip "Busca criaturas lendárias",
        "-type=dragon & -challenge>8" tip "Busca criaturas do tipo dragão e com nível de desafio maior que 8",
        "-lore=tiamat & -challenge>5 & -challenge<10" tip "Busca criaturas com mitologia que menciona tiamat, com nível de desafio maior que 5 e menor que 10",
        null tip "* A busca pelo tipo da criatura (-type=), alinhamneto (-alignment=) e tamanho (-size=) só aceitam valores em inglês por enquanto.",
    ),
) : SearchStrings

internal infix fun String?.tip(that: String): SearchTip = SearchTip(
    searchQuery = this,
    searchQueryExplanation = that
)

internal data class SearchTip(
    val searchQuery: String?,
    val searchQueryExplanation: String,
)

internal fun AppLocalization.getStrings(): SearchStrings {
    return getLanguage().getStrings()
}

internal fun Language.getStrings(): SearchStrings {
    return when (this) {
        Language.ENGLISH -> SearchEnStrings()
        Language.PORTUGUESE -> SearchPtStrings()
    }
}
