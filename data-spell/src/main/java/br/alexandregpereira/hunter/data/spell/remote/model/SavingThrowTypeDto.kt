package br.alexandregpereira.hunter.data.spell.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SavingThrowTypeDto {
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
