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
import kotlinx.coroutines.flow.Flow

/**
 * Use [MonsterRemoteRepository] and [MonsterLocalRepository] instead.
 */
interface MonsterRepository {

    fun saveMonsters(monsters: List<Monster>, isSync: Boolean = false): Flow<Unit>
    fun getRemoteMonsters(lang: String = "en-us"): Flow<List<Monster>>
    fun getRemoteMonsters(sourceAcronym: String, lang: String = "en-us"): Flow<List<Monster>>
    fun getLocalMonsterPreviews(): Flow<List<Monster>>
    fun getLocalMonsters(): Flow<List<Monster>>
    fun getLocalMonsters(indexes: List<String>): Flow<List<Monster>>
    fun getLocalMonster(index: String): Flow<Monster>
    fun getLocalMonstersByQuery(query: String): Flow<List<Monster>>
}
