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

package br.alexandregpereira.hunter.data.database.dao

import br.alexandregpereira.hunter.data.monster.local.entity.AbilityScoreEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ActionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ActionWithDamageDicesEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ConditionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageDiceEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageImmunityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageResistanceEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageVulnerabilityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ReactionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SavingThrowEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SkillEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpecialAbilityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpeedWithValuesEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellPreviewEntity
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
import br.alexandregpereira.hunter.database.ReactionQueries
import br.alexandregpereira.hunter.database.SavingThrowQueries
import br.alexandregpereira.hunter.database.SkillQueries
import br.alexandregpereira.hunter.database.SpecialAbilityQueries
import br.alexandregpereira.hunter.database.SpeedQueries
import br.alexandregpereira.hunter.database.SpeedValueQueries
import br.alexandregpereira.hunter.database.SpellUsageQueries
import br.alexandregpereira.hunter.database.SpellUsageSpellCrossRefQueries
import br.alexandregpereira.hunter.database.SpellcastingQueries

internal fun getSpeeds(
    monsterIndexes: List<String>,
    speedQueries: SpeedQueries,
    speedValueQueries: SpeedValueQueries,
): Map<String, SpeedWithValuesEntity> {
    val speedEntities = speedQueries.getByMonsterIndexes(monsterIndexes).executeAsList()
        .map { it.toLocalEntity() }

    val speedIds = speedEntities.map { it.id }
    val speedValuesMap = speedValueQueries.getBySpeedIds(speedIds).executeAsList()
        .map { it.toLocalEntity() }
        .groupBy { it.speedId }

    return speedEntities.associateWith { speedEntity ->
        val speedValues = speedValuesMap[speedEntity.id].orEmpty()
        SpeedWithValuesEntity(
            speed = speedEntity,
            values = speedValues
        )
    }.mapKeys { it.key.monsterIndex }
}

internal fun getAbilityScores(
    monsterIndexes: List<String>,
    abilityScoreQueries: AbilityScoreQueries
): Map<String, List<AbilityScoreEntity>> {
    return abilityScoreQueries.getByMonsterIndexes(monsterIndexes)
        .executeAsList()
        .map { it.toLocalEntity() }
        .groupBy { it.monsterIndex }
}

internal fun getSavingThrows(
    monsterIndexes: List<String>,
    savingThrowQueries: SavingThrowQueries
): Map<String, List<SavingThrowEntity>> {
    return savingThrowQueries.getByMonsterIndexes(monsterIndexes)
        .executeAsList()
        .map { it.toLocalEntity() }
        .groupBy { it.value.monsterIndex }
}

internal fun getSkills(
    monsterIndexes: List<String>,
    skillQueries: SkillQueries
): Map<String, List<SkillEntity>> {
    return skillQueries.getByMonsterIndexes(monsterIndexes)
        .executeAsList()
        .map { it.toLocalEntity() }
        .groupBy { it.value.monsterIndex }
}

internal fun getDamageImmunities(
    monsterIndexes: List<String>,
    damageImmunityQueries: DamageImmunityQueries
): Map<String, List<DamageImmunityEntity>> {
    return damageImmunityQueries.getByMonsterIndexes(monsterIndexes)
        .executeAsList()
        .map { it.toLocalEntity() }
        .groupBy { it.value.monsterIndex }
}

internal fun getDamageResistances(
    monsterIndexes: List<String>,
    damageResistanceQueries: DamageResistanceQueries
): Map<String, List<DamageResistanceEntity>> {
    return damageResistanceQueries.getByMonsterIndexes(monsterIndexes)
        .executeAsList()
        .map { it.toLocalEntity() }
        .groupBy { it.value.monsterIndex }
}

internal fun getDamageVulnerabilities(
    monsterIndexes: List<String>,
    damageVulnerabilityQueries: DamageVulnerabilityQueries
): Map<String, List<DamageVulnerabilityEntity>> {
    return damageVulnerabilityQueries.getByMonsterIndexes(monsterIndexes)
        .executeAsList()
        .map { it.toLocalEntity() }
        .groupBy { it.value.monsterIndex }
}

internal fun getConditionImmunities(
    monsterIndexes: List<String>,
    conditionQueries: ConditionQueries
): Map<String, List<ConditionEntity>> {
    return conditionQueries.getByMonsterIndexes(monsterIndexes)
        .executeAsList()
        .map { it.toLocalEntity() }
        .groupBy { it.value.monsterIndex }
}

