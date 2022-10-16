/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022. Alexandre Gomes Pereira.
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
