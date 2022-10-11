package br.alexandregpereira.hunter.data.spell.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.alexandregpereira.hunter.data.spell.local.model.SpellEntity

@Dao
interface SpellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<SpellEntity>)

    @Query("SELECT * FROM SpellEntity WHERE spellIndex == :index")
    suspend fun getSpell(index: String): SpellEntity

    @Query("DELETE FROM SpellEntity")
    suspend fun deleteAll()
}
