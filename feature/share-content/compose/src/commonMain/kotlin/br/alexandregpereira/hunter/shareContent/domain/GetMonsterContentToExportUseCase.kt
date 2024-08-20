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

package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.hunter.domain.monster.lore.GetMonstersLoreByIdsUseCase
import br.alexandregpereira.hunter.domain.spell.GetSpellsByIdsUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersByIdsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun interface GetMonstersContentToExport {

    operator fun invoke(monsterIndexes: List<String>): Flow<String>
}

internal fun GetMonstersContentToExport(
    getMonsters: GetMonstersByIdsUseCase,
    getMonstersLore: GetMonstersLoreByIdsUseCase,
    getSpellsByIds: GetSpellsByIdsUseCase,
    getMonstersContentEditedToExport: GetMonstersContentEditedToExport,
): GetMonstersContentToExport = GetMonstersContentToExport { monsterIndexes ->
    if (monsterIndexes.isEmpty()) {
        getMonstersContentEditedToExport()
    } else {
        getMonsters(monsterIndexes).map { monsters ->
            monsters.getContentToExport(getMonstersLore, getSpellsByIds)
        }
    }
}
