package br.alexandregpereira.hunter.data.monster.remote.mapper

import br.alexandregpereira.hunter.data.monster.remote.model.AbilityScoreDto
import br.alexandregpereira.hunter.data.monster.remote.model.AbilityScoreTypeDto
import br.alexandregpereira.hunter.data.monster.remote.model.ActionDto
import br.alexandregpereira.hunter.data.monster.remote.model.ColorDto
import br.alexandregpereira.hunter.data.monster.remote.model.ConditionDto
import br.alexandregpereira.hunter.data.monster.remote.model.DamageDiceDto
import br.alexandregpereira.hunter.data.monster.remote.model.DamageDto
import br.alexandregpereira.hunter.data.monster.remote.model.MeasurementUnitDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageContentScaleDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterTypeDto
import br.alexandregpereira.hunter.data.monster.remote.model.SavingThrowDto
import br.alexandregpereira.hunter.data.monster.remote.model.SkillDto
import br.alexandregpereira.hunter.data.monster.remote.model.SourceDto
import br.alexandregpereira.hunter.data.monster.remote.model.SpeedDto
import br.alexandregpereira.hunter.data.monster.remote.model.SpeedTypeDto
import br.alexandregpereira.hunter.data.monster.remote.model.SpeedValueDto
import br.alexandregpereira.hunter.data.monster.spell.remote.model.SchoolOfMagicDto
import br.alexandregpereira.hunter.data.monster.spell.remote.model.SpellPreviewDto
import br.alexandregpereira.hunter.data.monster.spell.remote.model.SpellUsageDto
import br.alexandregpereira.hunter.data.monster.spell.remote.model.SpellcastingDto
import br.alexandregpereira.hunter.data.monster.spell.remote.model.SpellcastingTypeDto
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.SavingThrow
import br.alexandregpereira.hunter.domain.model.Skill
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting

internal fun List<Monster>.toDtos(): List<MonsterDto> {
    return map { it.toDto() }
}

internal fun Monster.toDto(): MonsterDto {
    return MonsterDto(
        index = index,
        name = name,
        type = type.toMonsterTypeDto(),
        challengeRating = challengeRating,
        subtype = subtype,
        group = group,
        subtitle = subtitle,
        size = size,
        alignment = alignment,
        armorClass = stats.armorClass,
        hitPoints = stats.hitPoints,
        hitDice = stats.hitDice,
        initiative = stats.initiative,
        speed = speed.toSpeedDto(),
        abilityScores = abilityScores.toAbilityScoreDtos(),
        savingThrows = savingThrows.toSavingThrowDtos(),
        skills = skills.toSkillDtos(),
        damageVulnerabilities = damageVulnerabilities.toDamageDtos(),
        damageResistances = damageResistances.toDamageDtos(),
        damageImmunities = damageImmunities.toDamageDtos(),
        conditionImmunities = conditionImmunities.toConditionDtos(),
        senses = senses,
        languages = languages,
        specialAbilities = specialAbilities.toActionDtos(),
        actions = actions.toActionDtos(),
        bonusActions = bonusActions.toActionDtos(),
        legendaryActions = legendaryActions.toActionDtos(),
        reactions = reactions.toActionDtos(),
        spellcastings = spellcastings.toSpellcastingDtos(),
        source = SourceDto(name = sourceName, acronym = ""),
        lore = lore,
        status = status.name,
        image = MonsterImageDto(
            monsterIndex = index,
            backgroundColor = ColorDto(
                light = imageData.backgroundColor.light,
                dark = imageData.backgroundColor.dark
            ),
            imageUrl = imageData.url,
            contentScale = imageData.contentScale?.let {
                when (it) {
                    MonsterImageContentScale.Fit -> MonsterImageContentScaleDto.Fit
                    MonsterImageContentScale.Crop -> MonsterImageContentScaleDto.Crop
                }
            },
        ),
    )
}

