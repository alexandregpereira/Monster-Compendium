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

package br.alexandregpereira.hunter.shareContent.domain.mapper

import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.shareContent.domain.model.ShareMonsterLore
import br.alexandregpereira.hunter.shareContent.domain.model.ShareMonsterLoreEntry

internal fun MonsterLore.toShareMonsterLore(): ShareMonsterLore {
    return ShareMonsterLore(
        index = index,
        entries = entries.map {
            ShareMonsterLoreEntry(
                title = it.title,
                description = it.description,
            )
        }
    )
}
