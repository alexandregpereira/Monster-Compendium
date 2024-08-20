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
