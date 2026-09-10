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
    val noInternetConnection: String
    val tryAgain: String
    val search: String
}

internal data class MonsterCompendiumEnStrings(
    override val noInternetConnection: String = "No internet connection",
    override val tryAgain: String = "Try again",
    override val search: String = "Search",
) : MonsterCompendiumStrings

internal data class MonsterCompendiumPtrStrings(
    override val noInternetConnection: String = "Sem conexão com a internet",
    override val tryAgain: String = "Tentar novamente",
    override val search: String = "Buscar",
) : MonsterCompendiumStrings

fun MonsterCompendiumStrings(): MonsterCompendiumStrings = MonsterCompendiumEnStrings()

internal data class MonsterCompendiumEsStrings(
    override val noInternetConnection: String = "Sin conexión a internet",
    override val tryAgain: String = "Intentar de nuevo",
    override val search: String = "Buscar",
) : MonsterCompendiumStrings

internal fun AppLocalization.getStrings(): MonsterCompendiumStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> MonsterCompendiumEnStrings()
        Language.PORTUGUESE -> MonsterCompendiumPtrStrings()
        Language.SPANISH -> MonsterCompendiumEsStrings()
    }
}
