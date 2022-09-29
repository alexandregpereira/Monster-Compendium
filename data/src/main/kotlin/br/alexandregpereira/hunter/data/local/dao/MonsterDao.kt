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

package br.alexandregpereira.hunter.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import br.alexandregpereira.hunter.data.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.local.entity.MonsterEntity

@Dao
internal interface MonsterDao {

    @Transaction
    @Query("SELECT * FROM MonsterEntity")
    suspend fun getMonsters(): List<MonsterCompleteEntity>

    @Transaction
    @RawQuery
    suspend fun getMonstersByQuery(query: SupportSQLiteQuery): List<MonsterCompleteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(monsters: List<MonsterEntity>)

    @Query("DELETE FROM MonsterEntity")
    suspend fun deleteAll()
}
