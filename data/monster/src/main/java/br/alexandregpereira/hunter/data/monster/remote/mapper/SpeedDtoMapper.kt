/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster.remote.mapper

import br.alexandregpereira.hunter.data.monster.remote.model.SpeedDto
import br.alexandregpereira.hunter.data.monster.remote.model.SpeedValueDto
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue

internal fun SpeedDto.toDomain(): Speed {
    return Speed(hover = this.hover, values = this.values.toDomain())
}

internal fun List<SpeedValueDto>.toDomain(): List<SpeedValue> {
    return this.map {
        SpeedValue(
            type = SpeedType.valueOf(it.type.name),
            valueFormatted = it.valueFormatted
        )
    }
}