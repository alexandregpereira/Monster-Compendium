package br.alexandregpereira.hunter.data.monster.spell.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingSpellUsageCrossRefEntity

@Dao
interface SpellcastingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<SpellcastingEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpellUsageCrossReferences(entities: List<SpellcastingSpellUsageCrossRefEntity>)

    @Query("DELETE FROM SpellcastingEntity")
    suspend fun deleteAll()

    @Query("DELETE FROM SpellcastingSpellUsageCrossRefEntity")
    suspend fun deleteAllSpellUsageCrossReferences()
}
