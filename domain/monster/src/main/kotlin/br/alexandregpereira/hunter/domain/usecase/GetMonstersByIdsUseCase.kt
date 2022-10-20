/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMonstersByIdsUseCase @Inject internal constructor(
    private val repository: MonsterRepository
) {

    operator fun invoke(ids: List<String>): Flow<List<Monster>> {
        return repository.getLocalMonsters(indexes = ids).map { monsters ->
            val aggregator = mutableListOf<Monster>()
            ids.forEach { monsterIndex ->
                aggregator.add(
                    monsters.find { it.index == monsterIndex }
                        ?: throw IllegalAccessError("$monsterIndex not found")
                )
            }
            aggregator
        }
    }
}
