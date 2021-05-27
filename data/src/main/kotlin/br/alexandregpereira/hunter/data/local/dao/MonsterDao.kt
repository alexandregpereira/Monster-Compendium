package br.alexandregpereira.hunter.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.alexandregpereira.hunter.data.local.entity.MonsterEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface MonsterDao {

    @Query("SELECT * FROM monster")
    fun getMonsters(): Flow<List<MonsterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(monsters: List<MonsterEntity>)

    @Query("DELETE FROM monster")
    suspend fun deleteAll()
}