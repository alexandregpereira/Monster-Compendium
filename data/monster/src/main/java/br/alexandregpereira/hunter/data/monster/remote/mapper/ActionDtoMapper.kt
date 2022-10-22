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

package br.alexandregpereira.hunter.data.monster.remote.mapper

import br.alexandregpereira.hunter.data.monster.remote.model.ActionDto
import br.alexandregpereira.hunter.data.monster.remote.model.DamageDiceDto
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.DamageDice

internal fun List<ActionDto>.toDomain(): List<Action> {
    return this.map {
        Action(
            damageDices = it.damageDices.toDamageDiceDomain(),
            attackBonus = it.attackBonus,
            abilityDescription = AbilityDescription(name = it.name, description = it.description)
        )
    }
}

internal fun List<DamageDiceDto>.toDamageDiceDomain(): List<DamageDice> {
    return this.map {
        DamageDice(
            dice = it.dice,
            damage = it.damage.asDomain()
        )
    }
}
