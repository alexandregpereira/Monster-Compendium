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

package br.alexandregpereira.hunter.data.monster.local.mapper

import br.alexandregpereira.hunter.data.monster.local.entity.ActionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ActionWithDamageDicesEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageDiceEntity
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.uuid.generateUUID

internal fun List<ActionWithDamageDicesEntity>.toDomain(): List<Action> {
    return this.map {
        Action(
            id = it.action.id,
            damageDices = it.damageDices.toDamageDiceDomain(),
            attackBonus = it.action.attackBonus,
            abilityDescription = AbilityDescription(
                index = it.action.id,
                name = it.action.name,
                description = it.action.description
            )
        )
    }
}

internal fun List<DamageDiceEntity>.toDamageDiceDomain(): List<DamageDice> {
    return this.map {
        DamageDice(
            dice = it.dice,
            damage = it.damage.toDamageDomain()
        )
    }
}

internal fun List<Action>.toEntity(monsterIndex: String): List<ActionWithDamageDicesEntity> {
    return this.map { action ->
        val actionId = action.id.takeUnless { it.isBlank() } ?: "action-${generateUUID()}"
        ActionWithDamageDicesEntity(
            damageDices = action.damageDices.toDamageDiceEntity(actionId, monsterIndex),
            action = ActionEntity(
                id = actionId,
                attackBonus = action.attackBonus,
                description = action.abilityDescription.description,
                name = action.abilityDescription.name,
                monsterIndex = monsterIndex
            )
        )
    }
}

internal fun List<DamageDice>.toDamageDiceEntity(
    actionId: String,
    monsterIndex: String
): List<DamageDiceEntity> {
    return this.map {
        DamageDiceEntity(
            id = it.dice + actionId + monsterIndex,
            dice = it.dice,
            damage = it.damage.toEntity(actionId),
            actionId = actionId
        )
    }
}
