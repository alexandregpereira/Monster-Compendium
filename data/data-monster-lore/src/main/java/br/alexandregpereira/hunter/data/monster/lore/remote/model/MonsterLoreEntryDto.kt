package br.alexandregpereira.hunter.data.monster.lore.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonsterLoreEntryDto(
    @SerialName("title")
    val title: String? = null,
    @SerialName("description")
    val description: String,
)
