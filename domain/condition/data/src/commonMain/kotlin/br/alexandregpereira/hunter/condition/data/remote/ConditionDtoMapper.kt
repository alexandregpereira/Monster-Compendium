package br.alexandregpereira.hunter.condition.data.remote

import br.alexandregpereira.hunter.condition.Condition
import br.alexandregpereira.hunter.condition.ConditionType
import br.alexandregpereira.ktx.runCatching

internal fun List<ConditionDto>.toDomain(): List<Condition> {
    return mapNotNull { it.toDomain() }
}

internal fun ConditionDto.toDomain(): Condition? {
    val type = runCatching { ConditionType.valueOf(type) }.getOrNull() ?: return null
    return Condition(
        index = index,
        name = name,
        type = type,
        description = description,
    )
}
