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

package br.alexandregpereira.hunter.monster.lore.detail.domain

import br.alexandregpereira.hunter.domain.monster.lore.GetMonsterLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

class GetMonsterLoreDetailUseCase(
    private val getMonsterLoreUseCase: GetMonsterLoreUseCase,
    private val getMonsterUseCase: GetMonsterUseCase
) {

    operator fun invoke(index: String): Flow<MonsterLore> {
        return getMonsterLoreUseCase(index).zip(getMonsterUseCase(index)) { monsterLore, monster ->
            monsterLore?.copy(name = monster.name) ?: throw IllegalStateException("MonsterLore not found")
        }
    }
}
