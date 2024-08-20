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

package br.alexandregpereira.hunter.data.monster.local

import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntityStatus
import kotlinx.coroutines.flow.Flow

internal interface MonsterLocalDataSource {

    fun getMonsterPreviews(): Flow<List<MonsterEntity>>
    fun getMonsterPreviewsEdited(): Flow<List<MonsterEntity>>
    fun getMonsters(): Flow<List<MonsterCompleteEntity>>
    fun getMonsters(indexes: List<String>): Flow<List<MonsterCompleteEntity>>
    fun getMonstersByQuery(query: String): Flow<List<MonsterEntity>>
    fun saveMonsters(monsters: List<MonsterCompleteEntity>, isSync: Boolean): Flow<Unit>
    fun getMonster(index: String): Flow<MonsterCompleteEntity>
    fun deleteMonster(index: String): Flow<Unit>
    fun getMonstersByStatus(status: Set<MonsterEntityStatus>): Flow<List<MonsterCompleteEntity>>
}
