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

package br.alexandregpereira.hunter.data.database.dao

import br.alexandregpereira.hunter.data.monster.local.entity.AbilityScoreEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ActionWithDamageDicesEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ConditionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageImmunityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageResistanceEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageVulnerabilityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.LegendaryActionWithDamageDicesEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ReactionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SavingThrowEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SkillEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpecialAbilityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpeedWithValuesEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageCompleteEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingCompleteEntity
import br.alexandregpereira.hunter.database.AbilityScoreQueries
import br.alexandregpereira.hunter.database.ActionQueries
import br.alexandregpereira.hunter.database.ConditionQueries
import br.alexandregpereira.hunter.database.DamageDiceQueries
import br.alexandregpereira.hunter.database.DamageImmunityQueries
import br.alexandregpereira.hunter.database.DamageResistanceQueries
import br.alexandregpereira.hunter.database.DamageVulnerabilityQueries
import br.alexandregpereira.hunter.database.LegendaryActionQueries
import br.alexandregpereira.hunter.database.MonsterEntity
import br.alexandregpereira.hunter.database.ReactionQueries
import br.alexandregpereira.hunter.database.SavingThrowQueries
import br.alexandregpereira.hunter.database.SkillQueries
import br.alexandregpereira.hunter.database.SpecialAbilityQueries
import br.alexandregpereira.hunter.database.SpeedQueries
import br.alexandregpereira.hunter.database.SpeedValueQueries
import br.alexandregpereira.hunter.database.SpellUsageQueries
import br.alexandregpereira.hunter.database.SpellUsageSpellCrossRefQueries
import br.alexandregpereira.hunter.database.SpellcastingQueries

fun getSpeed(
    monster: MonsterEntity,
    speedQueries: SpeedQueries,
    speedValueQueries: SpeedValueQueries
): SpeedWithValuesEntity? {
    val speedEntity = speedQueries.getByMonterIndex(monster.index).executeAsList()
        .firstOrNull()?.toLocalEntity() ?: return null
    val speedValues = speedValueQueries.getBySpeedId(speedEntity.id).executeAsList()
        .map { it.toLocalEntity() }.takeIf { it.isNotEmpty() } ?: return null

    return SpeedWithValuesEntity(
        speed = speedEntity,
        values = speedValues
    )
}


fun getAbilityScores(
    monster: MonsterEntity,
    abilityScoreQueries: AbilityScoreQueries
): List<AbilityScoreEntity> {
    return abilityScoreQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

fun getSavingThrows(
    monster: MonsterEntity,
    savingThrowQueries: SavingThrowQueries
): List<SavingThrowEntity> {
    return savingThrowQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

fun getSkills(
    monster: MonsterEntity,
    skillQueries: SkillQueries
): List<SkillEntity> {
    return skillQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

fun getDamageImmunities(
    monster: MonsterEntity,
    damageImmunityQueries: DamageImmunityQueries
): List<DamageImmunityEntity> {
    return damageImmunityQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

fun getDamageResistances(
    monster: MonsterEntity,
    damageResistanceQueries: DamageResistanceQueries
): List<DamageResistanceEntity> {
    return damageResistanceQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

fun getDamageVulnerabilities(
    monster: MonsterEntity,
    damageVulnerabilityQueries: DamageVulnerabilityQueries
): List<DamageVulnerabilityEntity> {
    return damageVulnerabilityQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

fun getConditionImmunities(
    monster: MonsterEntity,
    conditionQueries: ConditionQueries
): List<ConditionEntity> {
    return conditionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

fun getSpecialAbilities(
    monster: MonsterEntity,
    specialAbilityQueries: SpecialAbilityQueries
): List<SpecialAbilityEntity> {
    return specialAbilityQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

fun getActions(
    monster: MonsterEntity,
    actionQueries: ActionQueries,
    damageDiceQueries: DamageDiceQueries
): List<ActionWithDamageDicesEntity> {
    return actionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { action ->
            val damageDices = damageDiceQueries.getByActionId(action.id).executeAsList()
                .map { it.toLocalEntity() }
            ActionWithDamageDicesEntity(
                action = action.toLocalEntity(),
                damageDices = damageDices
            )
        }
}

fun getLegendaryActions(
    monster: MonsterEntity,
    legendaryActionQueries: LegendaryActionQueries,
    damageDiceQueries: DamageDiceQueries
): List<LegendaryActionWithDamageDicesEntity> {
    return legendaryActionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { legendaryAction ->
            val damageDices = damageDiceQueries.getByActionId(legendaryAction.id).executeAsList()
                .map { it.toLocalEntity() }
            LegendaryActionWithDamageDicesEntity(
                action = legendaryAction.toLocalEntity(),
                damageDices = damageDices
            )
        }
}

fun getReactions(
    monster: MonsterEntity,
    reactionQueries: ReactionQueries,
): List<ReactionEntity> {
    return reactionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

fun getSpellcastings(
    monster: MonsterEntity,
    spellcastingQueries: SpellcastingQueries,
    spellUsageQueries: SpellUsageQueries,
    spellUsageCrossRefQueries: SpellUsageSpellCrossRefQueries
): List<SpellcastingCompleteEntity> {
    return spellcastingQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
        .map { spellcasting ->
            val spellUsages = spellUsageQueries.getBySpellcastingId(spellcasting.spellcastingId)
                .executeAsList().map { it.toLocalEntity() }
            SpellcastingCompleteEntity(
                spellcasting = spellcasting,
                usages = spellUsages.map { spellUsageEntity ->
                    SpellUsageCompleteEntity(
                        spellUsage = spellUsageEntity,
                        spells = spellUsageCrossRefQueries.getBySpellusageIdAndSpellIndex(
                            spellUsageEntity.spellUsageId
                        ).executeAsList().map {
                            it.toLocalEntity()
                        }
                    )
                }
            )
        }
}
