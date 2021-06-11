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

package br.alexandregpereira.hunter.domain.model

data class Monster(
    val preview: MonsterPreview,
    val subtype: String?,
    val group: String?,
    val subtitle: String,
    val size: String,
    val alignment: String,
    val stats: Stats,
    val speed: Speed,
    val abilityScores: List<AbilityScore> = emptyList(),
    val savingThrows: List<Proficiency> = emptyList(),
    val skills: List<Proficiency> = emptyList(),
    val damageVulnerabilities: List<Damage> = emptyList(),
    val damageResistances: List<Damage> = emptyList(),
    val damageImmunities: List<Damage> = emptyList(),
    val conditionImmunities: List<Condition> = emptyList(),
    val senses: List<String>,
    val languages: String,
    val specialAbilities: List<AbilityDescription>,
    val actions: List<Action>
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
        get() =  preview.imageData
}

data class MonsterPreview(
    val index: String,
    val name: String,
    val type: MonsterType,
    val challengeRating: Float,
    val imageData: MonsterImageData
)

data class MonsterImageData(
    val url: String,
    val backgroundColor: Color,
    val isHorizontal: Boolean = false
)

data class Color(
    val light: String,
    val dark: String
) {

    fun getColor(isDarkTheme: Boolean): String = if (isDarkTheme) dark else light
}
