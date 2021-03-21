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
    val armorClass: Int,
    val hitPoints: Int,
    val hitDice: String,
    val speed: Speed,
    val abilityScores: List<AbilityScore>,
    val savingThrows: List<SavingThrow>,
    val skills: List<Skill>,
    val damageVulnerabilities: List<Damage>,
    val damageResistances: List<Damage>,
    val damageImmunities: List<Damage>
)

data class MonsterImageData(
    val url: String,
    val backgroundColor: String
)
