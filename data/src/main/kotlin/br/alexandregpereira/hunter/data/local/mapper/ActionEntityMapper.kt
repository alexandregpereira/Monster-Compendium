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

import br.alexandregpereira.hunter.data.local.entity.ActionEntity
import br.alexandregpereira.hunter.data.local.entity.DamageDiceEntity
import br.alexandregpereira.hunter.data.local.entity.ValueEntity
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.DamageDice

internal fun List<ActionEntity>.toDomain(): List<Action> {
    return this.map {
        Action(
            damageDices = it.damageDices.toObjFromJson<List<DamageDiceEntity>>()
                .toDamageDiceDomain(),
            attackBonus = it.attackBonus,
            description = it.description,
            name = it.name
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
            description = it.description,
            name = it.name
        )
    }
}

internal fun List<DamageDice>.toDamageDiceEntity(): List<DamageDiceEntity> {
    return this.map {
        DamageDiceEntity(dice = it.dice, damage = it.damage.toEntity().toJsonFromObj())
    }
}
