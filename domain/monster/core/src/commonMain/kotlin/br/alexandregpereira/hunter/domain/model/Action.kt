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

package br.alexandregpereira.hunter.domain.model

import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage
import br.alexandregpereira.hunter.uuid.generateUUID

data class Action(
    val id: String,
    val damageDices: List<DamageDice>,
    val attackBonus: Int?,
    val abilityDescription: AbilityDescription,
    val spellsByGroup: List<SpellUsage> = emptyList(),
)

data class DamageDice(
    val dice: String,
    val damage: Damage,
    val index: String,
) {
    companion object {
        fun create(
            dice: String,
            damage: Damage,
        ) = DamageDice(
            index = "damageDice-${generateUUID()}",
            dice = dice,
            damage = damage,
        )
    }
}