internal fun getSpecialAbilities(
    monsterIndexes: List<String>,
    specialAbilityQueries: SpecialAbilityQueries
): Map<String, List<SpecialAbilityEntity>> {
    return specialAbilityQueries.getByMonsterIndexes(monsterIndexes)
        .executeAsList()
        .map { it.toLocalEntity() }
        .groupBy { it.monsterIndex }
}

internal fun getActions(
    monsterIndexes: List<String>,
    actionQueries: ActionQueries,
    damageDiceQueries: DamageDiceQueries
): Map<String, List<ActionWithDamageDicesEntity>> {
    val actions = actionQueries.getByMonsterIndexes(monsterIndexes)
        .executeAsList()
        .map { it.toLocalEntity() }

    return getActionWithDamageDicesEntities(actions, damageDiceQueries)
}

internal fun getLegendaryActions(
    monsterIndexes: List<String>,
    legendaryActionQueries: LegendaryActionQueries,
    damageDiceQueries: DamageDiceQueries
): Map<String, List<ActionWithDamageDicesEntity>> {
    val actions = legendaryActionQueries.getByMonsterIndexes(monsterIndexes)
        .executeAsList()
        .map { it.toLocalEntity() }

    return getActionWithDamageDicesEntities(actions, damageDiceQueries)
}

private fun getActionWithDamageDicesEntities(
    actions: List<ActionEntity>,
    damageDiceQueries: DamageDiceQueries
): Map<String, List<ActionWithDamageDicesEntity>> {
    val actionIds = actions.map { it.id }

    val damageDices = getActionWithDamageDices(actionIds, damageDiceQueries)

    return actions.map { action ->
        ActionWithDamageDicesEntity(
            action = action,
            damageDices = damageDices[action.id].orEmpty()
        )
    }.groupBy { it.action.monsterIndex }
}

private fun getActionWithDamageDices(
    actionIds: List<String>,
    damageDiceQueries: DamageDiceQueries
): Map<String, List<DamageDiceEntity>> {
    return damageDiceQueries.getByActionIds(actionIds)
        .executeAsList()
        .map { it.toLocalEntity() }
        .groupBy { it.actionId }
}

internal fun getReactions(
    monsterIndexes: List<String>,
    reactionQueries: ReactionQueries,
): Map<String, List<ReactionEntity>> {
    return reactionQueries.getByMonsterIndexes(monsterIndexes)
        .executeAsList()
        .map { it.toLocalEntity() }
        .groupBy { it.monsterIndex }
}

internal fun getSpellcastings(
    monsterIndexes: List<String>,
    spellcastingQueries: SpellcastingQueries,
    spellUsageQueries: SpellUsageQueries,
    spellUsageCrossRefQueries: SpellUsageSpellCrossRefQueries
): Map<String, List<SpellcastingCompleteEntity>> {
    val spellcastings = spellcastingQueries.getByMonsterIndexes(monsterIndexes).executeAsList()
        .map { it.toLocalEntity() }
    val spellcastingsIds = spellcastings.map { it.spellcastingId }

    val spellUsages = spellUsageQueries.getBySpellcastingIds(spellcastingsIds)
        .executeAsList().map { it.toLocalEntity() }
    val spellUsagesIds = spellUsages.map { it.spellUsageId }
    val spellUsagesMap = spellUsages.associateBy { it.spellUsageId }

    val spellsMap = spellUsageCrossRefQueries.getBySpellusageIdsAndSpellIndex(
        spellUsagesIds,
        mapper = { spellIndex: String, level: Long, name: String, school: String, spellUsageId: String ->
            spellUsageId to SpellPreviewEntity(
                spellIndex = spellIndex,
                name = name,
                level = level.toInt(),
                school = school
            )
        }
    ).executeAsList().groupBy { it.first }.mapValues { (_, values) ->
        values.map { it.second }
    }

    val usagesMap = spellsMap.map { (spellUsageId, spells) ->
        SpellUsageCompleteEntity(
            spellUsage = spellUsagesMap[spellUsageId] ?: throw IllegalStateException("Spell usage not found"),
            spells = spells
        )
    }.groupBy { it.spellUsage.spellcastingId }

    return spellcastings.map { spellcastingEntity ->
        SpellcastingCompleteEntity(
            spellcasting = spellcastingEntity,
            usages = usagesMap[spellcastingEntity.spellcastingId].orEmpty()
        )
    }.groupBy { it.spellcasting.monsterIndex }
}
