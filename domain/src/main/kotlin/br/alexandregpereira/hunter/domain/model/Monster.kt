/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.domain.model

data class Monster(
    val index: String,
    val type: MonsterType,
    val subtype: String?,
    val group: String?,
    val challengeRating: Float,
    val name: String,
    val subtitle: String,
    val imageData: MonsterImageData,
    val size: String,
    val alignment: String,
    val stats: Stats,
    val speed: Speed,
    val abilityScores: List<AbilityScore> = emptyList(),
    val savingThrows: List<SavingThrow> = emptyList(),
    val skills: List<Skill> = emptyList(),
    val damageVulnerabilities: List<Damage> = emptyList(),
    val damageResistances: List<Damage> = emptyList(),
    val damageImmunities: List<Damage> = emptyList()
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
