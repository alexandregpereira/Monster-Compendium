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

package br.alexandregpereira.hunter.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlternativeSourceDto(
    @SerialName("source")
    val source: SourceDto,
    @SerialName("totalMonsters")
    val totalMonsters: Int,
    @SerialName("summary")
    val summary: String = "",
    @SerialName("coverImageUrl")
    val coverImageUrl: String = "",
    @SerialName("isEnabled")
    val isEnabled: Boolean = true,
    @SerialName("isLoreEnabled")
    val isLoreEnabled: Boolean = true,
)
