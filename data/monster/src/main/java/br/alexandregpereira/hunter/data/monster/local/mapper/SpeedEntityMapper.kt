/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.monster.local.mapper

import br.alexandregpereira.hunter.data.monster.local.entity.SpeedEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpeedValueEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpeedWithValuesEntity
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue
import java.util.UUID

internal fun SpeedWithValuesEntity.toDomain(): Speed {
    return Speed(
        hover = this.speed.hover,
        values = this.values.toDomain()
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

internal fun Speed.toEntity(monsterIndex: String): SpeedWithValuesEntity {
    val speedId = UUID.randomUUID().toString()
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