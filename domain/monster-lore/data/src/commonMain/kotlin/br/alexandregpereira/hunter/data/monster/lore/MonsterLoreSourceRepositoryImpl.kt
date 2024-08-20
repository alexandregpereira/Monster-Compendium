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

package br.alexandregpereira.hunter.data.monster.lore

import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreSourceRepository
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreSource
import br.alexandregpereira.hunter.domain.source.GetMonsterLoreSourcesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MonsterLoreSourceRepositoryImpl(
    private val getMonsterLoreSourcesUseCase: GetMonsterLoreSourcesUseCase
) : MonsterLoreSourceRepository {


    override fun getMonsterLoreSources(): Flow<List<MonsterLoreSource>> {
        return getMonsterLoreSourcesUseCase().map {
            it.map { source ->
                MonsterLoreSource(
                    name = source.source.name,
                    acronym = source.source.acronym.lowercase()
                )
            }
        }
    }
}
