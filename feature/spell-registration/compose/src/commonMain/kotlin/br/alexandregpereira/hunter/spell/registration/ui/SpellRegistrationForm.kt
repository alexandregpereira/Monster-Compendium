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

package br.alexandregpereira.hunter.spell.registration.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.spell.model.SavingThrowType
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.spell.registration.SpellFormState
import br.alexandregpereira.hunter.spell.registration.SpellRegistrationStrings
import br.alexandregpereira.hunter.ui.compose.AppKeyboardType
import br.alexandregpereira.hunter.ui.compose.AppSwitch
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Composable
internal fun SpellRegistrationForm(
    spell: SpellFormState,
    isEditing: Boolean,
    strings: SpellRegistrationStrings,
    modifier: Modifier = Modifier,
    onSpellChanged: (SpellFormState) -> Unit = {},
) {
    val title = if (isEditing) strings.editSpell else strings.addSpell
    val schoolOptions = SchoolOfMagic.entries.map { it.toLabel(strings) }
    val savingThrowOptions = listOf(strings.none) + SavingThrowType.entries.map { it.toLabel(strings) }

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        item {
            Spacer(Modifier.height(48.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        }

        item {
            AppTextField(
                text = spell.name,
                label = strings.name,
                onValueChange = { onSpellChanged(spell.copy(name = it)) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        item {
            AppTextField(
                text = spell.level,
                label = strings.level,
                keyboardType = AppKeyboardType.NUMBER,
                onValueChange = { onSpellChanged(spell.copy(level = it)) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        item {
            PickerField(
                label = strings.school,
                options = schoolOptions,
                value = spell.school.toLabel(strings),
                onValueChange = { index ->
                    onSpellChanged(spell.copy(school = SchoolOfMagic.entries[index]))
                },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        item {
            AppTextField(
                text = spell.castingTime,
                label = strings.castingTime,
                onValueChange = { onSpellChanged(spell.copy(castingTime = it)) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        item {
            AppTextField(
                text = spell.components,
                label = strings.components,
                onValueChange = { onSpellChanged(spell.copy(components = it)) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        item {
            AppTextField(
                text = spell.duration,
                label = strings.duration,
                onValueChange = { onSpellChanged(spell.copy(duration = it)) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        item {
            AppTextField(
                text = spell.range,
                label = strings.range,
                onValueChange = { onSpellChanged(spell.copy(range = it)) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        item {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                AppSwitch(
                    checked = spell.ritual,
                    label = strings.ritual,
                    onCheckedChange = { onSpellChanged(spell.copy(ritual = it)) },
                )
                Spacer(Modifier.height(8.dp))
                AppSwitch(
                    checked = spell.concentration,
                    label = strings.concentration,
                    onCheckedChange = { onSpellChanged(spell.copy(concentration = it)) },
                )
            }
        }

        item {
            PickerField(
                label = strings.savingThrowType,
                options = savingThrowOptions,
                value = spell.savingThrowType?.toLabel(strings) ?: strings.none,
                onValueChange = { index ->
                    val savingThrow = if (index == 0) null else SavingThrowType.entries[index - 1]
                    onSpellChanged(spell.copy(savingThrowType = savingThrow))
                },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        item {
            AppTextField(
                text = spell.damageType,
                label = strings.damageType,
                onValueChange = { onSpellChanged(spell.copy(damageType = it)) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        item {
            AppTextField(
                text = spell.description,
                label = strings.description,
                multiline = true,
                onValueChange = { onSpellChanged(spell.copy(description = it)) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        item {
            AppTextField(
                text = spell.higherLevel,
                label = strings.higherLevel,
                multiline = true,
                onValueChange = { onSpellChanged(spell.copy(higherLevel = it)) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}

private fun SchoolOfMagic.toLabel(strings: SpellRegistrationStrings): String = when (this) {
    SchoolOfMagic.ABJURATION -> strings.schoolAbjuration
    SchoolOfMagic.CONJURATION -> strings.schoolConjuration
    SchoolOfMagic.DIVINATION -> strings.schoolDivination
    SchoolOfMagic.ENCHANTMENT -> strings.schoolEnchantment
    SchoolOfMagic.EVOCATION -> strings.schoolEvocation
    SchoolOfMagic.ILLUSION -> strings.schoolIllusion
    SchoolOfMagic.NECROMANCY -> strings.schoolNecromancy
    SchoolOfMagic.TRANSMUTATION -> strings.schoolTransmutation
}

private fun SavingThrowType.toLabel(strings: SpellRegistrationStrings): String = when (this) {
    SavingThrowType.STRENGTH -> strings.savingThrowStrength
    SavingThrowType.DEXTERITY -> strings.savingThrowDexterity
    SavingThrowType.CONSTITUTION -> strings.savingThrowConstitution
    SavingThrowType.INTELLIGENCE -> strings.savingThrowIntelligence
    SavingThrowType.WISDOM -> strings.savingThrowWisdom
    SavingThrowType.CHARISMA -> strings.savingThrowCharisma
}
