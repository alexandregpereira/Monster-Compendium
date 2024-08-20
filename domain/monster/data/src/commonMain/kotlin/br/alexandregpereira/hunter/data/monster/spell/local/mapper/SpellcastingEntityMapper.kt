/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
