/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.domain.model

import br.alexandregpereira.hunter.domain.locale.formatToNumber
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting

data class Monster(
    val index: String,
    val name: String = "",
    val type: MonsterType = MonsterType.ABERRATION,
    val challengeRating: Float = 0f,
    val imageData: MonsterImageData = MonsterImageData(),
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
    val savingThrows: List<SavingThrow> = emptyList(),
    val skills: List<Skill> = emptyList(),
    val damageVulnerabilities: List<Damage> = emptyList(),
    val damageResistances: List<Damage> = emptyList(),
    val damageImmunities: List<Damage> = emptyList(),
    val conditionImmunities: List<Condition> = emptyList(),
    val specialAbilities: List<AbilityDescription> = emptyList(),
    val actions: List<Action> = emptyList(),
    val legendaryActions: List<Action> = emptyList(),
    val reactions: List<AbilityDescription> = emptyList(),
    val spellcastings: List<Spellcasting> = emptyList(),
    val lore: String? = null,
    val status: MonsterStatus = MonsterStatus.Original,
) {

    val xp: Int = challengeRatingToXp()

    val challengeRatingFormatted: String = challengeRating.getChallengeRatingFormatted()
}

enum class MonsterStatus {
    Original, Edited, Clone
}

private fun Float.getChallengeRatingFormatted(): String {
    return if (this < 1) {
        val value = 1 / this
        "1/${value.toInt()}"
    } else {
        this.toInt().toString()
    }
}

data class MonsterImageData(
    val url: String = "",
    val backgroundColor: Color = Color(),
    val isHorizontal: Boolean = false
)

data class Color(
    val light: String = "",
    val dark: String = ""
)

fun Monster.isComplete() = abilityScores.isNotEmpty()

private fun Monster.challengeRatingToXp(): Int {
    return when (challengeRating) {
        0.125f -> 25
        0.25f -> 50
        0.5f -> 100
        1f -> 200
        2f -> 450
        3f -> 700
        4f -> 1100
        5f -> 1800
        6f -> 2300
        7f -> 2900
        8f -> 3900
        9f -> 5000
        10f -> 5900
        11f -> 7200
        12f -> 8400
        13f -> 10000
        14f -> 11500
        15f -> 13000
        16f -> 15000
        17f -> 18000
        18f -> 20000
        19f -> 22000
        20f -> 25000
        21f -> 33000
        22f -> 41000
        23f -> 50000
        24f -> 62000
        25f -> 75000
        26f -> 90000
        27f -> 105000
        28f -> 120000
        29f -> 135000
        30f -> 155000
        else -> 10
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
