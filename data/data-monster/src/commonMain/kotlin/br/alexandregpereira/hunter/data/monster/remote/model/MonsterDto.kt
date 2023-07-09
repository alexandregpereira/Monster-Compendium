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

package br.alexandregpereira.hunter.data.monster.remote.model

import br.alexandregpereira.hunter.data.monster.spell.remote.model.SpellcastingDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonsterDto(
    @SerialName("index")
    val index: String,
    @SerialName("type")
    val type: MonsterTypeDto,
    @SerialName("subtype")
    val subtype: String? = null,
    @SerialName("group")
    val group: String? = null,
    @SerialName("challenge_rating")
    val challengeRating: Float,
    @SerialName("name")
    val name: String,
    @SerialName("subtitle")
    val subtitle: String = "",
    @SerialName("size")
    val size: MonsterSizeDto,
    @SerialName("alignment")
    val alignment: String,
    @SerialName("armor_class")
    val armorClass: Int,
    @SerialName("hit_points")
    val hitPoints: Int,
    @SerialName("hit_dice")
    val hitDice: String,
    @SerialName("speed")
    val speed: SpeedDto,
    @SerialName("ability_scores")
    val abilityScores: List<AbilityScoreDto>,
    @SerialName("saving_throws")
    val savingThrows: List<SavingThrowDto> = emptyList(),
    @SerialName("skills")
    val skills: List<SkillDto> = emptyList(),
    @SerialName("damage_vulnerabilities")
    val damageVulnerabilities: List<DamageDto> = emptyList(),
    @SerialName("damage_resistances")
    val damageResistances: List<DamageDto> = emptyList(),
    @SerialName("damage_immunities")
    val damageImmunities: List<DamageDto> = emptyList(),
    @SerialName("condition_immunities")
    val conditionImmunities: List<ConditionDto> = emptyList(),
    @SerialName("senses")
    val senses: List<String> = emptyList(),
    @SerialName("languages")
    val languages: String,
    @SerialName("special_abilities")
    val specialAbilities: List<SpecialAbilityDto> = emptyList(),
    @SerialName("actions")
    val actions: List<ActionDto> = emptyList(),
    @SerialName("legendary_actions")
    val legendaryActions: List<ActionDto> = emptyList(),
    @SerialName("reactions")
    val reactions: List<SpecialAbilityDto> = emptyList(),
    @SerialName("spellcasting")
    val spellcastings: List<SpellcastingDto> = emptyList(),
    @SerialName("source")
    val source: SourceDto
)
