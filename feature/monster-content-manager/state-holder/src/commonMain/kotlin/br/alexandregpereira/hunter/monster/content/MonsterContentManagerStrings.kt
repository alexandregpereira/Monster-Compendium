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

package br.alexandregpereira.hunter.monster.content

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

interface MonsterContentManagerStrings {
    val title: String
    val add: String
    val remove: String
    val totalMonsters: String
    val preview: String
    val noInternetConnection: String
    val tryAgain: String
}

internal data class MonsterContentManagerEnStrings(
    override val title: String = "Manage Content",
    override val add: String = "Add",
    override val remove: String = "Remove",
    override val totalMonsters: String = "{0} monsters",
    override val preview: String = "Preview",
    override val noInternetConnection: String = "No internet connection",
    override val tryAgain: String = "Try again"
) : MonsterContentManagerStrings

internal data class MonsterContentManagerPtStrings(
    override val title: String = "Gerenciar Conteúdo",
    override val add: String = "Adicionar",
    override val remove: String = "Remover",
    override val totalMonsters: String = "{0} monstros",
    override val preview: String = "Prévia",
    override val noInternetConnection: String = "Sem conexão com a internet",
    override val tryAgain: String = "Tentar novamente",
) : MonsterContentManagerStrings

fun MonsterContentManagerStrings(): MonsterContentManagerStrings {
    return MonsterContentManagerEnStrings()
}

internal data class MonsterContentManagerEsStrings(
    override val title: String = "Gestionar Contenido",
    override val add: String = "Añadir",
    override val remove: String = "Eliminar",
    override val totalMonsters: String = "{0} monstruos",
    override val preview: String = "Vista previa",
    override val noInternetConnection: String = "Sin conexión a internet",
    override val tryAgain: String = "Intentar de nuevo",
) : MonsterContentManagerStrings

internal fun AppLocalization.getStrings(): MonsterContentManagerStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> MonsterContentManagerEnStrings()
        Language.PORTUGUESE -> MonsterContentManagerPtStrings()
        Language.SPANISH -> MonsterContentManagerEsStrings()
    }
}
