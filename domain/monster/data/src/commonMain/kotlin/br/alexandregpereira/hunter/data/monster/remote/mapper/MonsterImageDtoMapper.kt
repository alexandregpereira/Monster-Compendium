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

package br.alexandregpereira.hunter.data.monster.remote.mapper

import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageContentScaleDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageDto
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale

internal fun List<MonsterImageDto>.toDomain(): List<MonsterImage> {
    return this.map { it.toMonsterImage() }
}

internal fun MonsterImageDto.toMonsterImage(): MonsterImage {
    return MonsterImage(
        monsterIndex = this.monsterIndex,
        backgroundColor = this.backgroundColor?.toDomain(),
        imageUrl = this.imageUrl,
        contentScale = this.contentScale?.toContentScale(),
    )
}

internal fun MonsterImage.toMonsterImageDto(): MonsterImageDto {
    return MonsterImageDto(
        monsterIndex = monsterIndex,
        backgroundColor = backgroundColor?.toColorDto(),
        imageUrl = imageUrl,
        contentScale = contentScale?.let {
            when (it) {
                MonsterImageContentScale.Fit -> MonsterImageContentScaleDto.Fit
                MonsterImageContentScale.Crop -> MonsterImageContentScaleDto.Crop
            }
        },
    )
}

internal fun MonsterImageContentScaleDto.toContentScale(): MonsterImageContentScale {
    return when (this) {
        MonsterImageContentScaleDto.Fit -> MonsterImageContentScale.Fit
        MonsterImageContentScaleDto.Crop -> MonsterImageContentScale.Crop
    }
}

internal fun MonsterImageContentScale.toContentScaleDto(): MonsterImageContentScaleDto {
    return when (this) {
        MonsterImageContentScale.Fit -> MonsterImageContentScaleDto.Fit
        MonsterImageContentScale.Crop -> MonsterImageContentScaleDto.Crop
    }
}
