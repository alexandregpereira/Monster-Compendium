package br.alexandregpereira.beholder.dndapi.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Response(
    @SerialName("count")
    val count: Int,
    @SerialName("results")
    val results: List<APIReference>
)
