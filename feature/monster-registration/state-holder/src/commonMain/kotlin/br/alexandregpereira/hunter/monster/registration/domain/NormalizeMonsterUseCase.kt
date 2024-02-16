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

package br.alexandregpereira.hunter.monster.registration.domain

import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
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
    ).filterEmpties().createIndexes()
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
    val emptyAbilityDescription = AbilityDescription.create()
    val emptyAction = Action.create()

    return copy(
        speed = speed.copy(
            values = speed.values.filter { it.valueFormatted.isNotBlank() }
        ),
        conditionImmunities = conditionImmunities.filter { it.name.isNotBlank() },
        savingThrows = savingThrows.filter { it.modifier != 0 },
        skills = skills.filter { it.name.isNotBlank() },
        specialAbilities = specialAbilities.filter { it != emptyAbilityDescription },
        actions = actions.filterDamageDices().filter { it != emptyAction },
        reactions = reactions.filter { it != emptyAbilityDescription },
        legendaryActions = legendaryActions.filterDamageDices().filter { it != emptyAction },
        spellcastings = spellcastings.filterSpellUsages().filter { spellcasting ->
            spellcasting.usages.isNotEmpty()
        }
    )
}

private fun List<Action>.filterDamageDices(): List<Action> {
    return map { action ->
        action.copy(
            damageDices = action.damageDices.filter { it.dice.isBlank() }
        )
    }
}

private fun List<Spellcasting>.filterSpellUsages(): List<Spellcasting> {
    return map { spellcasting ->
        spellcasting.copy(
            usages = spellcasting.usages.filterSpells().filter { it.spells.isNotEmpty() }
        )
    }
}

private fun List<SpellUsage>.filterSpells(): List<SpellUsage> {
    return map { spellUsage ->
        spellUsage.copy(
            spells = spellUsage.spells.filter { it.name.isNotBlank() }
        )
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
