/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.monster.detail.domain

import br.alexandregpereira.hunter.domain.model.MonsterStatus
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
                status = MonsterStatus.Clone,
            )
        }
        .map { monster ->
            monster.copy(
                actions = monster.actions.map { it.copy(id = "") },
                legendaryActions = monster.legendaryActions.map { it.copy(id = "") },
            )
        }
        .map { monster ->
            val newMonsterLore = runCatching {
                getMonsterLore(monsterIndex).single()
            }.getOrNull()?.run {
                copy(
                    index = monster.index,
                    entries = entries.mapIndexed { i, entry ->
                        entry.copy(
                            index = "${monster.index}-$i",
                        )
                    },
                )
            }

            monster to newMonsterLore
        }
        .map { (monster, monsterLore) ->
            saveMonsters(listOf(monster)).single()
            monsterLore?.let { saveMonstersLore(listOf(monsterLore), isSync = false).single() }
            monster.index
        }
}
