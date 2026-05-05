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

import br.alexandregpereira.hunter.domain.spell.model.SpellStatus

internal enum class SpellDetailOption { CLONE, EDIT, DELETE, RESET_TO_ORIGINAL }

internal data class SpellDetailOptionState(
    val option: SpellDetailOption,
    val label: String,
)

internal fun SpellStatus.toOptions(strings: SpellDetailStrings): List<SpellDetailOptionState> {
    val options = when (this) {
        SpellStatus.Original -> listOf(SpellDetailOption.CLONE, SpellDetailOption.EDIT)
        SpellStatus.Imported -> listOf(SpellDetailOption.CLONE, SpellDetailOption.EDIT, SpellDetailOption.DELETE)
        SpellStatus.Edited -> listOf(SpellDetailOption.CLONE, SpellDetailOption.EDIT, SpellDetailOption.RESET_TO_ORIGINAL)
        SpellStatus.Cloned, SpellStatus.Created -> listOf(SpellDetailOption.CLONE, SpellDetailOption.EDIT, SpellDetailOption.DELETE)
    }
    return options.map { SpellDetailOptionState(it, it.toLabel(strings)) }
}

private fun SpellDetailOption.toLabel(strings: SpellDetailStrings): String = when (this) {
    SpellDetailOption.CLONE -> strings.clone
    SpellDetailOption.EDIT -> strings.edit
    SpellDetailOption.DELETE -> strings.delete
    SpellDetailOption.RESET_TO_ORIGINAL -> strings.resetToOriginal
}
