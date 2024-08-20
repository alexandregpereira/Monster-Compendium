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

package br.alexandregpereira.hunter.domain.spell.model

data class Spell(
    val index: String,
    val name: String,
    val level: Int,
    val castingTime: String,
    val components: String,
    val duration: String,
    val range: String,
    val ritual: Boolean,
    val concentration: Boolean,
    val savingThrowType: SavingThrowType?,
    val damageType: String?,
    val school: SchoolOfMagic,
    val description: String,
    val higherLevel: String?,
    val status: SpellStatus,
)

enum class SpellStatus {
    Original,
    Imported
}
