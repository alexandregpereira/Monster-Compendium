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

package br.alexandregpereira.hunter.data.monster.lore.remote.mapper

import br.alexandregpereira.hunter.data.monster.lore.remote.model.MonsterLoreDto
import br.alexandregpereira.hunter.data.monster.lore.remote.model.MonsterLoreEntryDto
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreEntry
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreStatus

internal fun MonsterLoreDto.toDomain(): MonsterLore {
    return MonsterLore(
        index = index,
        name = "",
        entries = entries.mapIndexed { i, entry ->
            entry.toDomain(monsterLoreIndex = index, index = i)
        },
        status = MonsterLoreStatus.Original,
    )
}

internal fun MonsterLoreEntryDto.toDomain(monsterLoreIndex: String, index: Int): MonsterLoreEntry {
    return MonsterLoreEntry(
        index = "$monsterLoreIndex-$index",
        title = title,
        description = description
    )
}
