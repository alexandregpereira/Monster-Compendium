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

package br.alexandregpereira.hunter.monster.detail

/**
 * The sections of the monster detail holding content for this monster, in the order they are laid
 * out. The ids are the item keys of the monster detail list, so the sections on screen can be
 * resolved from the keys the list reports as visible.
 *
 * The monster detail lays out an item for every section, whether the monster has that content or
 * not, and an empty one takes no height. Without this, every empty section of a monster would be
 * reported as viewed as soon as the list reached the end of its content.
 *
 * Mirrors the emptiness rules of the blocks in MonsterInfo.kt.
 */
internal fun MonsterState.contentSections(): List<String> = buildList {
    if (lore.isNotBlank()) add("lore")
    add("stats")
    if (speed.values.isNotEmpty()) add("speed")
    if (abilityScores.isNotEmpty()) add("abilityScores")
    if (savingThrows.isNotEmpty()) add("savingThrows")
    if (skills.isNotEmpty()) add("skills")
    if (damageVulnerabilities.isNotEmpty()) add("damageVulnerabilities")
    if (damageResistances.isNotEmpty()) add("damageResistances")
    if (damageImmunities.isNotEmpty()) add("damageImmunities")
    if (conditionImmunities.isNotEmpty()) add("conditionImmunities")
    if (senses.isNotEmpty()) add("senses")
    if (languages.isNotBlank()) add("languages")
    if (specialAbilities.isNotEmpty()) add("specialAbilities")
    if (actions.isNotEmpty()) add("actions")
    if (bonusActions.isNotEmpty()) add("bonusActions")
    if (reactions.isNotEmpty()) add("reactions")
    if (legendaryActions.isNotEmpty()) add("legendaryActions")
    if (spellcastings.isNotEmpty()) add(SPELLCASTINGS_SECTION)
    if (sourceName.isNotBlank()) add("source")
}

/**
 * A monster has an item per spellcasting and per spell group inside it, which says more about the
 * monster than about what the user read, so all of them count as the single spellcastings section.
 */
internal fun String.toSectionId(): String = when {
    startsWith(SPELLCASTING_ITEM_KEY_PREFIX) -> SPELLCASTINGS_SECTION
    else -> this
}

private const val SPELLCASTINGS_SECTION = "spellcastings"
private const val SPELLCASTING_ITEM_KEY_PREFIX = "spellcasting"
