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

package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.model.MonsterImageData

fun List<Monster>.appendMonsterImages(
    monsterImages: List<MonsterImage>
): List<Monster> = map { monster ->
    val monsterImage = monsterImages.firstOrNull { monsterImage ->
        monsterImage.monsterIndex == monster.index
    } ?: MonsterImage(
        monsterIndex = monster.index,
        backgroundColor = Color(light = "#e0dfd1", dark = "#e0dfd1"),
        isHorizontalImage = false,
        imageUrl = DEFAULT_IMAGE_BASE_URL + "default-${monster.type.name.lowercase()}.png",
        contentScale = null,
    )

    monster.copy(
        imageData = MonsterImageData(
            url = monsterImage.imageUrl,
            backgroundColor = monsterImage.backgroundColor,
            isHorizontal = monsterImage.isHorizontalImage,
            contentScale = monsterImage.contentScale,
        )
    )
}

private const val DEFAULT_IMAGE_BASE_URL =
    "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images/"