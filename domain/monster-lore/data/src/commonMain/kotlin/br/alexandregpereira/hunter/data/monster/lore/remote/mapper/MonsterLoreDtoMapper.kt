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
        entries = entries.map { it.toDomain() },
        status = MonsterLoreStatus.Original,
    )
}

internal fun MonsterLoreEntryDto.toDomain(): MonsterLoreEntry {
    return MonsterLoreEntry(
        title = title,
        description = description
    )
}
