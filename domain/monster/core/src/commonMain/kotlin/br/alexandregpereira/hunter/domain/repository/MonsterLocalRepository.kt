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

package br.alexandregpereira.hunter.domain.repository

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import kotlinx.coroutines.flow.Flow

interface MonsterLocalRepository {

    fun saveMonsters(monsters: List<Monster>, isSync: Boolean = false): Flow<Unit>
    fun getMonsterPreviews(): Flow<List<Monster>>
    fun getMonsterPreviewsEdited(): Flow<List<Monster>>
    fun getMonsters(): Flow<List<Monster>>
    fun getMonsters(indexes: List<String>): Flow<List<Monster>>
    fun getMonster(index: String): Flow<Monster>
    fun getMonstersByQuery(query: String): Flow<List<Monster>>
    fun deleteMonster(index: String): Flow<Unit>
    fun getMonstersByStatus(status: Set<MonsterStatus>): Flow<List<Monster>>
}
