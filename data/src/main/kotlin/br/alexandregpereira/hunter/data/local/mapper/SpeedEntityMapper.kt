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

import br.alexandregpereira.hunter.data.local.entity.SpeedEntity
import br.alexandregpereira.hunter.data.local.entity.SpeedValueEntity
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue

internal fun SpeedEntity.toDomain(): Speed {
    return Speed(
        hover = this.hover,
        values = this.values.toObjFromJson<List<SpeedValueEntity>>().toDomain()
    )
}

internal fun List<SpeedValueEntity>.toDomain(): List<SpeedValue> {
    return this.map {
        SpeedValue(
            type = SpeedType.valueOf(it.type),
            valueFormatted = it.valueFormatted
        )
    }
}

internal fun Speed.toEntity(): SpeedEntity {
    return SpeedEntity(hover = this.hover, values = this.values.toEntity().toJsonFromObj())
}

internal fun List<SpeedValue>.toEntity(): List<SpeedValueEntity> {
    return this.map {
        SpeedValueEntity(
            type = it.type.name,
            valueFormatted = it.valueFormatted
        )
    }
}