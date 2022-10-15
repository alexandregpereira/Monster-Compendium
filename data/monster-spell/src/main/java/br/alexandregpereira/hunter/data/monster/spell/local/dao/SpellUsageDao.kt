package br.alexandregpereira.hunter.data.monster.spell.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellPreviewEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageSpellCrossRefEntity

@Dao
interface SpellUsageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<SpellUsageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpellCrossReferences(entities: List<SpellUsageSpellCrossRefEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpellPreviews(entities: List<SpellPreviewEntity>)

    @Query("DELETE FROM SpellUsageEntity")
    suspend fun deleteAll()

    @Query("DELETE FROM SpellUsageSpellCrossRefEntity")
    suspend fun deleteAllSpellCrossReferences()
}
