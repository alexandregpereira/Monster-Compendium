/*
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

package br.alexandregpereira.hunter.domain.folder.model

data class MonsterPreviewFolder(
    val index: String,
    val name: String = "",
    val type: MonsterPreviewFolderType = MonsterPreviewFolderType.ABERRATION,
    val challengeRating: Float = 0f,
    val imageUrl: String = "",
    val backgroundColorLight: String,
    val backgroundColorDark: String,
    val isHorizontalImage: Boolean,
)
