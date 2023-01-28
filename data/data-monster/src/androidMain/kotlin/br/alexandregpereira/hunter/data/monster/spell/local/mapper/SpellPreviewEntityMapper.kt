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

import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellPreviewEntity
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellPreview

internal fun List<SpellPreviewEntity>.toDomain(): List<SpellPreview> {
    return map { it.toDomain() }
}

internal fun SpellPreviewEntity.toDomain(): SpellPreview {
    return SpellPreview(
        index = spellIndex,
        name = name,
        level = level,
        school = SchoolOfMagic.valueOf(school),
    )
}

internal fun List<SpellPreview>.toEntity(): List<SpellPreviewEntity> {
    return map {
        SpellPreviewEntity(
            spellIndex = it.index,
            name = it.name,
            level = it.level,
            school = it.school.name,
        )
    }
}
