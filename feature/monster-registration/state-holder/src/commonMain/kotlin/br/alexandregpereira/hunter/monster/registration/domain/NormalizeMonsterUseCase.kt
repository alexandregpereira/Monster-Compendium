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

package br.alexandregpereira.hunter.monster.registration.domain

import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.ChallengeRating
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.getChallengeRatingFormatted
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal fun interface NormalizeMonsterUseCase {
    operator fun invoke(monster: Monster): Flow<Monster>
}

internal fun NormalizeMonsterUseCase(): NormalizeMonsterUseCase =
    NormalizeMonsterUseCase { monster ->
        flowOf(monster)
            .map { it.changeAbilityScoresModifier() }
    }

private fun Monster.changeAbilityScoresModifier(): Monster {
    val monster = this
    return monster.copy(
        abilityScores = monster.abilityScores.map { abilityScore ->
            val newValue = abilityScore.value.coerceAtLeast(2).coerceAtMost(99)
            abilityScore.copy(
                value = newValue,
                modifier = when {
                    newValue < 2 -> -5
                    newValue < 4 -> -4
                    newValue < 6 -> -3
                    newValue < 8 -> -2
                    newValue < 10 -> -1
                    newValue < 12 -> 0
                    newValue < 14 -> 1
                    newValue < 16 -> 2
                    newValue < 18 -> 3
                    newValue < 20 -> 4
                    newValue < 22 -> 5
                    newValue < 24 -> 6
                    newValue < 26 -> 7
                    newValue < 28 -> 8
                    newValue < 30 -> 9
                    else -> 10
                }
            )
        },
        imageData = monster.imageData.copy(
            backgroundColor = monster.imageData.backgroundColor.normalizeColor(),
        ),
        challengeRatingData = monster.challengeRatingData.normalizeChallengeRating()
    ).filterEmpties().createIndexes()
}

private fun ChallengeRating.normalizeChallengeRating(): ChallengeRating {
    val newValue = (this.valueInString.toFloatOrNull() ?: 0f).coerceAtMost(50f)
    return this.copy(
        value = newValue,
        formatted = newValue.getChallengeRatingFormatted()
    )
}

private fun Color.normalizeColor(): Color {
    val newColorLight = this.light.normalizeColorString()
    val newColorDark = this.dark.normalizeColorString()
    return this.copy(
        light = newColorLight,
        dark = newColorDark,
    )
}

private fun String.normalizeColorString(): String {
    return this.takeIf { it.isNotBlank() }
        ?.replace("#", "")?.let { "#$it" }?.uppercase().orEmpty()
}

internal fun Monster.filterEmpties(): Monster {
    return copy(
        speed = speed.copy(
            values = speed.values.filter { it.valueFormatted.isNotBlank() }
        ),
        conditionImmunities = conditionImmunities.filter { it.name.isNotBlank() },
        savingThrows = savingThrows.filter { it.modifier != 0 },
        skills = skills.filter { it.name.isNotBlank() },
        specialAbilities = specialAbilities.filter { it.isEmpty().not() },
        actions = actions.filterDamageDices { it.abilityDescription.isEmpty().not() },
        reactions = reactions.filter { it.isEmpty().not() },
        legendaryActions = legendaryActions.filterDamageDices { it.abilityDescription.isEmpty().not() },
        spellcastings = spellcastings.filterSpellUsages { it.usages.isNotEmpty() }
    )
}

private fun AbilityDescription.isEmpty(): Boolean {
    return name.isBlank() && description.isBlank()
}

private fun List<Action>.filterDamageDices(predicate: (Action) -> Boolean = { true }): List<Action> {
    return mapNotNull { action ->
        val filteredAction = action.copy(
            damageDices = action.damageDices.filter { it.dice.isNotBlank() }
        )
        if (predicate(filteredAction)) filteredAction else null
    }
}

private fun List<Spellcasting>.filterSpellUsages(predicate: (Spellcasting) -> Boolean = { true }): List<Spellcasting> {
    return mapNotNull { spellcasting ->
        val filteredSpellcasting = spellcasting.copy(
            usages = spellcasting.usages.filterSpells { it.spells.isNotEmpty() }
        )
        if (predicate(filteredSpellcasting)) filteredSpellcasting else null
    }
}

private fun List<SpellUsage>.filterSpells(predicate: (SpellUsage) -> Boolean = { true }): List<SpellUsage> {
    return mapNotNull { spellUsage ->
        val filteredSpellUsage = spellUsage.copy(
            spells = spellUsage.spells.filter { it.name.isNotBlank() }
        )
        if (predicate(filteredSpellUsage)) filteredSpellUsage else null
    }
}

private fun Monster.createIndexes(): Monster {
    return copy(
        savingThrows = savingThrows.map { savingThrow ->
            savingThrow.copy(index = "saving-throw-${savingThrow.type.name}".normalizeIndex())
        },
        skills = skills.map { skill ->
            skill.copy(index = "skill-${skill.name}".normalizeIndex())
        },
        conditionImmunities = conditionImmunities.map { conditionImmunity ->
            conditionImmunity.copy(index = conditionImmunity.type.name.normalizeIndex())
        },
        damageVulnerabilities = damageVulnerabilities.map { damageVulnerability ->
            damageVulnerability.copy(index = damageVulnerability.type.name.normalizeIndex())
        },
        damageResistances = damageResistances.map { damageResistance ->
            damageResistance.copy(index = damageResistance.type.name.normalizeIndex())
        },
        damageImmunities = damageImmunities.map { damageImmunity ->
            damageImmunity.copy(index = damageImmunity.type.name.normalizeIndex())
        },
    )
}

private fun String.normalizeIndex(): String {
    return lowercase().replace(" ", "-")
}
