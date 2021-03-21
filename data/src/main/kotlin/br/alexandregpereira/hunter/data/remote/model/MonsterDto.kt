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

package br.alexandregpereira.hunter.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonsterDto(
    @SerialName("index")
    val index: String,
    @SerialName("type")
    val type: MonsterTypeDto,
    @SerialName("name")
    val name: String,
    @SerialName("subtitle")
    val subtitle: String,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("background_color")
    val backgroundColor: String = "#00000000",
    @SerialName("size")
    val size: String,
    @SerialName("alignment")
    val alignment: String,
    @SerialName("subtype")
    val subtype: String?,
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
    val damageImmunities: List<DamageDto>
)

