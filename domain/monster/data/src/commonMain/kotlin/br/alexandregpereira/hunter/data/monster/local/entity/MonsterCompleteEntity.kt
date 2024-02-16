/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster.local.entity

import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingCompleteEntity

data class MonsterCompleteEntity(
    val monster: MonsterEntity,
    val speed: SpeedWithValuesEntity?,
    val abilityScores: List<AbilityScoreEntity>,
    val savingThrows: List<SavingThrowEntity>,
    val skills: List<SkillEntity>,
    val damageVulnerabilities: List<DamageVulnerabilityEntity>,
    val damageResistances: List<DamageResistanceEntity>,
    val damageImmunities: List<DamageImmunityEntity>,
    val conditionImmunities: List<ConditionEntity>,
    val specialAbilities: List<SpecialAbilityEntity>,
    val actions: List<ActionWithDamageDicesEntity>,
    val legendaryActions: List<ActionWithDamageDicesEntity>,
    val reactions: List<ReactionEntity>,
    val spellcastings: List<SpellcastingCompleteEntity>,
)
