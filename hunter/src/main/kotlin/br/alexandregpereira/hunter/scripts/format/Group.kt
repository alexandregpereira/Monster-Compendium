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

internal fun isGroupByIndex(index: String): Boolean {
    return groupsByIndex.any { index.endsWith(it) }
}

internal fun getGroupByIndex(index: String): String? {
    return groupsByIndex.find { index.endsWith(it) }
        ?.removeSuffix("-wyrmling")
        ?.capitalize(Locale.ROOT)?.let { it + "s" }
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
)

internal val groupsByIndex = listOf(
    "hag",
    "elemental",
    "giant",
    "golem",
    "mephit",
    "naga",
    "zombie",
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

private val dragons = listOf(
    "ancient-{color}-dragon",
    "adult-{color}-dragon",
    "young-{color}-dragon",
    "{color}-dragon-wyrmling",
)

private val genies = listOf(
    "djinni",
    "efreeti",
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

private val groups = hashMapOf(
    "Angels" to angels,
    "Animated Objects" to animatedObjects,
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
    "Genies" to genies,
    "Lycanthropes" to lycanthropes,
    "Oozes" to oozes,
    "Sphinxes" to sphinxes,
    "Vampires" to vampires,
)
