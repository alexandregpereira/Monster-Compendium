/*
 * Copyright 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.monster.detail

import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.SavingThrow
import br.alexandregpereira.hunter.domain.model.Skill
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.domain.model.xpFormatted
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellcastingType

internal fun List<Monster>.asState(strings: MonsterDetailStrings): List<MonsterState> {
    return this.map { it.asState(strings) }
}

private fun Monster.asState(strings: MonsterDetailStrings): MonsterState {
    return MonsterState(
        index = index,
        name = name,
        imageState = imageData.asState(
            type = type,
            challengeRating = challengeRating,
            xp = xpFormatted(),
            contentDescription = name
        ),
        subtype = subtype,
        group = group,
        subtitle = subtitle,
        size = size,
        alignment = alignment,
        stats = StatsState(
            armorClass = stats.armorClass,
            hitPoints = stats.hitPoints,
            hitDice = stats.hitDice
        ),
        speed = SpeedState(
            hover = speed.hover,
            values = speed.values.map { it.asState() }
        ),
        abilityScores = abilityScores.map { it.asState(strings) },
        savingThrows = savingThrows.map { it.asSavingThrowState(strings) },
        skills = skills.map { it.asState() },
        damageVulnerabilities = damageVulnerabilities.map { it.asState() },
        damageResistances = damageResistances.map { it.asState() },
        damageImmunities = damageImmunities.map { it.asState() },
        conditionImmunities = conditionImmunities.map { it.asState() },
        senses = senses,
        languages = languages,
        specialAbilities = specialAbilities.map { it.asState() },
        actions = actions.map { it.asState() },
        legendaryActions = legendaryActions.map { it.asState() },
        reactions = reactions.map { it.asState() },
        spellcastings = spellcastings.map { it.asState(strings) },
        lore = lore?.run {
            val loreSize = 180
            val ellipse = if (length > loreSize) "..." else ""
            substring(0, loreSize.coerceAtMost(length)) + ellipse
        }.orEmpty()
    )
}

private fun MonsterImageData.asState(
    type: MonsterType,
    challengeRating: Float,
    xp: String,
    contentDescription: String,
): MonsterImageState {
    return MonsterImageState(
        url = url,
        type = MonsterType.valueOf(type.name),
        backgroundColor = ColorState(
            light = backgroundColor.light,
            dark = backgroundColor.dark,
        ),
        challengeRating = challengeRating,
        xp = xp,
        isHorizontal = isHorizontal,
        contentDescription = contentDescription,
    )
}

private fun SpeedValue.asState(): SpeedValueState {
    return SpeedValueState(
        type = type,
        valueFormatted = valueFormatted
    )
}

private fun AbilityScore.asState(strings: MonsterDetailStrings): AbilityScoreState {
    return AbilityScoreState(
        value = value,
        modifier = modifier,
        name = when(type) {
            AbilityScoreType.STRENGTH -> strings.savingThrowStrength
            AbilityScoreType.DEXTERITY -> strings.savingThrowDexterity
            AbilityScoreType.CONSTITUTION -> strings.savingThrowConstitution
            AbilityScoreType.INTELLIGENCE -> strings.savingThrowIntelligence
            AbilityScoreType.WISDOM -> strings.savingThrowWisdom
            AbilityScoreType.CHARISMA -> strings.savingThrowCharisma
        }
    )
}

private fun Skill.asState(): ProficiencyState {
    return ProficiencyState(
        index = index,
        modifier = modifier,
        name = name
    )
}

private fun SavingThrow.asSavingThrowState(strings: MonsterDetailStrings): ProficiencyState {
    return ProficiencyState(
        index = index,
        modifier = modifier,
        name = when (type) {
            AbilityScoreType.STRENGTH -> strings.savingThrowStrength
            AbilityScoreType.DEXTERITY -> strings.savingThrowDexterity
            AbilityScoreType.CONSTITUTION -> strings.savingThrowConstitution
            AbilityScoreType.INTELLIGENCE -> strings.savingThrowIntelligence
            AbilityScoreType.WISDOM -> strings.savingThrowWisdom
            AbilityScoreType.CHARISMA -> strings.savingThrowCharisma
        },
    )
}

private fun Damage.asState(): DamageState {
    return DamageState(
        index = index,
        type = type,
        name = name
    )
}

private fun Condition.asState(): ConditionState {
    return ConditionState(
        index = index,
        type = type,
        name = name
    )
}

private fun AbilityDescription.asState(): AbilityDescriptionState {
    return AbilityDescriptionState(
        name = name,
        description = description
    )
}

private fun Action.asState(): ActionState {
    return ActionState(
        damageDices = damageDices.map { it.asState() },
        attackBonus = attackBonus,
        abilityDescription = abilityDescription.asState()
    )
}

private fun DamageDice.asState(): DamageDiceState {
    return DamageDiceState(
        dice = dice,
        damage = damage.asState()
    )
}

private fun Spellcasting.asState(strings: MonsterDetailStrings): SpellcastingState {
    return SpellcastingState(
        name = when (type) {
            SpellcastingType.INNATE -> strings.innateSpellcasting
            SpellcastingType.SPELLCASTER -> strings.spellcasting
        },
        description = description,
        spellsByGroup = usages.associate { spellUsage ->
            spellUsage.group to spellUsage.spells.map { spell ->
                SpellPreviewState(
                    index = spell.index,
                    name = spell.name,
                    school = spell.school
                )
            }
        }
    )
}
