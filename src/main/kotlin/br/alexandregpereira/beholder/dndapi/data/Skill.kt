package br.alexandregpereira.beholder.dndapi.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Skill(
    @SerialName("index")
    val index: String,
    @SerialName("name")
    val name: String,
    @SerialName("desc")
    val desc: List<String>,
    @SerialName("ability_score")
    val abilityScore: APIReference,
    @SerialName("url")
    val url: String
)
