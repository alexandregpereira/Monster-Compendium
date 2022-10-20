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

package br.alexandregpereira.hunter.data.monster.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity

@Dao
interface MonsterDao {

    @Query("SELECT * FROM MonsterEntity")
    suspend fun getMonsterPreviews(): List<MonsterEntity>

    @Transaction
    @Query("SELECT * FROM MonsterEntity")
    suspend fun getMonsters(): List<MonsterCompleteEntity>

    @Transaction
    @Query("SELECT * FROM MonsterEntity WHERE `index` IN (:indexes)")
    suspend fun getMonsters(indexes: List<String>): List<MonsterCompleteEntity>

    @Transaction
    @Query("SELECT * FROM MonsterEntity WHERE `index` == :index")
    fun getMonster(index: String): MonsterCompleteEntity

    @RawQuery
    suspend fun getMonstersByQuery(query: SupportSQLiteQuery): List<MonsterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(monsters: List<MonsterEntity>)

    @Query("DELETE FROM MonsterEntity")
    suspend fun deleteAll()
}
