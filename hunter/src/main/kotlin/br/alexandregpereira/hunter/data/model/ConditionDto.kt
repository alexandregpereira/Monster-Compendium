package br.alexandregpereira.hunter.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ConditionDto(
    val index: String,
    val type: ConditionTypeDto,
    val name: String
)

enum class ConditionTypeDto {
    BLINDED,
    CHARMED,
    DEAFENED,
    EXHAUSTION,
    FRIGHTENED,
    GRAPPLED,
    PARALYZED,
    PETRIFIED,
    POISONED,
    PRONE,
    RESTRAINED,
    STUNNED,
    UNCONSCIOUS,
}
