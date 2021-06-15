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

package br.alexandregpereira.hunter.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonsterDto(
    @SerialName("index")
    val index: String,
    @SerialName("type")
    val type: MonsterTypeDto,
    @SerialName("subtype")
    val subtype: String?,
    @SerialName("group")
    val group: String?,
    @SerialName("challenge_rating")
    val challengeRating: Float,
    @SerialName("name")
    val name: String,
    @SerialName("subtitle")
    val subtitle: String = "",
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("background_color")
    val backgroundColor: ColorDto = ColorDto(),
    @SerialName("is_horizontal_image")
    val isHorizontalImage: Boolean,
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
    val savingThrows: List<SavingThrowDto>,
    @SerialName("skills")
    val skills: List<SkillDto>,
    @SerialName("damage_vulnerabilities")
    val damageVulnerabilities: List<DamageDto>,
    @SerialName("damage_resistances")
    val damageResistances: List<DamageDto>,
    @SerialName("damage_immunities")
    val damageImmunities: List<DamageDto>,
    @SerialName("condition_immunities")
    val conditionImmunities: List<ConditionDto>,
    @SerialName("senses")
    val senses: List<String>,
    @SerialName("languages")
    val languages: String,
    @SerialName("special_abilities")
    val specialAbilities: List<SpecialAbilityDto>,
    @SerialName("actions")
    val actions: List<ActionDto>,
    @SerialName("source")
    val source: SourceDto
)
