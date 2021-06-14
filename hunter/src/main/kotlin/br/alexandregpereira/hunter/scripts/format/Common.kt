/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.scripts.format

import br.alexandregpereira.hunter.data.remote.model.MonsterDto
import java.util.Locale

internal const val GITHUB_IMAGE_HOST =
    "https://raw.githubusercontent.com/alexandregpereira/hunter/main/images"

internal fun getImageUrl(index: String): String {
    return "$GITHUB_IMAGE_HOST/$index.png"
}

internal fun calculateAbilityScoreModifier(value: Int): Int {
    return when (value) {
        1 -> -5
        in 2..3 -> -4
        in 4..5 -> -3
        in 6..7 -> -2
        in 8..9 -> -1
        in 10..11 -> 0
        in 12..13 -> 1
        in 14..15 -> 2
        in 16..17 -> 3
        in 18..19 -> 4
        in 20..21 -> 5
        in 22..23 -> 6
        in 24..25 -> 7
        in 26..27 -> 8
        in 28..29 -> 9
        else -> 10
    }
}

internal fun MonsterDto.formatSubtitle(): MonsterDto {
    val subType = if (subtype.isNullOrEmpty()) "," else "($subtype),"
    return this.copy(subtitle = "$size ${type.name.toLowerCase(Locale.ROOT)}$subType $alignment")
}
