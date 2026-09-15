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

package br.alexandregpereira.hunter.monster.compendium.state

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

interface MonsterCompendiumStrings {
    val creatures: String
    val noInternetConnection: String
    val tryAgain: String
    val search: String
    val sortAlphabetical: String
    val sortBy: String
    val sortChallengeRatingAsc: String
    val sortChallengeRatingDesc: String
    val selectCreatures: String
    val createFolder: String
    val confirm: String
    val back: String
}

internal data class MonsterCompendiumEnStrings(
    override val creatures: String = "Creatures",
    override val noInternetConnection: String = "No internet connection",
    override val tryAgain: String = "Try again",
    override val search: String = "Search",
    override val sortAlphabetical: String = "Alphabetical",
    override val sortBy: String = "Sort by",
    override val sortChallengeRatingAsc: String = "Challenge rating (asc)",
    override val sortChallengeRatingDesc: String = "Challenge rating (desc)",
    override val selectCreatures: String = "Select the creatures",
    override val createFolder: String = "Create folder",
    override val confirm: String = "Confirm",
    override val back: String = "Back",
) : MonsterCompendiumStrings

internal data class MonsterCompendiumPtrStrings(
    override val creatures: String = "Criaturas",
    override val noInternetConnection: String = "Sem conexão com a internet",
    override val tryAgain: String = "Tentar novamente",
    override val search: String = "Buscar",
    override val sortAlphabetical: String = "Alfabética",
    override val sortBy: String = "Ordenar por",
    override val sortChallengeRatingAsc: String = "Nível de desafio (cresc.)",
    override val sortChallengeRatingDesc: String = "Nível de desafio (decresc.)",
    override val selectCreatures: String = "Selecione as criaturas",
    override val createFolder: String = "Criar pasta",
    override val confirm: String = "Confirmar",
    override val back: String = "Voltar",
) : MonsterCompendiumStrings

fun MonsterCompendiumStrings(): MonsterCompendiumStrings = MonsterCompendiumEnStrings()

internal data class MonsterCompendiumEsStrings(
    override val creatures: String = "Criaturas",
    override val noInternetConnection: String = "Sin conexión a internet",
    override val tryAgain: String = "Intentar de nuevo",
    override val search: String = "Buscar",
    override val sortAlphabetical: String = "Alfabético",
    override val sortBy: String = "Ordenar por",
    override val sortChallengeRatingAsc: String = "Valor de desafío (asc.)",
    override val sortChallengeRatingDesc: String = "Valor de desafío (desc.)",
    override val selectCreatures: String = "Selecciona las criaturas",
    override val createFolder: String = "Crear carpeta",
    override val confirm: String = "Confirmar",
    override val back: String = "Volver",
) : MonsterCompendiumStrings

internal fun AppLocalization.getStrings(): MonsterCompendiumStrings {
    return getLanguage().getStrings()
}

internal fun Language.getStrings(): MonsterCompendiumStrings {
    return when (this) {
        Language.ENGLISH -> MonsterCompendiumEnStrings()
        Language.PORTUGUESE -> MonsterCompendiumPtrStrings()
        Language.SPANISH -> MonsterCompendiumEsStrings()
    }
}
