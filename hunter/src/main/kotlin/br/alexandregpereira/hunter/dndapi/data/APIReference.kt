package br.alexandregpereira.hunter.dndapi.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class APIReference(
    @SerialName("index")
    val index: String,
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)