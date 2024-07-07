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
        subtitle = strings.subtitle(level, school.name(strings)),
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
