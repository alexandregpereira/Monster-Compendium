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
import br.alexandregpereira.hunter.data.monster.local.entity.ActionWithDamageDicesEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ConditionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageImmunityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageResistanceEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageVulnerabilityEntity
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

internal fun getSpeed(
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

internal fun getAbilityScores(
    monster: MonsterEntity,
    abilityScoreQueries: AbilityScoreQueries
): List<AbilityScoreEntity> {
    return abilityScoreQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun getSavingThrows(
    monster: MonsterEntity,
    savingThrowQueries: SavingThrowQueries
): List<SavingThrowEntity> {
    return savingThrowQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun getSkills(
    monster: MonsterEntity,
    skillQueries: SkillQueries
): List<SkillEntity> {
    return skillQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun getDamageImmunities(
    monster: MonsterEntity,
    damageImmunityQueries: DamageImmunityQueries
): List<DamageImmunityEntity> {
    return damageImmunityQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun getDamageResistances(
    monster: MonsterEntity,
    damageResistanceQueries: DamageResistanceQueries
): List<DamageResistanceEntity> {
    return damageResistanceQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun getDamageVulnerabilities(
    monster: MonsterEntity,
    damageVulnerabilityQueries: DamageVulnerabilityQueries
): List<DamageVulnerabilityEntity> {
    return damageVulnerabilityQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun getConditionImmunities(
    monster: MonsterEntity,
    conditionQueries: ConditionQueries
): List<ConditionEntity> {
    return conditionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun getSpecialAbilities(
    monster: MonsterEntity,
    specialAbilityQueries: SpecialAbilityQueries
): List<SpecialAbilityEntity> {
    return specialAbilityQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun getActions(
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

internal fun getLegendaryActions(
    monster: MonsterEntity,
    legendaryActionQueries: LegendaryActionQueries,
    damageDiceQueries: DamageDiceQueries
): List<ActionWithDamageDicesEntity> {
    return legendaryActionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { legendaryAction ->
            val damageDices = damageDiceQueries.getByActionId(legendaryAction.id).executeAsList()
                .map { it.toLocalEntity() }
            ActionWithDamageDicesEntity(
                action = legendaryAction.toLocalEntity(),
                damageDices = damageDices
            )
        }
}

internal fun getReactions(
    monster: MonsterEntity,
    reactionQueries: ReactionQueries,
): List<ReactionEntity> {
    return reactionQueries.getByMonterIndex(monster.index).executeAsList()
        .map { it.toLocalEntity() }
}

internal fun getSpellcastings(
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
