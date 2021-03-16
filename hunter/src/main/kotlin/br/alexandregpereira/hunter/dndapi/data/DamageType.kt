package br.alexandregpereira.hunter.dndapi.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DamageType(
    @SerialName("index")
    val index: String,
    @SerialName("name")
    val name: String,
    @SerialName("desc")
    val desc: List<String>,
    @SerialName("url")
    val url: String
)
