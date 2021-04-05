package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.MonsterRepository
import kotlinx.coroutines.flow.Flow

class SaveCompendiumScrollItemPositionUseCase(
    private val repository: MonsterRepository
) {

    operator fun invoke(position: Int): Flow<Unit> {
        return repository.saveCompendiumScrollItemPosition(position)
    }
}