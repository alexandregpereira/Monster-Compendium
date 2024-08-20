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

import br.alexandregpereira.hunter.data.monster.local.entity.SpeedEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpeedValueEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpeedWithValuesEntity
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue

internal fun SpeedWithValuesEntity.toDomain(): Speed {
    return Speed(
        hover = this.speed.hover,
        values = this.values.toDomain()
    )
}

internal fun List<SpeedValueEntity>.toDomain(): List<SpeedValue> {
    return this.mapIndexed { index, speedValueEntity ->
        SpeedValue(
            type = SpeedType.valueOf(speedValueEntity.type),
            valueFormatted = speedValueEntity.valueFormatted,
            index = "${speedValueEntity.speedId}-${speedValueEntity.type}-$index"
        )
    }
}

internal fun Speed.toEntity(monsterIndex: String): SpeedWithValuesEntity {
    val speedId = this.values.joinToString(separator = "-") { it.type.name } + monsterIndex
    return SpeedWithValuesEntity(
        speed = SpeedEntity(
            id = speedId,
            hover = this.hover,
            monsterIndex = monsterIndex
        ),
        values = this.values.toEntity(speedId)
    )
}

internal fun List<SpeedValue>.toEntity(speedId: String): List<SpeedValueEntity> {
    return this.map {
        SpeedValueEntity(
            type = it.type.name,
            valueFormatted = it.valueFormatted,
            speedId = speedId
        )
    }
}