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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal suspend fun getSpeed(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    speedQueries: SpeedQueries,
    speedValueQueries: SpeedValueQueries
): SpeedWithValuesEntity? = withContext(dispatcher) {
    val speedEntity = speedQueries.getByMonterIndex(monster.index).executeAsList()
        .firstOrNull()?.toLocalEntity() ?: return@withContext null
    val speedValues = speedValueQueries.getBySpeedId(speedEntity.id).executeAsList()
        .map { it.toLocalEntity() }.takeIf { it.isNotEmpty() } ?: return@withContext null

    SpeedWithValuesEntity(
        speed = speedEntity,
        values = speedValues
    )
}

internal suspend fun getAbilityScores(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    abilityScoreQueries: AbilityScoreQueries
): List<AbilityScoreEntity> = withContext(dispatcher) {
    abilityScoreQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal suspend fun getSavingThrows(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    savingThrowQueries: SavingThrowQueries
): List<SavingThrowEntity> = withContext(dispatcher) {
    savingThrowQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal suspend fun getSkills(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    skillQueries: SkillQueries
): List<SkillEntity> = withContext(dispatcher) {
    skillQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal suspend fun getDamageImmunities(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    damageImmunityQueries: DamageImmunityQueries
): List<DamageImmunityEntity> = withContext(dispatcher) {
    damageImmunityQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal suspend fun getDamageResistances(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    damageResistanceQueries: DamageResistanceQueries
): List<DamageResistanceEntity> = withContext(dispatcher) {
    damageResistanceQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal suspend fun getDamageVulnerabilities(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    damageVulnerabilityQueries: DamageVulnerabilityQueries
): List<DamageVulnerabilityEntity> = withContext(dispatcher) {
    damageVulnerabilityQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal suspend fun getConditionImmunities(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    conditionQueries: ConditionQueries
): List<ConditionEntity> = withContext(dispatcher) {
    conditionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal suspend fun getSpecialAbilities(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    specialAbilityQueries: SpecialAbilityQueries
): List<SpecialAbilityEntity> = withContext(dispatcher) {
    specialAbilityQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal suspend fun getActions(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    actionQueries: ActionQueries,
    damageDiceQueries: DamageDiceQueries
): List<ActionWithDamageDicesEntity> = withContext(dispatcher) {
    actionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { action ->
            val damageDices = damageDiceQueries.getByActionId(action.id).executeAsList()
                .map { it.toLocalEntity() }
            ActionWithDamageDicesEntity(
                action = action.toLocalEntity(),
                damageDices = damageDices
            )
        }
}

internal suspend fun getLegendaryActions(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    legendaryActionQueries: LegendaryActionQueries,
    damageDiceQueries: DamageDiceQueries
): List<LegendaryActionWithDamageDicesEntity> = withContext(dispatcher) {
    legendaryActionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { legendaryAction ->
            val damageDices = damageDiceQueries.getByActionId(legendaryAction.id).executeAsList()
                .map { it.toLocalEntity() }
            LegendaryActionWithDamageDicesEntity(
                action = legendaryAction.toLocalEntity(),
                damageDices = damageDices
            )
        }
}

internal suspend fun getReactions(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    reactionQueries: ReactionQueries,
): List<ReactionEntity> = withContext(dispatcher) {
    reactionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal suspend fun getSpellcastings(
    dispatcher: CoroutineDispatcher,
    monster: MonsterEntity,
    spellcastingQueries: SpellcastingQueries,
    spellUsageQueries: SpellUsageQueries,
    spellUsageCrossRefQueries: SpellUsageSpellCrossRefQueries
): List<SpellcastingCompleteEntity> = withContext(dispatcher) {
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
