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

package br.alexandregpereira.hunter.data.monster.remote.mapper

import br.alexandregpereira.hunter.data.monster.remote.model.ActionDto
import br.alexandregpereira.hunter.data.monster.remote.model.DamageDiceDto
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.uuid.generateUUID

internal fun List<ActionDto>.toDomain(): List<Action> {
    return this.map { action ->
        val uuid = generateUUID()
        Action(
            id = "action-$uuid",
            damageDices = action.damageDices.toDamageDiceDomain(),
            attackBonus = action.attackBonus,
            abilityDescription = AbilityDescription(
                name = action.name,
                description = action.description,
                index = "action-$uuid"
            )
        )
    }
}

internal fun List<DamageDiceDto>.toDamageDiceDomain(): List<DamageDice> {
    return this.map {
        DamageDice(
            dice = it.dice,
            damage = it.damage.asDomain()
        )
    }
}
