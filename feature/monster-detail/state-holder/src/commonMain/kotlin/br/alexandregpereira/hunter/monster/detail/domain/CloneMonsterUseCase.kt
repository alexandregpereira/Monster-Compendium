package br.alexandregpereira.hunter.monster.detail.domain

import br.alexandregpereira.hunter.domain.monster.lore.GetMonsterLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.SaveMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

fun interface CloneMonsterUseCase {

    operator fun invoke(monsterIndex: String, monsterName: String): Flow<String>
}

internal fun CloneMonsterUseCase(
    getMonster: GetMonsterUseCase,
    getMonsterLore: GetMonsterLoreUseCase,
    saveMonsters: SaveMonstersUseCase,
    saveMonstersLore: SaveMonstersLoreUseCase,
) = CloneMonsterUseCase { monsterIndex, monsterName ->
    getMonster(monsterIndex)
        .map { monster ->
            val monsterNameIndex = monsterName.lowercase().replace(" ", "-")
            monster.copy(
                index = "$monsterNameIndex-$monsterIndex-k4k4sh1",
                name = monsterName,
                isClone = true,
            )
        }
        .map { monster ->
            val newMonsterLore = runCatching {
                getMonsterLore(monsterIndex).single()
            }.getOrNull()?.copy(index = monster.index)

            monster to newMonsterLore
        }
        .map { (monster, monsterLore) ->
            saveMonsters(listOf(monster)).single()
            monsterLore?.let { saveMonstersLore(listOf(monsterLore), isSync = false).single() }
            monster.index
        }
}
