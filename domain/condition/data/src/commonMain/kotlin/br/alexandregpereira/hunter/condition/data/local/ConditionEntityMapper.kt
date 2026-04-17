package br.alexandregpereira.hunter.condition.data.local

import br.alexandregpereira.hunter.condition.Condition
import br.alexandregpereira.hunter.condition.ConditionType

internal fun ConditionEntity.toDomain(): Condition {
    return Condition(
        index = index,
        name = name,
        type = ConditionType.valueOf(type),
        description = description,
    )
}

internal fun List<Condition>.toEntity(): List<ConditionEntity> {
    return map { it.toEntity() }
}

internal fun Condition.toEntity(): ConditionEntity {
    return ConditionEntity(
        index = index,
        name = name,
        type = type.name,
        description = description,
    )
}
