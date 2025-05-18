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

package br.alexandregpereira.hunter.data.monster.local.entity

data class MonsterEntity(
    val index: String,
    val type: String,
    val subtype: String?,
    val group: String?,
    val challengeRating: Float,
    val name: String,
    val subtitle: String,
    val imageUrl: String,
    val backgroundColorLight: String,
    val backgroundColorDark: String,
    val isHorizontalImage: Boolean,
    val size: String,
    val alignment: String,
    val armorClass: Int,
    val hitPoints: Int,
    val hitDice: String,
    val senses: String,
    val languages: String,
    val sourceName: String,
    val status: MonsterEntityStatus,
    val imageContentScale: Int?,
)

enum class MonsterEntityStatus {
    Original, Clone, Edited, Imported
}
