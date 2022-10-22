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

package br.alexandregpereira.hunter.detail

import br.alexandregpereira.hunter.detail.ui.AbilityDescriptionState
import br.alexandregpereira.hunter.detail.ui.AbilityScoreState
import br.alexandregpereira.hunter.detail.ui.ActionState
import br.alexandregpereira.hunter.detail.ui.ColorState
import br.alexandregpereira.hunter.detail.ui.ConditionState
import br.alexandregpereira.hunter.detail.ui.ConditionTypeState
import br.alexandregpereira.hunter.detail.ui.DamageDiceState
import br.alexandregpereira.hunter.detail.ui.DamageState
import br.alexandregpereira.hunter.detail.ui.DamageTypeState
import br.alexandregpereira.hunter.detail.ui.MonsterImageState
import br.alexandregpereira.hunter.detail.ui.MonsterState
import br.alexandregpereira.hunter.detail.ui.MonsterTypeState
import br.alexandregpereira.hunter.detail.ui.ProficiencyState
import br.alexandregpereira.hunter.detail.ui.SpeedState
import br.alexandregpereira.hunter.detail.ui.SpeedTypeState
import br.alexandregpereira.hunter.detail.ui.SpeedValueState
import br.alexandregpereira.hunter.detail.ui.SpellPreviewState
import br.alexandregpereira.hunter.detail.ui.SpellcastingState
import br.alexandregpereira.hunter.detail.ui.SpellcastingTypeState
import br.alexandregpereira.hunter.detail.ui.StatsState
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Proficiency
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState

internal fun List<Monster>.asState(): List<MonsterState> {
    return this.map { it.asState() }
}

private fun Monster.asState(): MonsterState {
    return MonsterState(
        index = index,
        name = name,
        imageState = imageData.asState(type, challengeRating, contentDescription = name),
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
        abilityScores = abilityScores.map { it.asState() },
        savingThrows = savingThrows.map { it.asState() },
        skills = skills.map { it.asState() },
        damageVulnerabilities = damageVulnerabilities.map { it.asState() },
        damageResistances = damageResistances.map { it.asState() },
        damageImmunities = damageImmunities.map { it.asState() },
        conditionImmunities = conditionImmunities.map { it.asState() },
        senses = senses,
        languages = languages,
        specialAbilities = specialAbilities.map { it.asState() },
        actions = actions.map { it.asState() },
        reactions = reactions.map { it.asState() },
        spellcastings = spellcastings.map { it.asState() }
    )
}

private fun MonsterImageData.asState(
    type: MonsterType,
    challengeRating: Float,
    contentDescription: String,
): MonsterImageState {
    return MonsterImageState(
        url = url,
        type = MonsterTypeState.valueOf(type.name),
        backgroundColor = ColorState(
            light = backgroundColor.light,
            dark = backgroundColor.dark,
        ),
        challengeRating = challengeRating,
        isHorizontal = isHorizontal,
        contentDescription = contentDescription,
    )
}

private fun SpeedValue.asState(): SpeedValueState {
    return SpeedValueState(
        type = SpeedTypeState.valueOf(type.name),
        valueFormatted = valueFormatted
    )
}

private fun AbilityScore.asState(): AbilityScoreState {
    return AbilityScoreState(
        name = type.name,
        value = value,
        modifier = modifier
    )
}

private fun Proficiency.asState(): ProficiencyState {
    return ProficiencyState(
        index = index,
        modifier = modifier,
        name = name
    )
}

private fun Damage.asState(): DamageState {
    return DamageState(
        index = index,
        type = DamageTypeState.valueOf(type.name),
        name = name
    )
}

private fun Condition.asState(): ConditionState {
    return ConditionState(
        index = index,
        type = ConditionTypeState.valueOf(type.name),
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

private fun Spellcasting.asState(): SpellcastingState {
    return SpellcastingState(
        type = SpellcastingTypeState.valueOf(type.name),
        description = description,
        spellsByGroup = usages.associate { spellUsage ->
            spellUsage.group to spellUsage.spells.map { spell ->
                SpellPreviewState(
                    index = spell.index,
                    name = spell.name,
                    school = SchoolOfMagicState.valueOf(spell.school.name)
                )
            }
        }
    )
}
