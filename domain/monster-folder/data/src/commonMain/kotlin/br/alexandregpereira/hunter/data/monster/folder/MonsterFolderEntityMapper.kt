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

package br.alexandregpereira.hunter.data.monster.folder

import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderImageContentScale
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderType

internal fun List<MonsterFolderCompleteEntity>.asDomain(
    monsterImageContentScale: MonsterPreviewFolderImageContentScale
): List<MonsterFolder> {
    return this.map { it.asDomain(monsterImageContentScale) }
}

internal fun MonsterFolderCompleteEntity.asDomain(
    monsterImageContentScale: MonsterPreviewFolderImageContentScale
): MonsterFolder {
    return MonsterFolder(
        name = monsterFolderEntity.folderName,
        monsters = monsters.asDomainMonsterPreviewFolderEntity(monsterImageContentScale)
    )
}

internal fun List<MonsterEntity>.asDomainMonsterPreviewFolderEntity(
    monsterImageContentScale: MonsterPreviewFolderImageContentScale
): List<MonsterPreviewFolder> {
    return map {
        it.run {
            MonsterPreviewFolder(
                index = index,
                name = name,
                type = MonsterPreviewFolderType.valueOf(type),
                challengeRating = challengeRating.getChallengeRatingFormatted(),
                imageUrl = imageUrl,
                backgroundColorLight = backgroundColorLight,
                backgroundColorDark = backgroundColorDark,
                isHorizontalImage = isHorizontalImage,
                imageContentScale = monsterImageContentScale,
            )
        }
    }
}

private fun Float.getChallengeRatingFormatted(): String {
    return if (this < 1) {
        val value = 1 / this
        "1/${value.toInt()}"
    } else {
        this.toInt().toString()
    }
}
