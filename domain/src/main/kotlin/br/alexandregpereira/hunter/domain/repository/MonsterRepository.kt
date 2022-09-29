/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.domain.repository

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.Source
import kotlinx.coroutines.flow.Flow

interface MonsterRepository {

    fun saveMonsters(monsters: List<Monster>, isSync: Boolean = false): Flow<Unit>
    fun getRemoteMonsters(): Flow<List<Monster>>
    fun getRemoteMonsters(source: Source): Flow<List<Monster>>
    fun getLocalMonsters(): Flow<List<Monster>>
    fun getLocalMonster(index: String): Flow<Monster>
    fun getLocalMonstersByQuery(query: String): Flow<List<Monster>>
}
