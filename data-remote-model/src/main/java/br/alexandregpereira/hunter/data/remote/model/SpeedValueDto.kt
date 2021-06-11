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

package br.alexandregpereira.hunter.data.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SpeedDto(
    @SerialName("hover")
    val hover: Boolean,
    @SerialName("value")
    val values: List<SpeedValueDto>,
)

@Serializable
data class SpeedValueDto(
    @SerialName("type")
    val type: SpeedTypeDto,
    @SerialName("measurement_unit")
    val measurementUnit: MeasurementUnitDto,
    @SerialName("value")
    val value: Int,
    @SerialName("value_formatted")
    val valueFormatted: String
)

@Serializable
enum class SpeedTypeDto {
    @SerialName("BURROW")
    BURROW,
    @SerialName("CLIMB")
    CLIMB,
    @SerialName("FLY")
    FLY,
    @SerialName("WALK")
    WALK,
    @SerialName("SWIM")
    SWIM,
}

@Serializable
enum class MeasurementUnitDto(val value: String) {
    @SerialName("FEET")
    FEET(value = "ft."),
    @SerialName("METER")
    METER(value = "m")
}