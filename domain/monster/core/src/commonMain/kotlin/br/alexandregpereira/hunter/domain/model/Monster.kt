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

package br.alexandregpereira.hunter.domain.model

import br.alexandregpereira.hunter.domain.locale.formatToNumber
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting

data class Monster(
    val index: String,
    val name: String,
    val type: MonsterType,
    val challengeRatingData: ChallengeRating,
    val imageData: MonsterImageData,
    val originalImageData: MonsterImageData,
    val customMonsterImage: MonsterImage?,
    val subtype: String?,
    val group: String?,
    val subtitle: String,
    val size: String,
    val alignment: String,
    val stats: Stats,
    val senses: List<String>,
    val languages: String,
    val sourceName: String,
    val speed: Speed,
    val abilityScores: List<AbilityScore>,
    val savingThrows: List<SavingThrow>,
    val skills: List<Skill>,
    val damageVulnerabilities: List<Damage>,
    val damageResistances: List<Damage>,
    val damageImmunities: List<Damage>,
    val conditionImmunities: List<Condition>,
    val specialAbilities: List<AbilityDescription>,
    val actions: List<Action>,
    val legendaryActions: List<Action>,
    val reactions: List<AbilityDescription>,
    val spellcastings: List<Spellcasting>,
    val lore: String?,
    val status: MonsterStatus,
) {

    val xp: Int
        get() = challengeRatingToXp()

    val challengeRating: Float
        get() = challengeRatingData.value

    val challengeRatingFormatted: String
        get() = challengeRatingData.value.getChallengeRatingFormatted()
}

enum class MonsterStatus {
    Original, Edited, Clone, Imported
}

data class ChallengeRating(
    val value: Float,
    val valueInString: String,
    val formatted: String,
) {
    companion object {
        fun create(value: Float) = ChallengeRating(
            value = value,
            valueInString = value.toString(),
            formatted = value.getChallengeRatingFormatted(),
        )
    }
}

fun Float.getChallengeRatingFormatted(): String {
    if (this == 0f) return "0"

    return if (this < 1) {
        val value = 1 / this
        "1/${value.toInt()}"
    } else {
        this.toInt().toString()
    }
}

data class MonsterImageData(
    val url: String,
    val backgroundColor: Color,
    val isHorizontal: Boolean,
    val contentScale: MonsterImageContentScale?,
    val isImageDataFromCustomDatabase: Boolean,
) {
    fun contentScaleOrDefault(): MonsterImageContentScale {
        return contentScale ?: MonsterImageContentScale.Fit
    }
}

enum class MonsterImageContentScale {
    Fit, Crop
}

data class Color(
    val light: String,
    val dark: String
)

fun Monster.isComplete() = abilityScores.isNotEmpty()

private fun Monster.challengeRatingToXp(): Int {
    return when (challengeRating) {
        in 0.125f..0.24f -> 25
        in 0.25f..0.49f -> 50
        in 0.5f..0.9f -> 100
        else -> when (val intValue = challengeRating.toInt()) {
            0 -> 10
            1 -> 200
            2 -> 450
            3 -> 700
            4 -> 1100
            5 -> 1800
            6 -> 2300
            7 -> 2900
            8 -> 3900
            9 -> 5000
            10 -> 5900
            11 -> 7200
            12 -> 8400
            13 -> 10000
            14 -> 11500
            15 -> 13000
            16 -> 15000
            17 -> 18000
            18 -> 20000
            19 -> 22000
            20 -> 25000
            21 -> 33000
            22 -> 41000
            23 -> 50000
            24 -> 62000
            25 -> 75000
            26 -> 90000
            27 -> 105000
            28 -> 120000
            29 -> 135000
            30 -> 155000
            else -> if (challengeRating > 30) {
                (155000 + (intValue - 30) * 10000).coerceAtMost(300000)
            } else 0
        }
    }
}

fun Monster.xpFormatted(): String {
    val xpString = when {
        xp < 1000 -> xp.toString()
        else -> {
            val xpFormatted = xp.formatToNumber()
                .dropLastWhile { it == '0' }
                .let { if (it.last().isDigit().not()) it.dropLast(1) else it }
            "${xpFormatted}k"
        }
    }

    return "$xpString XP"
}
