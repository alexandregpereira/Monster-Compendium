package br.alexandregpereira.hunter.monster.registration.domain

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import kotlinx.coroutines.flow.Flow

internal fun interface SaveMonsterUseCase {
    operator fun invoke(monster: Monster): Flow<Unit>
}

internal fun SaveMonsterUseCase(
    saveMonsters: SaveMonstersUseCase,
) = SaveMonsterUseCase { monster ->
    val newMonster = when (monster.status) {
        MonsterStatus.Original -> monster.copy(status = MonsterStatus.Edited)
        MonsterStatus.Edited,
        MonsterStatus.Clone -> monster
    }
    saveMonsters(listOf(newMonster))
}
