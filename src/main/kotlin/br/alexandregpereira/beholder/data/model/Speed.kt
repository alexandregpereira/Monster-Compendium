package br.alexandregpereira.beholder.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Speed(
    @SerialName("type")
    val type: SpeedType,
    @SerialName("measurement_unit")
    val measurementUnit: MeasurementUnit,
    @SerialName("measurement_unit_abbreviation")
    val measurementUnitAbbreviation: String,
    @SerialName("value_formatted")
    val valueFormatted: String,
    @SerialName("value")
    val value: String,
)

@Serializable
enum class SpeedType {
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
enum class MeasurementUnit {
    @SerialName("FEET")
    FEET,
    @SerialName("METER")
    METER
}