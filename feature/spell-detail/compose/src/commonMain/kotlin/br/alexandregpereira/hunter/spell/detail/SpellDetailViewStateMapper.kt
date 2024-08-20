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

package br.alexandregpereira.hunter.spell.detail

import br.alexandregpereira.hunter.domain.spell.model.SavingThrowType
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.model.Spell

internal fun Spell.asState(strings: SpellDetailStrings): SpellState {
    val durationText = if (concentration) {
        "${strings.concentration}, ${duration.lowercase()}"
    } else {
        duration
    }
    return SpellState(
        index = index,
        name = name,
        subtitle = strings.formatSubTitle(level, school.name(strings)),
        castingTime = castingTime,
        components = components,
        duration = durationText,
        range = range,
        concentration = concentration,
        savingThrowType = savingThrowType?.name(strings),
        school = school,
        description = description,
        higherLevel = higherLevel
    )
}

private fun SchoolOfMagic.name(strings: SpellDetailStrings): String {
    return when (this) {
        SchoolOfMagic.ABJURATION -> strings.schoolAbjuration
        SchoolOfMagic.CONJURATION -> strings.schoolConjuration
        SchoolOfMagic.DIVINATION -> strings.schoolDivination
        SchoolOfMagic.ENCHANTMENT -> strings.schoolEnchantment
        SchoolOfMagic.EVOCATION -> strings.schoolEvocation
        SchoolOfMagic.ILLUSION -> strings.schoolIllusion
        SchoolOfMagic.NECROMANCY -> strings.schoolNecromancy
        SchoolOfMagic.TRANSMUTATION -> strings.schoolTransmutation
    }
}

private fun SavingThrowType.name(strings: SpellDetailStrings): String {
    return when (this) {
        SavingThrowType.STRENGTH -> strings.savingThrowStrength
        SavingThrowType.DEXTERITY -> strings.savingThrowDexterity
        SavingThrowType.CONSTITUTION -> strings.savingThrowConstitution
        SavingThrowType.INTELLIGENCE -> strings.savingThrowIntelligence
        SavingThrowType.WISDOM -> strings.savingThrowWisdom
        SavingThrowType.CHARISMA -> strings.savingThrowCharisma
    }
}
