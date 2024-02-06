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

import br.alexandregpereira.hunter.data.monster.local.entity.ActionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ActionWithDamageDicesEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageDiceEntity
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.uuid.generateUUID

internal fun List<ActionWithDamageDicesEntity>.toDomain(): List<Action> {
    return this.map {
        Action(
            id = it.action.id,
            damageDices = it.damageDices.toDamageDiceDomain(),
            attackBonus = it.action.attackBonus,
            abilityDescription = AbilityDescription(
                index = it.action.id,
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
    return this.map { action ->
        val actionId = action.id.takeUnless { it.isBlank() } ?: "action-${generateUUID()}"
        ActionWithDamageDicesEntity(
            damageDices = action.damageDices.toDamageDiceEntity(actionId, monsterIndex),
            action = ActionEntity(
                id = actionId,
                attackBonus = action.attackBonus,
                description = action.abilityDescription.description,
                name = action.abilityDescription.name,
                monsterIndex = monsterIndex
            )
        )
    }
}

internal fun List<DamageDice>.toDamageDiceEntity(
    actionId: String,
    monsterIndex: String
): List<DamageDiceEntity> {
    return this.map {
        DamageDiceEntity(
            id = it.dice + actionId + monsterIndex,
            dice = it.dice,
            damage = it.damage.toEntity(actionId),
            actionId = actionId
        )
    }
}
