package br.alexandregpereira.hunter.data.monster.spell.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SchoolOfMagicDto {
    @SerialName("ABJURATION")
    ABJURATION,
    @SerialName("CONJURATION")
    CONJURATION,
    @SerialName("DIVINATION")
    DIVINATION,
    @SerialName("ENCHANTMENT")
    ENCHANTMENT,
    @SerialName("EVOCATION")
    EVOCATION,
    @SerialName("ILLUSION")
    ILLUSION,
    @SerialName("NECROMANCY")
    NECROMANCY,
    @SerialName("TRANSMUTATION")
    TRANSMUTATION,
}