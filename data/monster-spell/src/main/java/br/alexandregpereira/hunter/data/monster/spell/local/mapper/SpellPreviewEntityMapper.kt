/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
