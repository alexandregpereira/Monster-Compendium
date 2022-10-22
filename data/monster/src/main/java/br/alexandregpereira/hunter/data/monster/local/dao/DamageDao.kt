/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
