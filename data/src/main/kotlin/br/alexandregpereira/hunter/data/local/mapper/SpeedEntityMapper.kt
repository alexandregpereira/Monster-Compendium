/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.local.mapper

import br.alexandregpereira.hunter.data.local.entity.SpeedEntity
import br.alexandregpereira.hunter.data.local.entity.SpeedValueEntity
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
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
            measurementUnit = MeasurementUnit.valueOf(it.measurementUnit),
            value = it.value,
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
            measurementUnit = it.measurementUnit.name,
            value = it.value,
            valueFormatted = it.valueFormatted
        )
    }
}