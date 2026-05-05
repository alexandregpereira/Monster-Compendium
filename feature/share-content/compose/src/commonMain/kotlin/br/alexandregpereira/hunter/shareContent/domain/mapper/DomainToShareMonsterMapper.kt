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

package br.alexandregpereira.hunter.shareContent.domain.mapper

import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.SavingThrow
import br.alexandregpereira.hunter.domain.model.Skill
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellcastingType
import br.alexandregpereira.hunter.shareContent.domain.model.ShareAbilityDescription
import br.alexandregpereira.hunter.shareContent.domain.model.ShareAbilityScore
import br.alexandregpereira.hunter.shareContent.domain.model.ShareAction
import br.alexandregpereira.hunter.shareContent.domain.model.ShareDamageDice
import br.alexandregpereira.hunter.shareContent.domain.model.ShareMonster
import br.alexandregpereira.hunter.shareContent.domain.model.ShareMonsterSpell
import br.alexandregpereira.hunter.shareContent.domain.model.ShareSavingThrow
import br.alexandregpereira.hunter.shareContent.domain.model.ShareSpeedValue
import br.alexandregpereira.hunter.shareContent.domain.model.ShareSpellUsage
import br.alexandregpereira.hunter.shareContent.domain.model.ShareSpellcasting
import br.alexandregpereira.hunter.shareContent.domain.model.ShareType

internal fun Monster.toShareMonster(): ShareMonster {
    return ShareMonster(
        index = index,
        name = name,
        type = type.name,
        challengeRating = challengeRating,
        imageUrl = imageData.url,
        imageBackgroundColorLight = imageData.backgroundColor.light,
        imageBackgroundColorDark = imageData.backgroundColor.dark,
        isHorizontalImage = imageData.isHorizontal,
        subtype = subtype,
        group = group,
        subtitle = subtitle,
        size = size,
        alignment = alignment,
        armorClass = stats.armorClass,
        hitPoints = stats.hitPoints,
        hitDice = stats.hitDice,
        senses = senses,
        languages = languages,
        sourceName = sourceName,
        hover = speed.hover,
        speedValues = speed.values.map { it.toShareSpeedValue() },
        abilityScores = abilityScores.map { it.toShareAbilityScore() },
        savingThrows = savingThrows.map { it.toShareSavingThrow() },
        skills = skills.map { it.toShareSkill() },
        damageVulnerabilities = damageVulnerabilities.map { it.toShareType() },
        damageResistances = damageResistances.map { it.toShareType() },
        damageImmunities = damageImmunities.map { it.toShareType() },
        conditionImmunities = conditionImmunities.map {
            it.toShareCondition()
        },
        specialAbilities = specialAbilities.map { it.toShareAbilityDescription() },
        actions = actions.map { it.toShareAction() },
        legendaryActions = legendaryActions.map { it.toShareAction() },
        reactions = reactions.map { it.toShareAbilityDescription() },
        spellcaster = spellcastings.find { it.type == SpellcastingType.SPELLCASTER }
            ?.toShareSpellcasting(),
        innateSpellcaster = spellcastings.find { it.type == SpellcastingType.INNATE }
            ?.toShareSpellcasting(),
        lore = lore,
    )
}

private fun SpeedValue.toShareSpeedValue(): ShareSpeedValue = ShareSpeedValue(
    type = type.name,
    valueFormatted = valueFormatted,
    index = index
)

private fun AbilityScore.toShareAbilityScore(): ShareAbilityScore = ShareAbilityScore(
    type = type.name,
    value = value,
    modifier = modifier
)

private fun SavingThrow.toShareSavingThrow(): ShareSavingThrow = ShareSavingThrow(
    index = index,
    modifier = modifier,
    type = type.name
)

private fun Skill.toShareSkill(): ShareSavingThrow = ShareSavingThrow(
    index = index,
    modifier = modifier,
    type = name,
)

private fun Damage.toShareType(): ShareType = ShareType(
    index = index,
    type = type.name,
    name = name
)

private fun Action.toShareAction(): ShareAction = ShareAction(
    id = id,
    damageDices = damageDices.map { it.toShareDamageDice() },
    attackBonus = attackBonus,
    abilityDescription = abilityDescription.toShareAbilityDescription()
)

private fun AbilityDescription.toShareAbilityDescription(): ShareAbilityDescription {
    return ShareAbilityDescription(
        index = index,
        type = description,
        name = name,
        savingThrows = savingThrows.map { it.toShareSavingThrow() },
        conditions = conditions.map { it.toShareCondition() }
    )
}

private fun Condition.toShareCondition(): ShareType = ShareType(
    index = index,
    type = type.name,
    name = name,
)

private fun DamageDice.toShareDamageDice(): ShareDamageDice = ShareDamageDice(
    dice = dice,
    damage = damage.toShareType(),
)

private fun Spellcasting.toShareSpellcasting(): ShareSpellcasting = ShareSpellcasting(
    description = description,
    usages = usages.map { it.toShareSpellUsage() },
)

private fun SpellUsage.toShareSpellUsage(): ShareSpellUsage = ShareSpellUsage(
    group = group,
    spells = spells.map {
        ShareMonsterSpell(
            index = it.index,
            name = it.name,
        )
    },
)
