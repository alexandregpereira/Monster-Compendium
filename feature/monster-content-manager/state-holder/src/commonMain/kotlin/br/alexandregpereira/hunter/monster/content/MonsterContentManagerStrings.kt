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
}

internal data class MonsterContentManagerEnStrings(
    override val title: String = "Manage Content",
    override val add: String = "Add",
    override val remove: String = "Remove",
    override val totalMonsters: String = "{0} monsters",
    override val preview: String = "Preview",
) : MonsterContentManagerStrings

internal data class MonsterContentManagerPtStrings(
    override val title: String = "Gerenciar Conteúdo",
    override val add: String = "Adicionar",
    override val remove: String = "Remover",
    override val totalMonsters: String = "{0} monstros",
    override val preview: String = "Prévia",
) : MonsterContentManagerStrings

data class MonsterContentManagerEmptyStrings(
    override val title: String = "",
    override val add: String = "",
    override val remove: String = "",
    override val totalMonsters: String = "",
    override val preview: String = "",
) : MonsterContentManagerStrings

internal fun AppLocalization.getStrings(): MonsterContentManagerStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> MonsterContentManagerEnStrings()
        Language.PORTUGUESE -> MonsterContentManagerPtStrings()
    }
}
