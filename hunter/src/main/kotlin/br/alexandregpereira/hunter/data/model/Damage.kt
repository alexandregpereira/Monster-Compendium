package br.alexandregpereira.hunter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Damage(
    @SerialName("index")
    val index: String,
    @SerialName("type")
    val type: DamageType,
    @SerialName("name")
    val name: String
)

enum class DamageType {
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