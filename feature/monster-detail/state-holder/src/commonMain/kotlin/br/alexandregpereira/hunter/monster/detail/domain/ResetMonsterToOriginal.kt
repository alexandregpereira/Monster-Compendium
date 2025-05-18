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
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreStatus
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
    getMonsterLore: GetMonsterLoreUseCase,
    saveMonstersLoreUseCase: SaveMonstersLoreUseCase
) = ResetMonsterToOriginal { monsterIndex ->
    getMonster(monsterIndex).map { monster ->
        monster.copy(status = MonsterStatus.Original)
    }.map { newMonster ->
        saveMonstersUseCase(listOf(newMonster)).single()
        getMonsterLore(newMonster.index).single()
            ?.copy(status = MonsterLoreStatus.Original)?.let { newMonsterLore ->
                saveMonstersLoreUseCase(listOf(newMonsterLore), isSync = false).single()
            }
    }
}
