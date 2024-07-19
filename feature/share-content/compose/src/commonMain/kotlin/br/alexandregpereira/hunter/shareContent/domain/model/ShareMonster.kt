/*
 * Copyright 2024 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.shareContent.domain.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ShareMonster(
    val index: String,
    val name: String = "",
    val type: String = "",
    val challengeRating: Float = 0f,
    val imageUrl: String = "",
    val imageBackgroundColorLight: String = "",
    val imageBackgroundColorDark: String = "",
    val isHorizontalImage: Boolean = false,
    val subtype: String? = null,
    val group: String? = null,
    val subtitle: String = "",
    val size: String = "",
    val alignment: String = "",
    val armorClass: Int = 0,
    val hitPoints: Int = 0,
    val hitDice: String = "",
    val senses: List<String> = emptyList(),
    val languages: String = "",
    val sourceName: String = "",
    val hover: Boolean = false,
    val speedValues: List<ShareSpeedValue>,
    val abilityScores: List<ShareAbilityScore> = emptyList(),
    val savingThrows: List<ShareSavingThrow> = emptyList(),
    val skills: List<ShareSavingThrow> = emptyList(),
    val damageVulnerabilities: List<ShareType> = emptyList(),
    val damageResistances: List<ShareType> = emptyList(),
    val damageImmunities: List<ShareType> = emptyList(),
    val conditionImmunities: List<ShareType> = emptyList(),
    val specialAbilities: List<ShareType> = emptyList(),
    val actions: List<ShareAction> = emptyList(),
    val legendaryActions: List<ShareAction> = emptyList(),
    val reactions: List<ShareType> = emptyList(),
    val spellcaster: ShareSpellcasting? = null,
    val innateSpellcaster: ShareSpellcasting? = null,
    val lore: String? = null,
)

@Serializable
internal data class ShareSpeedValue(
    val type: String = "",
    val valueFormatted: String,
    val index: String = "",
)

@Serializable
internal data class ShareAbilityScore(
    val type: String,
    val value: Int,
    val modifier: Int
)

@Serializable
internal data class ShareSavingThrow(
    val index: String,
    val modifier: Int,
    val type: String
)

@Serializable
internal data class ShareType(
    val index: String,
    val type: String,
    val name: String
)

@Serializable
internal data class ShareAction(
    val id: String,
    val damageDices: List<ShareDamageDice>,
    val attackBonus: Int? = null,
    val abilityDescription: ShareType,
)

@Serializable
internal data class ShareDamageDice(
    val dice: String,
    val damage: ShareType,
)

@Serializable
internal data class ShareSpellcasting(
    val description: String,
    val usages: List<ShareSpellUsage>,
)

@Serializable
internal data class ShareSpellUsage(
    val group: String,
    val spells: List<ShareMonsterSpell>,
)

@Serializable
internal data class ShareMonsterSpell(
    val index: String,
    val name: String,
)
