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
