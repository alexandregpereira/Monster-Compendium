package br.alexandregpereira.hunter.condition

data class Condition(
    val index: String,
    val name: String,
    val type: ConditionType,
    val description: String,
)

enum class ConditionType {
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
