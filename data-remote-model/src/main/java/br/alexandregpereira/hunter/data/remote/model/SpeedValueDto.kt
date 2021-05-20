/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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