/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.scripts.format

import java.util.Locale

internal fun getGroup(index: String, subtype: String?): String? {
    return getGroupByGroupMap(index)
        ?: getGroupByIndexSuffix(index)
        ?: getGroupByIndexPrefix(index)
        ?: subtype.takeIf { isSubtypeGroup(it) }?.getSubtypeGroup()
}

private fun isSubtypeGroup(subtype: String?): Boolean {
    return subtype != null && subtypeGroupAllowList.contains(subtype)
}

private fun String.getSubtypeGroup(): String {
    return replaceFirstChar { char -> char.titlecase(Locale.ROOT) }.let { it + "s" }
}

internal fun getGroupByIndexSuffix(index: String): String? {
    return groupsByIndexSuffix.find { index.endsWith(it) }
        ?.removeSuffix("-wyrmling")
        ?.replaceFirstChar { char -> char.titlecase(Locale.ROOT) }?.let { it + "s" }
}

internal fun getGroupByIndexPrefix(index: String): String? {
    return groupsByIndexPrefix.find { index.startsWith(it) }
        ?.replaceFirstChar { char -> char.titlecase(Locale.ROOT) }?.let { it + "s" }
}

internal fun getGroupByGroupMap(index: String): String? {
    return groups.toList().find { it.second.any { i -> index == i } }?.first
}

private fun getDragonsByColor(color: String): List<String>{
    return dragons.map { it.replace("{color}", color) }
}


internal val subtypeGroupAllowList = listOf(
    "devil",
    "demon",
    "yugoloth",
    "kobold",
)

internal val groupsByIndexSuffix = listOf(
    "hag",
    "elemental",
    "giant",
    "golem",
    "mephit",
    "naga",
    "zombie",
    "slaad",
    "blight",
)

internal val groupsByIndexPrefix = listOf(
    "drow",
    "kuo-toa",
    "yuan-ti",
    "duergar",
    "gnoll",
)

internal val angels = listOf(
    "deva",
    "planetar",
    "solar"
)

private val animatedObjects = listOf(
    "animated-armor",
    "flying-sword",
    "rug-of-smothering"
)

private val beholders = listOf(
    "beholder",
    "spectator",
)

private val dragons = listOf(
    "ancient-{color}-dragon",
    "adult-{color}-dragon",
    "young-{color}-dragon",
    "{color}-dragon-wyrmling",
)

private val genies = listOf(
    "dao",
    "djinni",
    "efreeti",
    "marid",
)

private val lycanthropes = listOf(
    "werebear",
    "wereboar",
    "wererat",
    "weretiger",
    "werewolf",
)

private val oozes = listOf(
    "black-pudding",
    "gelatinous-cube",
    "ochre-jelly",
)

private val sphinxes = listOf(
    "androsphinx",
    "gynosphinx",
)

private val vampires = listOf(
    "vampire",
    "vampire-spawn",
)

private val demonLords = listOf(
    "baphomet",
    "demogorgon",
    "juiblex",
    "orcus",
)

private val guildMasters = listOf(
    "rakdos",
    "obzedat-ghost",
    "niv-mizzet",
    "borborygmos",
    "isperia",
    "aurelia",
)

private val groups = hashMapOf(
    "Angels" to angels,
    "Animated Objects" to animatedObjects,
    "Beholders" to beholders,
    "Demon Lords" to demonLords,
    "Dragons, Black" to getDragonsByColor("black"),
    "Dragons, Blue" to getDragonsByColor("blue"),
    "Dragons, Green" to getDragonsByColor("green"),
    "Dragons, Red" to getDragonsByColor("red"),
    "Dragons, White" to getDragonsByColor("white"),
    "Dragons, Brass" to getDragonsByColor("brass"),
    "Dragons, Bronze" to getDragonsByColor("bronze"),
    "Dragons, Cooper" to getDragonsByColor("copper"),
    "Dragons, Gold" to getDragonsByColor("gold"),
    "Dragons, Silver" to getDragonsByColor("silver"),
    "Dragons, Shadow" to getDragonsByColor("red-shadow"),
    "Genies" to genies,
    "Lycanthropes" to lycanthropes,
    "Oozes" to oozes,
    "Ravnica Guild Masters" to guildMasters,
    "Sphinxes" to sphinxes,
    "Vampires" to vampires,
)
