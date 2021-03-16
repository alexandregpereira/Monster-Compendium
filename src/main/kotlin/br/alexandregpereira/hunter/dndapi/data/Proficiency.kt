package br.alexandregpereira.hunter.dndapi.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Proficiency(
    @SerialName("index")
    val index: String,
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)

@Serializable
data class ProficiencyValue(
    @SerialName("proficiency")
    val proficiency: Proficiency,
    @SerialName("value")
    val value: Int
)