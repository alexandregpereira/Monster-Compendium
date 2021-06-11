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
import br.alexandregpereira.hunter.data.local.entity.DamageDiceEntity
import br.alexandregpereira.hunter.data.local.entity.ValueEntity
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.DamageDice

internal fun List<ActionEntity>.toDomain(): List<Action> {
    return this.map {
        Action(
            damageDices = it.damageDices.toObjFromJson<List<DamageDiceEntity>>()
                .toDamageDiceDomain(),
            attackBonus = it.attackBonus,
            abilityDescription = AbilityDescription(name = it.name, description = it.description)
        )
    }
}

internal fun List<DamageDiceEntity>.toDamageDiceDomain(): List<DamageDice> {
    return this.map {
        DamageDice(
            dice = it.dice,
            damage = it.damage.toObjFromJson<ValueEntity>().toDamageDomain()
        )
    }
}

internal fun List<Action>.toEntity(): List<ActionEntity> {
    return this.map {
        ActionEntity(
            damageDices = it.damageDices.toDamageDiceEntity().toJsonFromObj(),
            attackBonus = it.attackBonus,
            description = it.abilityDescription.description,
            name = it.abilityDescription.name
        )
    }
}

internal fun List<DamageDice>.toDamageDiceEntity(): List<DamageDiceEntity> {
    return this.map {
        DamageDiceEntity(dice = it.dice, damage = it.damage.toEntity().toJsonFromObj())
    }
}
