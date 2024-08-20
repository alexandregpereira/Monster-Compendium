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

package br.alexandregpereira.hunter.data.monster.local.dao

import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntityStatus

interface MonsterDao {

    suspend fun getMonsterPreviews(): List<MonsterEntity>

    suspend fun getMonsterPreviews(indexes: List<String>): List<MonsterEntity>

    suspend fun getMonstersPreviewsEdited(): List<MonsterEntity>

    suspend fun getMonsters(): List<MonsterCompleteEntity>

    suspend fun getMonsters(indexes: List<String>): List<MonsterCompleteEntity>

    suspend fun getMonster(index: String): MonsterCompleteEntity

    suspend fun getMonstersByQuery(query: String): List<MonsterEntity>

    suspend fun insert(monsters: List<MonsterCompleteEntity>, deleteAll: Boolean)

    suspend fun deleteMonster(index: String)

    suspend fun getMonstersByStatus(status: Set<MonsterEntityStatus>): List<MonsterCompleteEntity>
}
