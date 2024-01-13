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

import br.alexandregpereira.hunter.data.monster.local.entity.LegendaryActionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.LegendaryActionWithDamageDicesEntity
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action

internal fun List<LegendaryActionWithDamageDicesEntity>.toDomain(): List<Action> {
    return this.map {
        Action(
            id = it.action.id,
            damageDices = it.damageDices.toDamageDiceDomain(),
            attackBonus = it.action.attackBonus,
            abilityDescription = AbilityDescription(
                name = it.action.name,
                description = it.action.description
            )
        )
    }
}

internal fun List<Action>.toLegendaryActionEntity(
    monsterIndex: String
): List<LegendaryActionWithDamageDicesEntity> {
    return this.map {
        LegendaryActionWithDamageDicesEntity(
            damageDices = it.damageDices.toDamageDiceEntity(it.id, monsterIndex),
            action = LegendaryActionEntity(
                id = it.id,
                attackBonus = it.attackBonus,
                description = it.abilityDescription.description,
                name = it.abilityDescription.name,
                monsterIndex = monsterIndex
            )
        )
    }
}
