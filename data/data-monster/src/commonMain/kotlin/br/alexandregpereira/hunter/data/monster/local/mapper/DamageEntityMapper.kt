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

import br.alexandregpereira.hunter.data.monster.local.entity.DamageEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageImmunityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageResistanceEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageVulnerabilityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ReactionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.ValueEntity
import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageType

internal fun List<DamageEntity>.toDamageDomain(): List<Damage> {
    return this.map {
        it.toDamageDomain()
    }
}

internal fun DamageEntity.toDamageDomain(): Damage {
    return Damage(
        index = this.value.index,
        type = DamageType.valueOf(this.value.type),
        name = this.value.name
    )
}

internal fun ValueEntity.toDamageDomain(): Damage {
    return Damage(
        index = this.index,
        type = DamageType.valueOf(this.type),
        name = this.name
    )
}

internal fun List<Damage>.toDamageResistanceEntity(
    monsterIndex: String
): List<DamageResistanceEntity> {
    return this.map {
        it.toDamageResistanceEntity(monsterIndex)
    }
}

internal fun List<Damage>.toDamageImmunityEntity(monsterIndex: String): List<DamageImmunityEntity> {
    return this.map {
        it.toDamageImmunityEntity(monsterIndex)
    }
}

internal fun List<Damage>.toDamageVulnerabilityEntity(
    monsterIndex: String
): List<DamageVulnerabilityEntity> {
    return this.map {
        it.toDamageVulnerabilityEntity(monsterIndex)
    }
}

internal fun Damage.toDamageResistanceEntity(monsterIndex: String): DamageResistanceEntity {
    return DamageResistanceEntity(
        value = ValueEntity(
            index = this.index,
            type = this.type.name,
            name = this.name,
            monsterIndex = monsterIndex
        )
    )
}

internal fun Damage.toDamageImmunityEntity(monsterIndex: String): DamageImmunityEntity {
    return DamageImmunityEntity(
        value = ValueEntity(
            index = this.index,
            type = this.type.name,
            name = this.name,
            monsterIndex = monsterIndex
        )
    )
}

internal fun Damage.toDamageVulnerabilityEntity(monsterIndex: String): DamageVulnerabilityEntity {
    return DamageVulnerabilityEntity(
        value = ValueEntity(
            index = this.index,
            type = this.type.name,
            name = this.name,
            monsterIndex = monsterIndex
        )
    )
}

internal fun Damage.toEntity(monsterIndex: String): ValueEntity {
    return ValueEntity(
        index = this.index,
        type = this.type.name,
        name = this.name,
        monsterIndex = monsterIndex
    )
}
internal fun List<AbilityDescription>.toReactionEntity(monsterIndex: String): List<ReactionEntity> {
    return map {
        ReactionEntity(
            name = it.name,
            description = it.description,
            monsterIndex = monsterIndex
        )
    }
}
