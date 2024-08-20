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

package br.alexandregpereira.hunter.monster.compendium.domain

import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendium
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class GetMonsterCompendiumBaseUseCase internal constructor(
    private val getTableContentUseCase: GetTableContentUseCase,
    private val getAlphabetUseCase: GetAlphabetUseCase
) {

    operator fun invoke(
        monstersBySectionFlow: Flow<List<MonsterCompendiumItem>>
    ): Flow<MonsterCompendium> {
        return monstersBySectionFlow.map { items ->
            val (tableContent, alphabet) = coroutineScope {
                val tableContentDeferred = async { getTableContentUseCase(items).single() }
                val alphabetDeferred = async { getAlphabetUseCase(items).single() }

                tableContentDeferred.await() to alphabetDeferred.await()
            }

            MonsterCompendium(
                items = items,
                tableContent = tableContent,
                alphabet = alphabet
            )
        }
    }
}
