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

package br.alexandregpereira.hunter.data.monster.local.mapper

import br.alexandregpereira.hunter.data.monster.local.entity.ConditionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ValueEntity
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.ConditionType

internal fun List<ConditionEntity>.toConditionDomain(): List<Condition> {
    return this.map {
        Condition(
            index = it.value.index,
            type = ConditionType.valueOf(it.value.type),
            name = it.value.name
        )
    }
}

internal fun List<Condition>.toEntity(monsterIndex: String): List<ConditionEntity> {
    return this.map {
        ConditionEntity(
            value = ValueEntity(
                index = it.index,
                type = it.type.name,
                name = it.name,
                monsterIndex = monsterIndex
            )
        )
    }
}
