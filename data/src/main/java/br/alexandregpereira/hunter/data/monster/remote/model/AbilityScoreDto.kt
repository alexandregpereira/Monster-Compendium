package br.alexandregpereira.hunter.data.monster.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AbilityScoreDto(
    @SerialName("type")
    val type: AbilityScoreTypeDto,
    @SerialName("value")
    val value: Int,
    @SerialName("modifier")
    val modifier: Int
)

@Serializable
data class SavingThrowDto(
    @SerialName("type")
    val type: AbilityScoreTypeDto,
    @SerialName("modifier")
    val modifier: Int
)

@Serializable
data class SkillDto(
    @SerialName("index")
    val index: String,
    @SerialName("modifier")
    val modifier: Int
)

@Serializable
enum class AbilityScoreTypeDto {
    @SerialName("STRENGTH")
    STRENGTH,
    @SerialName("DEXTERITY")
    DEXTERITY,
    @SerialName("CONSTITUTION")
    CONSTITUTION,
    @SerialName("INTELLIGENCE")
    INTELLIGENCE,
    @SerialName("WISDOM")
    WISDOM,
    @SerialName("CHARISMA")
    CHARISMA
}
