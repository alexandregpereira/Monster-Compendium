package br.alexandregpereira.hunter.data.local.entity

import kotlinx.serialization.Serializable

@Serializable
data class ConditionEntity(
    val index: String,
    val type: String,
    val name: String
)
