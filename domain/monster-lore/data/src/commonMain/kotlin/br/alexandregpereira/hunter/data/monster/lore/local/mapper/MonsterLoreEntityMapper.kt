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
        entries = entries.map { it.toDomain() },
        status = when (monsterLore.status) {
            MonsterLoreEntityStatus.Original -> MonsterLoreStatus.Original
            MonsterLoreEntityStatus.Imported -> MonsterLoreStatus.Imported
        }
    )
}

internal fun MonsterLoreEntryEntity.toDomain(): MonsterLoreEntry {
    return MonsterLoreEntry(
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
        id = monsterLoreIndex + "-" + (title ?: "lore"),
        title = title,
        description = description,
        monsterIndex = monsterLoreIndex
    )
}
