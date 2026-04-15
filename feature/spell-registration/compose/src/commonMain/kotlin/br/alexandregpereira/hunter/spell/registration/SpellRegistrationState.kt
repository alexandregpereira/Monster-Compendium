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

package br.alexandregpereira.hunter.spell.registration

import br.alexandregpereira.hunter.domain.spell.model.SavingThrowType
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.model.SpellStatus

data class SpellRegistrationState(
    val isOpen: Boolean = false,
    val isLoading: Boolean = false,
    val isSaveEnabled: Boolean = false,
    val isEditing: Boolean = false,
    val spell: SpellFormState = SpellFormState(),
    val strings: SpellRegistrationStrings = SpellRegistrationStrings(),
)

data class SpellFormState(
    val index: String = "",
    val name: String = "",
    val level: String = "",
    val castingTime: String = "",
    val components: String = "",
    val duration: String = "",
    val range: String = "",
    val ritual: Boolean = false,
    val concentration: Boolean = false,
    val savingThrowType: SavingThrowType? = null,
    val damageType: String = "",
    val school: SchoolOfMagic = SchoolOfMagic.ABJURATION,
    val description: String = "",
    val higherLevel: String = "",
    val originalStatus: SpellStatus = SpellStatus.Original,
)
