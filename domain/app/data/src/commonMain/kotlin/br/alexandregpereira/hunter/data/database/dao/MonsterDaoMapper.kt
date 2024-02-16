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

import br.alexandregpereira.hunter.data.monster.local.entity.*
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellPreviewEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageSpellCrossRefEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingSpellUsageCrossRefEntity
import br.alexandregpereira.hunter.database.GetBySpellusageIdAndSpellIndex
import br.alexandregpereira.hunter.database.AbilityScoreEntity as AbilityScoreDatabaseEntity
import br.alexandregpereira.hunter.database.ActionEntity as ActionDatabaseEntity
import br.alexandregpereira.hunter.database.ConditionEntity as ConditionDatabaseEntity
import br.alexandregpereira.hunter.database.DamageDiceEntity as DamageDiceDatabaseEntity
import br.alexandregpereira.hunter.database.DamageImmunityEntity as DamageImmunityDatabaseEntity
import br.alexandregpereira.hunter.database.DamageResistanceEntity as DamageResistanceDatabaseEntity
import br.alexandregpereira.hunter.database.DamageVulnerabilityEntity as DamageVulnerabilityDatabaseEntity
import br.alexandregpereira.hunter.database.LegendaryActionEntity as LegendaryActionDatabaseEntity
import br.alexandregpereira.hunter.database.MonsterEntity as MonsterDatabaseEntity
import br.alexandregpereira.hunter.database.ReactionEntity as ReactionDatabaseEntity
import br.alexandregpereira.hunter.database.SavingThrowEntity as SavingThrowDatabaseEntity
import br.alexandregpereira.hunter.database.SkillEntity as SkillDatabaseEntity
import br.alexandregpereira.hunter.database.SpecialAbilityEntity as SpecialAbilityDatabaseEntity
import br.alexandregpereira.hunter.database.SpeedEntity as SpeedDatabaseEntity
import br.alexandregpereira.hunter.database.SpeedValueEntity as SpeedValueDatabaseEntity
import br.alexandregpereira.hunter.database.SpellPreviewEntity as SpellPreviewDatabaseEntity
import br.alexandregpereira.hunter.database.SpellUsageEntity as SpellUsageDatabaseEntity
import br.alexandregpereira.hunter.database.SpellUsageSpellCrossRefEntity as SpellUsageSpellCrossRefDatabaseEntity
import br.alexandregpereira.hunter.database.SpellcastingEntity as SpellcastingDatabaseEntity
import br.alexandregpereira.hunter.database.SpellcastingSpellUsageCrossRefEntity as SpellcastingSpellUsageCrossRefDatabaseEntity

internal fun AbilityScoreEntity.toDatabaseEntity(): AbilityScoreDatabaseEntity {
    return AbilityScoreDatabaseEntity(
        type = this.type,
        value_ = this.value.toLong(),
        modifier = this.modifier.toLong(),
        monsterIndex = this.monsterIndex
    )
}

internal fun ActionEntity.toDatabaseEntity(): ActionDatabaseEntity {
    return ActionDatabaseEntity(
        id = this.id,
        attackBonus = this.attackBonus?.toLong(),
        description = this.description,
        name = this.name,
        monsterIndex = this.monsterIndex
    )
}

internal fun ConditionEntity.toDatabaseEntity(): ConditionDatabaseEntity {
    return ConditionDatabaseEntity(
        index = this.value.index,
        type = this.value.type,
        name = this.value.name,
        monsterIndex = this.value.monsterIndex
    )
}

internal fun DamageDiceEntity.toDatabaseEntity(): DamageDiceDatabaseEntity {
    return DamageDiceDatabaseEntity(
        id = this.id,
        dice = this.dice,
        actionId = this.actionId,
        index = this.damage.index,
        type = this.damage.type,
        name = this.damage.name,
        monsterIndex = this.damage.monsterIndex
    )
}

internal fun ActionEntity.toLegendaryActionDatabaseEntity(): LegendaryActionDatabaseEntity {
    return LegendaryActionDatabaseEntity(
        id = this.id,
        attackBonus = this.attackBonus?.toLong(),
        description = this.description,
        name = this.name,
        monsterIndex = this.monsterIndex
    )
}

internal fun MonsterEntity.toDatabaseEntity(): MonsterDatabaseEntity {
    return MonsterDatabaseEntity(
        index = this.index,
        type = this.type,
        subtype = this.subtype,
        group = this.group,
        challengeRating = this.challengeRating.toDouble(),
        name = this.name,
        subtitle = this.subtitle,
        imageUrl = this.imageUrl,
        backgroundColorLight = this.backgroundColorLight,
        backgroundColorDark = this.backgroundColorDark,
        isHorizontalImage = if (this.isHorizontalImage) 1L else 0L,
        size = this.size,
        alignment = this.alignment,
        armorClass = this.armorClass.toLong(),
        hitPoints = this.hitPoints.toLong(),
        hitDice = this.hitDice,
        senses = this.senses,
        languages = this.languages,
        sourceName = this.sourceName,
        isClone = if (this.isClone) 1L else 0L
    )
}

