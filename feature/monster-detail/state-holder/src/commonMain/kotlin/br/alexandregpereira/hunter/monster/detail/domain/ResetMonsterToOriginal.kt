package br.alexandregpereira.hunter.monster.detail.domain

import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

internal fun interface ResetMonsterToOriginal {
    operator fun invoke(monsterIndex: String): Flow<Unit>
}

internal fun ResetMonsterToOriginal(
    getMonster: GetMonsterUseCase,
    saveMonstersUseCase: SaveMonstersUseCase,
) = ResetMonsterToOriginal { monsterIndex ->
    getMonster(monsterIndex).map { monster ->
        monster.copy(status = MonsterStatus.Original)
    }.map { newMonster ->
        saveMonstersUseCase(listOf(newMonster)).single()
    }
}
