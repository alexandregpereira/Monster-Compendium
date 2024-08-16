package br.alexandregpereira.hunter.shareContent.domain.mapper

import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.ChallengeRating
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.SavingThrow
import br.alexandregpereira.hunter.domain.model.Skill
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellPreview
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellcastingType
import br.alexandregpereira.hunter.shareContent.domain.model.ShareAction
import br.alexandregpereira.hunter.shareContent.domain.model.ShareMonster
import br.alexandregpereira.hunter.shareContent.domain.model.ShareSpellcasting
import br.alexandregpereira.hunter.shareContent.domain.model.ShareType

internal fun ShareMonster.toMonster(): Monster {
    return Monster(
        index = index,
        name = name,
        type = MonsterType.valueOf(type),
        challengeRatingData = ChallengeRating(challengeRating),
        imageData = MonsterImageData(
            url = imageUrl,
            backgroundColor = Color(
                light = imageBackgroundColorLight,
                dark = imageBackgroundColorDark
            ),
            isHorizontal = isHorizontalImage
        ),
        subtype = subtype,
        group = group,
        subtitle = subtitle,
        size = size,
        alignment = alignment,
        stats = Stats(
            armorClass = armorClass,
            hitPoints = hitPoints,
            hitDice = hitDice
        ),
        senses = senses,
        languages = languages,
        sourceName = sourceName,
        speed = Speed(
            hover = hover,
            values = speedValues.map {
                SpeedValue(
                    type = SpeedType.valueOf(it.type),
                    valueFormatted = it.valueFormatted,
                    index = it.index
                )
            }
        ),
        abilityScores = abilityScores.map {
            AbilityScore(
                type = AbilityScoreType.valueOf(it.type),
                value = it.value,
                modifier = it.modifier
            )
        },
        savingThrows = savingThrows.map {
            SavingThrow(
                index = it.index,
                modifier = it.modifier,
                type = AbilityScoreType.valueOf(it.type)
            )
        },
        skills = skills.map {
            Skill(
                index = it.index,
                modifier = it.modifier,
                name = it.type
            )
        },
        damageVulnerabilities = damageVulnerabilities.map { it.toDamage() },
        damageResistances = damageResistances.map { it.toDamage() },
        damageImmunities = damageImmunities.map { it.toDamage() },
        conditionImmunities = conditionImmunities.map {
            Condition(
                index = it.index,
                type = ConditionType.valueOf(it.type),
                name = it.name,
            )
        },
        specialAbilities = specialAbilities.map { it.toAbilityDescription() },
        actions = actions.map { it.toAction() },
        legendaryActions = legendaryActions.map { it.toAction() },
        reactions = reactions.map { it.toAbilityDescription() },
        spellcastings = listOfNotNull(
            spellcaster?.toSpellcastings(SpellcastingType.SPELLCASTER),
            innateSpellcaster?.toSpellcastings(SpellcastingType.INNATE),
        ),
        lore = lore,
        status = MonsterStatus.Imported,
    )
}

private fun ShareType.toDamage(): Damage {
    return Damage(
        index = index,
        type = DamageType.valueOf(type),
        name = name,
    )
}

private fun ShareType.toAbilityDescription(): AbilityDescription {
    return AbilityDescription(
        index = index,
        description = type,
        name = name,
    )
}

private fun ShareAction.toAction(): Action {
    return Action(
        id = id,
        damageDices = damageDices.map {
            DamageDice(
                dice = it.dice,
                damage = it.damage.toDamage(),
            )
        },
        attackBonus = attackBonus,
        abilityDescription = abilityDescription.toAbilityDescription(),
    )
}

private fun ShareSpellcasting.toSpellcastings(type: SpellcastingType): Spellcasting {
    return Spellcasting(
        description = description,
        type = type,
        usages = usages.map {
            SpellUsage(
                group = it.group,
                spells = it.spells.map { spell ->
                    SpellPreview(
                        index = spell.index,
                        name = spell.name,
                        level = 0,
                        school = SchoolOfMagic.ABJURATION,
                    )
                },
            )
        }
    )
}