internal fun ReactionEntity.toDatabaseEntity(): ReactionDatabaseEntity {
    return ReactionDatabaseEntity(
        name = this.name,
        description = this.description,
        monsterIndex = this.monsterIndex
    )
}

internal fun SavingThrowEntity.toDatabaseEntity(): SavingThrowDatabaseEntity {
    return SavingThrowDatabaseEntity(
        index = this.value.index,
        modifier = this.value.modifier.toLong(),
        name = this.value.name,
        monsterIndex = this.value.monsterIndex
    )
}

internal fun SkillEntity.toDatabaseEntity(): SkillDatabaseEntity {
    return SkillDatabaseEntity(
        index = this.value.index,
        modifier = this.value.modifier.toLong(),
        name = this.value.name,
        monsterIndex = this.value.monsterIndex
    )
}

internal fun SpecialAbilityEntity.toDatabaseEntity(): SpecialAbilityDatabaseEntity {
    return SpecialAbilityDatabaseEntity(
        name = this.name,
        description = this.description,
        monsterIndex = this.monsterIndex
    )
}

internal fun SpeedEntity.toDatabaseEntity(): SpeedDatabaseEntity {
    return SpeedDatabaseEntity(
        id = this.id,
        hover = if (this.hover) 1L else 0L,
        monsterIndex = this.monsterIndex
    )
}

internal fun SpeedValueEntity.toDatabaseEntity(): SpeedValueDatabaseEntity {
    return SpeedValueDatabaseEntity(
        type = this.type,
        valueFormatted = this.valueFormatted,
        speedId = this.speedId
    )
}

internal fun SpellcastingSpellUsageCrossRefEntity.toDatabaseEntity(): SpellcastingSpellUsageCrossRefDatabaseEntity {
    return SpellcastingSpellUsageCrossRefDatabaseEntity(
        spellcastingId = spellcastingId,
        spellUsageId = spellUsageId
    )
}

internal fun SpellUsageSpellCrossRefEntity.toDatabaseEntity(): SpellUsageSpellCrossRefDatabaseEntity {
    return SpellUsageSpellCrossRefDatabaseEntity(
        spellUsageId = spellUsageId,
        spellIndex = spellIndex
    )
}

internal fun AbilityScoreDatabaseEntity.toLocalEntity(): AbilityScoreEntity {
    return AbilityScoreEntity(
        type = this.type,
        value = this.value_.toInt(),
        modifier = this.modifier.toInt(),
        monsterIndex = this.monsterIndex
    )
}

internal fun ActionDatabaseEntity.toLocalEntity(): ActionEntity {
    return ActionEntity(
        id = this.id,
        attackBonus = this.attackBonus?.toInt(),
        description = this.description,
        name = this.name,
        monsterIndex = this.monsterIndex
    )
}

internal fun ConditionDatabaseEntity.toLocalEntity(): ConditionEntity {
    return ConditionEntity(
        value = ValueEntity(
            index = this.index,
            type = this.type,
            name = this.name,
            monsterIndex = this.monsterIndex
        )
    )
}

internal fun DamageDiceDatabaseEntity.toLocalEntity(): DamageDiceEntity {
    return DamageDiceEntity(
        id = this.id,
        dice = this.dice,
        damage = ValueEntity(
            index = index,
            type = type,
            name = name,
            monsterIndex = monsterIndex
        ),
        actionId = this.actionId
    )
}

internal fun DamageVulnerabilityDatabaseEntity.toLocalEntity(): DamageVulnerabilityEntity {
    return DamageVulnerabilityEntity(
        value = ValueEntity(
            index = this.index,
            type = this.type,
            name = this.name,
            monsterIndex = this.monsterIndex
        )
    )
}

internal fun DamageVulnerabilityEntity.toDatabaseEntity(): DamageVulnerabilityDatabaseEntity {
    return DamageVulnerabilityDatabaseEntity(
        index = this.value.index,
        type = this.value.type,
        name = this.value.name,
        monsterIndex = this.value.monsterIndex
    )
}

internal fun DamageResistanceDatabaseEntity.toLocalEntity(): DamageResistanceEntity {
    return DamageResistanceEntity(
        value = ValueEntity(
            index = this.index,
            type = this.type,
            name = this.name,
            monsterIndex = this.monsterIndex
        )
    )
}

internal fun DamageResistanceEntity.toDatabaseEntity(): DamageResistanceDatabaseEntity {
    return DamageResistanceDatabaseEntity(
        index = this.value.index,
        type = this.value.type,
        name = this.value.name,
        monsterIndex = this.value.monsterIndex
    )
}

