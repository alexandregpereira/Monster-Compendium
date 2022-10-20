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

package br.alexandregpereira.hunter.data.spell.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpellDto(
    @SerialName("index")
    val index: String,
    @SerialName("name")
    val name: String,
    @SerialName("level")
    val level: Int,
    @SerialName("casting_time")
    val castingTime: String,
    @SerialName("components")
    val components: String,
    @SerialName("duration")
    val duration: String,
    @SerialName("range")
    val range: String,
    @SerialName("ritual")
    val ritual: Boolean,
    @SerialName("concentration")
    val concentration: Boolean,
    @SerialName("saving_throw_type")
    val savingThrowType: SavingThrowTypeDto?,
    @SerialName("damage_type")
    val damageType: String?,
    @SerialName("school")
    val school: SchoolOfMagicDto,
    @SerialName("description")
    val description: String,
    @SerialName("higher_level")
    val higherLevel: String?
)
