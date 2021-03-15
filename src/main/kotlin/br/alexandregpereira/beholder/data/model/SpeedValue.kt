package br.alexandregpereira.beholder.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Speed(
    @SerialName("type")
    val hover: Boolean,
    @SerialName("value")
    val values: List<SpeedValue>,
)

@Serializable
data class SpeedValue(
    @SerialName("type")
    val type: SpeedType,
    @SerialName("measurement_unit")
    val measurementUnit: MeasurementUnit,
    @SerialName("value")
    val distance: Float,
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
enum class MeasurementUnit(value: String) {
    @SerialName("FEET")
    FEET(value = "ft."),
    @SerialName("METER")
    METER(value = "m")
}