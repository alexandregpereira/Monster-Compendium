package br.alexandregpereira.hunter.domain.monster.lore

import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import kotlinx.coroutines.flow.Flow

fun interface SaveMonstersLoreUseCase {

    operator fun invoke(monsterLore: List<MonsterLore>, isSync: Boolean): Flow<Unit>
}

internal fun SaveMonstersLoreUseCase(
    repository: MonsterLoreLocalRepository
) = SaveMonstersLoreUseCase { monsterLore, isSync ->
    repository.save(monsterLore, isSync)
}
