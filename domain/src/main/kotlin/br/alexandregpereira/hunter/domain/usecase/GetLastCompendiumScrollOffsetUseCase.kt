package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.MonsterRepository
import kotlinx.coroutines.flow.Flow

class GetLastCompendiumScrollItemPositionUseCase(
    private val repository: MonsterRepository
) {

    operator fun invoke(): Flow<Int> {
        return repository.getLastCompendiumScrollItemPosition()
    }
}