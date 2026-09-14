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

package br.alexandregpereira.hunter.home

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

internal interface HomeStrings {
    val title: String
    val menu: String
    val searchPlaceholder: String
    val creatures: String
    val spells: String
    val conditions: String
    val total: (Int) -> String
    val recentlyViewed: String
    val refresh: String
    val folders: String
    val seeAllFolders: String
    val create: String
    val monster: String
    val spell: String
    val extraContent: String
    val extraContentProgress: (added: Int, total: Int) -> String
    val manageExtraContent: String
}

internal data class HomeEnStrings(
    override val title: String = "Compendium",
    override val menu: String = "Menu",
    override val searchPlaceholder: String = "Search creatures",
    override val creatures: String = "Creatures",
    override val spells: String = "Spells",
    override val conditions: String = "Conditions",
    override val total: (Int) -> String = { "$it total" },
    override val recentlyViewed: String = "Recently viewed",
    override val refresh: String = "Refresh",
    override val folders: String = "Folders",
    override val seeAllFolders: String = "See all folders",
    override val create: String = "Create",
    override val monster: String = "Monster",
    override val spell: String = "Spell",
    override val extraContent: String = "Extra content",
    override val extraContentProgress: (Int, Int) -> String = { added, total ->
        "$added of $total extra contents added"
    },
    override val manageExtraContent: String = "Manage extra content",
) : HomeStrings

internal data class HomePtStrings(
    override val title: String = "Compêndio",
    override val menu: String = "Menu",
    override val searchPlaceholder: String = "Buscar criaturas",
    override val creatures: String = "Criaturas",
    override val spells: String = "Magias",
    override val conditions: String = "Condições",
    override val total: (Int) -> String = { "$it no total" },
    override val recentlyViewed: String = "Vistos recentemente",
    override val refresh: String = "Atualizar",
    override val folders: String = "Pastas",
    override val seeAllFolders: String = "Ver todas as pastas",
    override val create: String = "Criar",
    override val monster: String = "Monstro",
    override val spell: String = "Magia",
    override val extraContent: String = "Conteúdo extra",
    override val extraContentProgress: (Int, Int) -> String = { added, total ->
        "$added de $total conteúdos extras adicionados"
    },
    override val manageExtraContent: String = "Gerenciar conteúdo extra",
) : HomeStrings

internal data class HomeEsStrings(
    override val title: String = "Compendio",
    override val menu: String = "Menú",
    override val searchPlaceholder: String = "Buscar criaturas",
    override val creatures: String = "Criaturas",
    override val spells: String = "Hechizos",
    override val conditions: String = "Condiciones",
    override val total: (Int) -> String = { "$it en total" },
    override val recentlyViewed: String = "Vistos recientemente",
    override val refresh: String = "Actualizar",
    override val folders: String = "Carpetas",
    override val seeAllFolders: String = "Ver todas las carpetas",
    override val create: String = "Crear",
    override val monster: String = "Monstruo",
    override val spell: String = "Hechizo",
    override val extraContent: String = "Contenido extra",
    override val extraContentProgress: (Int, Int) -> String = { added, total ->
        "$added de $total contenidos extra añadidos"
    },
    override val manageExtraContent: String = "Gestionar contenido extra",
) : HomeStrings

internal fun HomeStrings(): HomeStrings = HomeEnStrings()

internal fun AppLocalization.getHomeStrings(): HomeStrings = getLanguage().getHomeStrings()

internal fun Language.getHomeStrings(): HomeStrings {
    return when (this) {
        Language.ENGLISH -> HomeEnStrings()
        Language.PORTUGUESE -> HomePtStrings()
        Language.SPANISH -> HomeEsStrings()
    }
}
