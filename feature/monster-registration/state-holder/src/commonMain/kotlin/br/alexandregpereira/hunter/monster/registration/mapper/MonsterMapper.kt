package br.alexandregpereira.hunter.monster.registration.mapper

import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.SavingThrow
import br.alexandregpereira.hunter.domain.model.Skill
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellPreview
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellcastingType
import br.alexandregpereira.hunter.monster.registration.AbilityDescriptionState
import br.alexandregpereira.hunter.monster.registration.ActionState
import br.alexandregpereira.hunter.monster.registration.ConditionState
import br.alexandregpereira.hunter.monster.registration.DamageDiceState
import br.alexandregpereira.hunter.monster.registration.DamageState
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationStrings
import br.alexandregpereira.hunter.monster.registration.MonsterState
import br.alexandregpereira.hunter.monster.registration.SavingThrowState
import br.alexandregpereira.hunter.monster.registration.SkillState
import br.alexandregpereira.hunter.monster.registration.SpellPreviewState
import br.alexandregpereira.hunter.monster.registration.SpellcastingState
import br.alexandregpereira.hunter.monster.registration.SpellsByGroupState
import br.alexandregpereira.hunter.uuid.generateUUID

internal fun Monster.editBy(
    state: MonsterState,
    strings: MonsterRegistrationStrings,
): Monster {
    val monster = this
    val spellsMap = mutableMapOf<String, SpellPreview>()
    monster.spellcastings.asSequence()
        .map { it.usages }
        .flatten()
        .map { it.spells }
        .flatten()
        .toSet()
        .forEach {
            spellsMap[it.index] = it
        }

    return monster.copy(
        name = state.info.name,
        subtitle = state.info.subtitle,
        group = state.info.group.takeUnless { it.isBlank() },
        challengeRatingData = monster.challengeRatingData.copy(
            valueInString = state.info.challengeRating,
        ),
        imageData = monster.imageData.copy(
            url = state.info.imageUrl,
            backgroundColor = monster.imageData.backgroundColor.copy(
                light = state.info.backgroundColorLight,
                dark = state.info.backgroundColorDark
            ),
            isHorizontal = state.info.isImageHorizontal,
        ),
        type = MonsterType.entries[state.info.typeIndex],
        stats = monster.stats.copy(
            armorClass = state.stats.armorClass,
            hitPoints = state.stats.hitPoints,
            hitDice = state.stats.hitDice
        ),
        speed = monster.speed.copy(
            values = state.speedValues.map {
                SpeedValue(
                    index = it.key,
                    type = SpeedType.entries[it.typeIndex],
                    valueFormatted = it.value
                )
            }
        ),
        abilityScores = monster.abilityScores.mapIndexed { index, abilityScore ->
            abilityScore.copy(
                value = state.abilityScores[index].value,
            )
        },
        savingThrows = state.savingThrows.map { it.asDomain() },
        skills = state.skills.map { it.asDomain() },
        damageVulnerabilities = state.damageVulnerabilities.map { it.asDomain(strings) },
        damageResistances = state.damageResistances.map { it.asDomain(strings) },
        damageImmunities = state.damageImmunities.map { it.asDomain(strings) },
        conditionImmunities = state.conditionImmunities.map { it.asDomain() },
        senses = state.senses,
        languages = state.languages,
        specialAbilities = state.specialAbilities.map { it.asDomain() },
        actions = state.actions.map { it.asDomain(strings) },
        legendaryActions = state.legendaryActions.map { it.asDomain(strings) },
        reactions = state.reactions.map { it.asDomain() },
        spellcastings = state.spellcastings.map { it.asDomain(spellsMap) },
    )
}

private fun SavingThrowState.asDomain(): SavingThrow {
    return SavingThrow(
        index = key,
        type = AbilityScoreType.entries[selectedIndex],
        modifier = modifier,
    )
}

private fun SkillState.asDomain(): Skill {
    return Skill(
        index = key,
        name = name,
        modifier = modifier,
    )
}

private fun DamageState.asDomain(strings: MonsterRegistrationStrings): Damage {
    return Damage(
        index = key,
        type = DamageType.entries[selectedIndex],
        name = otherName
            ?: typeName.takeUnless { it.isBlank() }
            ?: DamageType.entries[selectedIndex].name(strings),
    )
}

private fun DamageDiceState.asDomain(strings: MonsterRegistrationStrings): DamageDice {
    return DamageDice(
        index = key,
        dice = dice,
        damage = damage.asDomain(strings),
    )
}

private fun ConditionState.asDomain(): Condition {
    return Condition(
        index = key,
        name = name,
        type = ConditionType.entries[selectedIndex],
    )
}

private fun AbilityDescriptionState.asDomain(): AbilityDescription {
    return AbilityDescription(
        index = key,
        name = name,
        description = description,
    )
}

private fun ActionState.asDomain(strings: MonsterRegistrationStrings): Action {
    return Action(
        id = key,
        damageDices = damageDices.map { it.asDomain(strings) },
        attackBonus = attackBonus,
        abilityDescription = abilityDescription.asDomain(),
    )
}

private fun SpellcastingState.asDomain(spellsMap: Map<String, SpellPreview>): Spellcasting {
    return Spellcasting(
        index = key,
        description = description,
        type = SpellcastingType.entries[selectedIndex],
        usages = spellsByGroup.map { it.asDomain(spellsMap) },
    )
}

private fun SpellsByGroupState.asDomain(spellsMap: Map<String, SpellPreview>): SpellUsage {
    return SpellUsage(
        index = key,
        group = group,
        spells = spells.map { it.asDomain(spellsMap) },
    )
}

private fun SpellPreviewState.asDomain(spellsMap: Map<String, SpellPreview>): SpellPreview {
    return spellsMap[index] ?: SpellPreview(
        index = generateUUID(),
        name = name,
        level = 0,
        school = SchoolOfMagic.ABJURATION,
    )
}
