/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.monster.spell.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpellcastingDto(
    @SerialName("desc")
    val desc: String,
    @SerialName("type")
    val type: SpellcastingTypeDto,
    @SerialName("spells_by_group")
    val spellsByGroup: List<SpellUsageDto>
)

@Serializable
data class SpellUsageDto(
    @SerialName("group")
    val group: String,
    @SerialName("spells")
    val spells: List<SpellPreviewDto>
)

@Serializable
data class SpellPreviewDto(
    @SerialName("index")
    val index: String,
    @SerialName("name")
    val name: String,
    @SerialName("school")
    val school: SchoolOfMagicDto,
    @SerialName("level")
    val level: Int,
)

@Serializable
enum class SpellcastingTypeDto {
    @SerialName("SPELLCASTER")
    SPELLCASTER,
    @SerialName("INNATE")
    INNATE
}
