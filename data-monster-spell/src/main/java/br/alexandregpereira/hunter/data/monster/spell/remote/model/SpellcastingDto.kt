package br.alexandregpereira.hunter.data.monster.spell.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpellcastingDto(
    @SerialName("desc")
    val desc: String,
    @SerialName("type")
    val type: SpellcastingTypeDto,
    @SerialName("spells_by_group")
    val spellsByGroup: List<SpellUsageDto>
)

@Serializable
data class SpellUsageDto(
    @SerialName("group")
    val group: String,
    @SerialName("spells")
    val spells: List<SpellPreviewDto>
)

@Serializable
data class SpellPreviewDto(
    @SerialName("index")
    val index: String,
    @SerialName("name")
    val name: String,
    @SerialName("school")
    val school: SchoolOfMagicDto,
    @SerialName("level")
    val level: Int,
)

@Serializable
enum class SpellcastingTypeDto {
    @SerialName("SPELLCASTER")
    SPELLCASTER,
    @SerialName("INNATE")
    INNATE
}
