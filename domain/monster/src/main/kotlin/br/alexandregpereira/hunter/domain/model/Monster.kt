/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.domain.model

import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting

data class Monster(
    val preview: MonsterPreview,
    val subtype: String? = null,
    val group: String? = null,
    val subtitle: String = "",
    val size: String = "",
    val alignment: String = "",
    val stats: Stats = Stats(),
    val senses: List<String> = emptyList(),
    val languages: String = "",
    val sourceName: String = "",
    val speed: Speed = Speed(hover = false, values = emptyList()),
    val abilityScores: List<AbilityScore> = emptyList(),
    val savingThrows: List<Proficiency> = emptyList(),
    val skills: List<Proficiency> = emptyList(),
    val damageVulnerabilities: List<Damage> = emptyList(),
    val damageResistances: List<Damage> = emptyList(),
    val damageImmunities: List<Damage> = emptyList(),
    val conditionImmunities: List<Condition> = emptyList(),
    val specialAbilities: List<AbilityDescription> = emptyList(),
    val actions: List<Action> = emptyList(),
    val reactions: List<AbilityDescription> = emptyList(),
    val spellcastings: List<Spellcasting> = emptyList(),
) {
    val index: String
        get() = preview.index
    val name: String
        get() = preview.name
    val type: MonsterType
        get() = preview.type
    val challengeRating: Float
        get() = preview.challengeRating
    val imageData: MonsterImageData
        get() = preview.imageData
}

data class MonsterPreview(
    val index: String,
    val name: String = "",
    val type: MonsterType = MonsterType.ABERRATION,
    val challengeRating: Float = 0f,
    val imageData: MonsterImageData = MonsterImageData()
)

data class MonsterImageData(
    val url: String = "",
    val backgroundColor: Color = Color(),
    val isHorizontal: Boolean = false
)

data class Color(
    val light: String = "",
    val dark: String = ""
)
