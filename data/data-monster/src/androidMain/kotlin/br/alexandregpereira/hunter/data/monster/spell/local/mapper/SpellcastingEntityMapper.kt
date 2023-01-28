/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster.spell.local.mapper

import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingCompleteEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingEntity
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellcastingType

fun List<SpellcastingCompleteEntity>.toDomain(): List<Spellcasting> {
    return map { entity ->
        Spellcasting(
            description = entity.spellcasting.description,
            type = SpellcastingType.valueOf(entity.spellcasting.type),
            usages = entity.usages.toDomain()
        )
    }
}

fun List<Spellcasting>.toEntity(monsterIndex: String): List<SpellcastingCompleteEntity> {
    return map { spellcasting ->
        val spellcastingId = "${spellcasting.type}-$monsterIndex"
        SpellcastingCompleteEntity(
            spellcasting = SpellcastingEntity(
                spellcastingId = spellcastingId,
                type = spellcasting.type.name,
                description = spellcasting.description,
                monsterIndex = monsterIndex
            ),
            usages = spellcasting.usages.toEntity(spellcastingId)
        )
    }
}
