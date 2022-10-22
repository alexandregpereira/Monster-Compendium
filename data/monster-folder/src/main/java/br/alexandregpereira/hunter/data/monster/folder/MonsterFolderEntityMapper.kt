/*
 * Copyright 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster.folder

import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderType

internal fun List<MonsterFolderCompleteEntity>.asDomain(): List<MonsterFolder> {
    return this.map { it.asDomain() }
}

internal fun MonsterFolderCompleteEntity.asDomain(): MonsterFolder {
    return MonsterFolder(
        name = monsterFolderEntity.folderName,
        monsters = monsters.asDomain()
    )
}

@JvmName("asDomainMonsterPreviewFolderEntity")
private fun List<MonsterEntity>.asDomain(): List<MonsterPreviewFolder> {
    return map {
        it.run {
            MonsterPreviewFolder(
                index = index,
                name = name,
                type = MonsterPreviewFolderType.valueOf(type),
                challengeRating = challengeRating,
                imageUrl = imageUrl,
                backgroundColorLight = backgroundColorLight,
                backgroundColorDark = backgroundColorDark,
                isHorizontalImage = isHorizontalImage,
            )
        }
    }
}
