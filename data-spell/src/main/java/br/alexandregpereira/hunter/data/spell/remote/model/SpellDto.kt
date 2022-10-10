package br.alexandregpereira.hunter.data.spell.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpellDto(
    @SerialName("index")
    val index: String,
    @SerialName("name")
    val name: String,
    @SerialName("level")
    val level: Int,
    @SerialName("casting_time")
    val castingTime: String,
    @SerialName("components")
    val components: String,
    @SerialName("duration")
    val duration: String,
    @SerialName("range")
    val range: String,
    @SerialName("ritual")
    val ritual: Boolean,
    @SerialName("concentration")
    val concentration: Boolean,
    @SerialName("saving_throw_type")
    val savingThrowType: SavingThrowTypeDto?,
    @SerialName("damage_type")
    val damageType: String?,
    @SerialName("school")
    val school: SchoolOfMagicDto,
    @SerialName("description")
    val description: String,
    @SerialName("higher_level")
    val higherLevel: String?
)
