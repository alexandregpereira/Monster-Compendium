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

package br.alexandregpereira.hunter.data.monster.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.alexandregpereira.hunter.data.monster.local.entity.DamageImmunityEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageResistanceEntity
import br.alexandregpereira.hunter.data.monster.local.entity.DamageVulnerabilityEntity

@Dao
interface DamageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVulnerability(entities: List<DamageVulnerabilityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResistance(entities: List<DamageResistanceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImmunity(entities: List<DamageImmunityEntity>)

    @Query("DELETE FROM DamageVulnerabilityEntity")
    suspend fun deleteAllVulnerabilities()

    @Query("DELETE FROM DamageResistanceEntity")
    suspend fun deleteAllResistances()

    @Query("DELETE FROM DamageImmunityEntity")
    suspend fun deleteAllImmunities()
}