internal fun DamageImmunityDatabaseEntity.toLocalEntity(): DamageImmunityEntity {
    return DamageImmunityEntity(
        value = ValueEntity(
            index = this.index,
            type = this.type,
            name = this.name,
            monsterIndex = this.monsterIndex
        )
    )
}

internal fun DamageImmunityEntity.toDatabaseEntity(): DamageImmunityDatabaseEntity {
    return DamageImmunityDatabaseEntity(
        index = this.value.index,
        type = this.value.type,
        name = this.value.name,
        monsterIndex = this.value.monsterIndex
    )
}

internal fun LegendaryActionDatabaseEntity.toLocalEntity(): ActionEntity {
    return ActionEntity(
        id = this.id,
        attackBonus = this.attackBonus?.toInt(),
        description = this.description,
        name = this.name,
        monsterIndex = this.monsterIndex
    )
}

internal fun MonsterDatabaseEntity.toLocalEntity(): MonsterEntity {
    return MonsterEntity(
        index = this.index,
        type = this.type,
        subtype = this.subtype,
        group = this.group,
        challengeRating = this.challengeRating.toFloat(),
        name = this.name,
        subtitle = this.subtitle,
        imageUrl = this.imageUrl,
        backgroundColorLight = this.backgroundColorLight,
        backgroundColorDark = this.backgroundColorDark,
        isHorizontalImage = this.isHorizontalImage == 1L,
        size = this.size,
        alignment = this.alignment,
        armorClass = this.armorClass.toInt(),
        hitPoints = this.hitPoints.toInt(),
        hitDice = this.hitDice,
        senses = this.senses,
        languages = this.languages,
        sourceName = this.sourceName,
        isClone = this.isClone == 1L,
    )
}

internal fun ReactionDatabaseEntity.toLocalEntity(): ReactionEntity {
    return ReactionEntity(
        name = this.name,
        description = this.description,
        monsterIndex = this.monsterIndex
    )
}

internal fun SavingThrowDatabaseEntity.toLocalEntity(): SavingThrowEntity {
    return SavingThrowEntity(
        value = ProficiencyEntity(
            index = this.index,
            modifier = this.modifier.toInt(),
            name = this.name,
            monsterIndex = this.monsterIndex
        )
    )
}

internal fun SkillDatabaseEntity.toLocalEntity(): SkillEntity {
    return SkillEntity(
        value = ProficiencyEntity(
            index = this.index,
            modifier = this.modifier.toInt(),
            name = this.name,
            monsterIndex = this.monsterIndex
        )
    )
}

internal fun SpecialAbilityDatabaseEntity.toLocalEntity(): SpecialAbilityEntity {
    return SpecialAbilityEntity(
        name = this.name,
        description = this.description,
        monsterIndex = this.monsterIndex
    )
}

internal fun SpeedDatabaseEntity.toLocalEntity(): SpeedEntity {
    return SpeedEntity(
        id = this.id,
        hover = this.hover == 1L,
        monsterIndex = this.monsterIndex
    )
}

internal fun SpeedValueDatabaseEntity.toLocalEntity(): SpeedValueEntity {
    return SpeedValueEntity(
        type = this.type,
        valueFormatted = this.valueFormatted,
        speedId = this.speedId
    )
}

internal fun SpellcastingDatabaseEntity.toLocalEntity(): SpellcastingEntity {
    return SpellcastingEntity(
        spellcastingId = this.spellcastingId,
        type = this.type,
        description = this.description,
        monsterIndex = this.monsterIndex
    )
}

internal fun SpellcastingEntity.toDatabaseEntity(): SpellcastingDatabaseEntity {
    return SpellcastingDatabaseEntity(
        spellcastingId = this.spellcastingId,
        type = this.type,
        description = this.description,
        monsterIndex = monsterIndex
    )
}

internal fun GetBySpellusageIdAndSpellIndex.toLocalEntity(): SpellPreviewEntity {
    return SpellPreviewEntity(
        spellIndex = this.spellIndex,
        name = this.name,
        level = this.level.toInt(),
        school = this.school,
    )
}

internal fun SpellPreviewEntity.toDatabaseEntity(): SpellPreviewDatabaseEntity {
    return SpellPreviewDatabaseEntity(
        spellIndex = this.spellIndex,
        name = this.name,
        level = this.level.toLong(),
        school = this.school,
    )
}

internal fun SpellUsageDatabaseEntity.toLocalEntity(): SpellUsageEntity {
    return SpellUsageEntity(
        spellUsageId = this.spellUsageId,
        group = this.group,
        spellcastingId = this.spellcastingId
    )
}

internal fun SpellUsageEntity.toDatabaseEntity(): SpellUsageDatabaseEntity {
    return SpellUsageDatabaseEntity(
        spellUsageId = this.spellUsageId,
        group = this.group,
        spellcastingId = this.spellcastingId
    )
}
