/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.local.mapper

import br.alexandregpereira.hunter.data.local.entity.ActionEntity
import br.alexandregpereira.hunter.data.local.entity.ActionWithDamageDicesEntity
import br.alexandregpereira.hunter.data.local.entity.DamageDiceEntity
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.DamageDice
import java.util.UUID

internal fun List<ActionWithDamageDicesEntity>.toDomain(): List<Action> {
    return this.map {
        Action(
            damageDices = it.damageDices.toDamageDiceDomain(),
            attackBonus = it.action.attackBonus,
            abilityDescription = AbilityDescription(
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
    return this.map {
        val actionId = it.abilityDescription.name + monsterIndex
        ActionWithDamageDicesEntity(
            damageDices = it.damageDices.toDamageDiceEntity(actionId),
            action = ActionEntity(
                id = actionId,
                attackBonus = it.attackBonus,
                description = it.abilityDescription.description,
                name = it.abilityDescription.name,
                monsterIndex = monsterIndex
            )
        )
    }
}

internal fun List<DamageDice>.toDamageDiceEntity(actionId: String): List<DamageDiceEntity> {
    return this.map {
        DamageDiceEntity(
            id = UUID.randomUUID().toString(),
            dice = it.dice,
            damage = it.damage.toEntity(actionId),
            actionId = actionId
        )
    }
}
