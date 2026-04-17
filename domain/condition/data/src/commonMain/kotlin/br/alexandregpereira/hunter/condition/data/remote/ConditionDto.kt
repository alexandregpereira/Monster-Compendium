package br.alexandregpereira.hunter.condition.data.remote

import kotlinx.serialization.Serializable

@Serializable
internal data class ConditionDto(
    val index: String,
    val name: String,
    val type: String,
    val description: String,
)
