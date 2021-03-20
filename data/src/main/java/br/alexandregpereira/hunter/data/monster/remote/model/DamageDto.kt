package br.alexandregpereira.hunter.data.monster.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DamageDto(
    @SerialName("index")
    val index: String,
    @SerialName("type")
    val type: DamageTypeDto,
    @SerialName("name")
    val name: String
)

enum class DamageTypeDto {
    ACID,
    BLUDGEONING,
    COLD,
    FIRE,
    LIGHTNING,
    NECROTIC,
    PIERCING,
    POISON,
    PSYCHIC,
    RADIANT,
    SLASHING,
    THUNDER,
    OTHER,
}