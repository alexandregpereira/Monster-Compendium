package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import kotlinx.coroutines.flow.Flow

fun interface GetMonstersByStatus {

    operator fun invoke(status: Set<MonsterStatus>): Flow<List<Monster>>
}

internal fun GetMonstersByStatus(
    repository: MonsterLocalRepository,
): GetMonstersByStatus = GetMonstersByStatus { status ->
    repository.getMonstersByStatus(status)
}