private fun MonsterType.toMonsterTypeDto(): MonsterTypeDto {
    return when (this) {
        MonsterType.ABERRATION -> MonsterTypeDto.ABERRATION
        MonsterType.BEAST -> MonsterTypeDto.BEAST
        MonsterType.CELESTIAL -> MonsterTypeDto.CELESTIAL
        MonsterType.CONSTRUCT -> MonsterTypeDto.CONSTRUCT
        MonsterType.DRAGON -> MonsterTypeDto.DRAGON
        MonsterType.ELEMENTAL -> MonsterTypeDto.ELEMENTAL
        MonsterType.FEY -> MonsterTypeDto.FEY
        MonsterType.FIEND -> MonsterTypeDto.FIEND
        MonsterType.GIANT -> MonsterTypeDto.GIANT
        MonsterType.HUMANOID -> MonsterTypeDto.HUMANOID
        MonsterType.MONSTROSITY -> MonsterTypeDto.MONSTROSITY
        MonsterType.OOZE -> MonsterTypeDto.OOZE
        MonsterType.PLANT -> MonsterTypeDto.PLANT
        MonsterType.UNDEAD -> MonsterTypeDto.UNDEAD
    }
}

private fun Speed.toSpeedDto(): SpeedDto {
    return SpeedDto(hover = hover, values = values.map { it.toSpeedValueDto() })
}

private fun SpeedValue.toSpeedValueDto(): SpeedValueDto {
    val measurementUnit = if (valueFormatted.contains(MeasurementUnitDto.METER.value)) {
        MeasurementUnitDto.METER
    } else {
        MeasurementUnitDto.FEET
    }
    val value = valueFormatted.split(" ").firstOrNull()?.toIntOrNull() ?: 0
    return SpeedValueDto(
        type = SpeedTypeDto.valueOf(type.name),
        measurementUnit = measurementUnit,
        value = value,
        valueFormatted = valueFormatted,
    )
}

private fun List<AbilityScore>.toAbilityScoreDtos(): List<AbilityScoreDto> {
    return map { AbilityScoreDto(type = AbilityScoreTypeDto.valueOf(it.type.name), value = it.value, modifier = it.modifier) }
}

private fun List<SavingThrow>.toSavingThrowDtos(): List<SavingThrowDto> {
    return map { SavingThrowDto(index = it.index, type = AbilityScoreTypeDto.valueOf(it.type.name), modifier = it.modifier) }
}

private fun List<Skill>.toSkillDtos(): List<SkillDto> {
    return map { SkillDto(index = it.index, modifier = it.modifier, name = it.name) }
}

private fun List<Damage>.toDamageDtos(): List<DamageDto> {
    return map { DamageDto(index = it.index, type = it.type.name, name = it.name) }
}

private fun List<Condition>.toConditionDtos(): List<ConditionDto> {
    return map { ConditionDto(index = it.index, type = it.type.name, name = it.name) }
}

private fun List<Action>.toActionDtos(): List<ActionDto> {
    return map { action ->
        ActionDto(
            name = action.abilityDescription.name,
            description = action.abilityDescription.description,
            attackBonus = action.attackBonus,
            damageDices = action.damageDices.toDamageDiceDtos(),
            damageDicesV2 = emptyList(),
            savingThrows = action.abilityDescription.savingThrows.toSavingThrowDtos(),
            conditions = action.abilityDescription.conditions.toConditionDtos(),
            spellsByGroup = action.spellsByGroup.toSpellUsageDtos(),
        )
    }
}

private fun List<DamageDice>.toDamageDiceDtos(): List<DamageDiceDto> {
    return map { DamageDiceDto(dice = it.dice, damage = DamageDto(index = it.damage.index, type = it.damage.type.name, name = it.damage.name)) }
}

private fun List<Spellcasting>.toSpellcastingDtos(): List<SpellcastingDto> {
    return map { spellcasting ->
        SpellcastingDto(
            desc = spellcasting.description,
            type = SpellcastingTypeDto.valueOf(spellcasting.type.name),
            spellsByGroup = spellcasting.usages.toSpellUsageDtos(),
        )
    }
}

private fun List<SpellUsage>.toSpellUsageDtos(): List<SpellUsageDto> {
    return map { usage ->
        SpellUsageDto(
            group = usage.group,
            spells = usage.spells.map { spell ->
                SpellPreviewDto(
                    index = spell.index,
                    name = spell.name,
                    level = spell.level,
                    school = SchoolOfMagicDto.valueOf(spell.school.name),
                )
            }
        )
    }
}
