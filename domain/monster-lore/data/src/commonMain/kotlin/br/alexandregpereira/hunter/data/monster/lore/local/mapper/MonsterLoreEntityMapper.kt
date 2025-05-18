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

package br.alexandregpereira.hunter.data.monster.lore.local.mapper

import br.alexandregpereira.hunter.data.monster.lore.local.entity.MonsterLoreCompleteEntity
import br.alexandregpereira.hunter.data.monster.lore.local.entity.MonsterLoreEntity
import br.alexandregpereira.hunter.data.monster.lore.local.entity.MonsterLoreEntityStatus
import br.alexandregpereira.hunter.data.monster.lore.local.entity.MonsterLoreEntryEntity
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreEntry
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreStatus

internal fun MonsterLoreCompleteEntity.toDomain(): MonsterLore {
    return MonsterLore(
        index = monsterLore.monsterLoreIndex,
        name = "",
        entries = entries.mapIndexed { i, entry -> entry.toDomain(index = i) },
        status = when (monsterLore.status) {
            MonsterLoreEntityStatus.Original -> MonsterLoreStatus.Original
            MonsterLoreEntityStatus.Imported -> MonsterLoreStatus.Imported
        }
    )
}

internal fun MonsterLoreEntryEntity.toDomain(index: Int): MonsterLoreEntry {
    return MonsterLoreEntry(
        index = "$monsterIndex-$index",
        title = title,
        description = description
    )
}

internal fun MonsterLore.toEntity(): MonsterLoreCompleteEntity {
    return MonsterLoreCompleteEntity(
        monsterLore = MonsterLoreEntity(
            monsterLoreIndex = index,
            status = when (status) {
                MonsterLoreStatus.Original -> MonsterLoreEntityStatus.Original
                MonsterLoreStatus.Imported -> MonsterLoreEntityStatus.Imported
            }
        ),
        entries = entries.map { it.toEntity(index) }
    )
}

internal fun MonsterLoreEntry.toEntity(monsterLoreIndex: String): MonsterLoreEntryEntity {
    return MonsterLoreEntryEntity(
        id = index,
        title = title,
        description = description,
        monsterIndex = monsterLoreIndex
    )
}
