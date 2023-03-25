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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

internal fun CoroutineScope.getSpeedAsync(
    monster: MonsterEntity,
    speedQueries: SpeedQueries,
    speedValueQueries: SpeedValueQueries
): Deferred<SpeedWithValuesEntity> = async {
    val speedEntity = speedQueries.getByMonterIndex(monster.index).executeAsOne().toLocalEntity()
    SpeedWithValuesEntity(
        speed = speedEntity,
        values = speedValueQueries.getBySpeedId(speedEntity.id).executeAsList()
            .map { it.toLocalEntity() }
    )
}

internal fun CoroutineScope.getAbilityScoresAsync(
    monster: MonsterEntity,
    abilityScoreQueries: AbilityScoreQueries
): Deferred<List<AbilityScoreEntity>> = async {
    abilityScoreQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun CoroutineScope.getSavingThrowsAsync(
    monster: MonsterEntity,
    savingThrowQueries: SavingThrowQueries
): Deferred<List<SavingThrowEntity>> = async {
    savingThrowQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun CoroutineScope.getSkillsAsync(
    monster: MonsterEntity,
    skillQueries: SkillQueries
): Deferred<List<SkillEntity>> = async {
    skillQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun CoroutineScope.getDamageVulnerabilitiesAsync(
    monster: MonsterEntity,
    damageVulnerabilityQueries: DamageVulnerabilityQueries
): Deferred<List<DamageVulnerabilityEntity>> = async {
    damageVulnerabilityQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun CoroutineScope.getDamageResistancesAsync(
    monster: MonsterEntity,
    damageResistanceQueries: DamageResistanceQueries
): Deferred<List<DamageResistanceEntity>> = async {
    damageResistanceQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun CoroutineScope.getDamageImmunitiesAsync(
    monster: MonsterEntity,
    damageImmunityQueries: DamageImmunityQueries
): Deferred<List<DamageImmunityEntity>> = async {
    damageImmunityQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun CoroutineScope.getConditionImmunitiesAsync(
    monster: MonsterEntity,
    conditionQueries: ConditionQueries
): Deferred<List<ConditionEntity>> = async {
    conditionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun CoroutineScope.getSpecialAbilitiesAsync(
    monster: MonsterEntity,
    specialAbilityQueries: SpecialAbilityQueries
): Deferred<List<SpecialAbilityEntity>> = async {
    specialAbilityQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun CoroutineScope.getActionsAsync(
    monster: MonsterEntity,
    actionQueries: ActionQueries,
    damageDiceQueries: DamageDiceQueries
): Deferred<List<ActionWithDamageDicesEntity>> = async {
    actionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
        .map { action ->
            ActionWithDamageDicesEntity(
                action = action,
                damageDices = damageDiceQueries.getByActionId(action.id)
                    .executeAsList().map { it.toLocalEntity() }
            )
        }
}

internal fun CoroutineScope.getLegendaryActionsAsync(
    monster: MonsterEntity,
    legendaryActionQueries: LegendaryActionQueries,
    damageDiceQueries: DamageDiceQueries
): Deferred<List<LegendaryActionWithDamageDicesEntity>> = async {
    legendaryActionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
        .map { action ->
            LegendaryActionWithDamageDicesEntity(
                action = action,
                damageDices = damageDiceQueries.getByActionId(action.id)
                    .executeAsList().map { it.toLocalEntity() }
            )
        }
}

internal fun CoroutineScope.getReactionsAsync(
    monster: MonsterEntity,
    reactionQueries: ReactionQueries
): Deferred<List<ReactionEntity>> = async {
    reactionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun CoroutineScope.getSpellcastingsAsync(
    monster: MonsterEntity,
    spellcastingQueries: SpellcastingQueries,
    spellUsageQueries: SpellUsageQueries,
    spellUsageCrossRefQueries: SpellUsageSpellCrossRefQueries
): Deferred<List<SpellcastingCompleteEntity>> = async {
    spellcastingQueries.getByMonterIndex(monster.index).executeAsList()
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
