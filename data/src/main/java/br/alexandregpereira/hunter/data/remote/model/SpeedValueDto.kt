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
    @SerialName("HOVER")
    HOVER,
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