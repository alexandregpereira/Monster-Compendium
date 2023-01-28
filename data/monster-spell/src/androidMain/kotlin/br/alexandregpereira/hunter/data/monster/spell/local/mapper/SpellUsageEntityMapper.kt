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

import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageCompleteEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageEntity
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage

internal fun List<SpellUsageCompleteEntity>.toDomain(): List<SpellUsage> {
    return map { entity ->
        SpellUsage(
            group = entity.spellUsage.group,
            spells = entity.spells.toDomain()
        )
    }
}

internal fun List<SpellUsage>.toEntity(spellcastingId: String): List<SpellUsageCompleteEntity> {
    return map { usage ->
        SpellUsageCompleteEntity(
            spellUsage = SpellUsageEntity(
                spellUsageId = "${usage.group}-$spellcastingId",
                group = usage.group,
                spellcastingId = spellcastingId
            ),
            spells = usage.spells.toEntity()
        )
    }
}
