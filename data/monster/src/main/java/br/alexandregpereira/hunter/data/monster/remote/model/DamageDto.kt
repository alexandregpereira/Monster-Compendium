/*
 * Hunter - DnD 5th edition monster compendium application
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

package br.alexandregpereira.hunter.data.monster.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DamageDto(
    @SerialName("index")
    val index: String,
    @SerialName("type")
    val type: DamageTypeDto,
    @SerialName("name")
    val name: String
)

@Serializable
enum class DamageTypeDto {
    @SerialName("ACID")
    ACID,
    @SerialName("BLUDGEONING")
    BLUDGEONING,
    @SerialName("COLD")
    COLD,
    @SerialName("FIRE")
    FIRE,
    @SerialName("LIGHTNING")
    LIGHTNING,
    @SerialName("NECROTIC")
    NECROTIC,
    @SerialName("PIERCING")
    PIERCING,
    @SerialName("POISON")
    POISON,
    @SerialName("PSYCHIC")
    PSYCHIC,
    @SerialName("RADIANT")
    RADIANT,
    @SerialName("SLASHING")
    SLASHING,
    @SerialName("THUNDER")
    THUNDER,
    @SerialName("OTHER")
    OTHER,
}
