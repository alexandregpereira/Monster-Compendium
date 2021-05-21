package br.alexandregpereira.hunter.data

import androidx.room.Database
import androidx.room.RoomDatabase
import br.alexandregpereira.hunter.data.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.local.entity.MonsterEntity

@Database(entities = [MonsterEntity::class], version = 2)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun monsterDao(): MonsterDao
}