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

package br.alexandregpereira.hunter.data.monster.spell.remote.mapper

import br.alexandregpereira.hunter.data.monster.spell.remote.model.SpellUsageDto
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellPreview
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage

fun List<SpellUsageDto>.toDomain(): List<SpellUsage> {
    return map { spellUsage ->
        SpellUsage(
            group = spellUsage.group,
            spells = spellUsage.spells.map { monsterSpell ->
                SpellPreview(
                    index = monsterSpell.index,
                    name = monsterSpell.name,
                    level = monsterSpell.level,
                    school = SchoolOfMagic.valueOf(monsterSpell.school.name),
                )
            }
        )
    }
}
