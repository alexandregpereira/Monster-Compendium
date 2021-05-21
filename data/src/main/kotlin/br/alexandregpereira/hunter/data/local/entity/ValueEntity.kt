package br.alexandregpereira.hunter.data.local.entity

import kotlinx.serialization.Serializable

@Serializable
internal data class ValueEntity(
    val index: String,
    val type: String,
    val name: String
)
