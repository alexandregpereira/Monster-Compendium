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
        MonsterStatus.Imported,
        MonsterStatus.Clone -> monster
    }
    saveMonsters(listOf(newMonster))
}
