/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.folder

import br.alexandregpereira.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.data.local.entity.MonsterFolderCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.mapper.toDomain

internal fun List<MonsterFolderCompleteEntity>.asDomain(): List<MonsterFolder> {
    return this.map { it.asDomain() }
}

internal fun MonsterFolderCompleteEntity.asDomain(): MonsterFolder {
    return MonsterFolder(
        name = monsterFolderEntity.folderName,
        monsters = monsters.toDomain()
    )
}
